package com.amit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.amit.entity.ProductEntity;
import com.amit.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class ProductRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testSaveProductSuccess() throws Exception {
		ProductEntity mockProduct = new ProductEntity(1, "Mouse", 200.0);

		// Mock the behavior of the ProductService
		when(productService.saveProduct(any(ProductEntity.class))).thenReturn("Saved successfully");

	
		// Perform the POST request
		mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockProduct)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().string("Saved successfully"));
	}

	@Test
	public void testSaveProductFailure() throws Exception {
		ProductEntity mockProduct = new ProductEntity(1, "Mouse", 200.0);

		// Mock the behavior of the ProductService
		when(productService.saveProduct(any(ProductEntity.class))).thenReturn("Problem occured");

		// Perform the POST request
		mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockProduct)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.content().string("Problem occured"));
	}

	@Test
	public void testGetAllProductsSuccess() throws Exception {
		// Create a list of sample products
		List<ProductEntity> sampleProducts = new ArrayList<>();
		sampleProducts.add(new ProductEntity(1, "Product 1", 2000.0));
		sampleProducts.add(new ProductEntity(2, "Product 2", 3000.0));

		// Mock the behavior of the ProductService
		when(productService.getProducts()).thenReturn(sampleProducts);

		// Perform the GET request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/products")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(sampleProducts.size()));
	}

	@Test
    public void testGetAllProductsEmpty() throws Exception {
        // Mock the behavior of the ProductService
        when(productService.getProducts()).thenReturn(new ArrayList<>());

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

	@Test
	public void testGetProductByIdExisting() throws Exception {
		Integer pId = 1;

		// Create a sample product for the response
		ProductEntity sampleProduct = new ProductEntity(pId, "Phone", 5000.0);

		// Mock the behavior of the ProductService
		when(productService.getProductById(pId)).thenReturn(sampleProduct);

		// Perform the GET request
		mockMvc.perform(MockMvcRequestBuilders.get("/getProduct/{pId}", pId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(sampleProduct.getProductId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.priductName").value(sampleProduct.getPriductName()));
	}

	
	@Test
	public void testGetProductByIdNonExisting() throws Exception {
		Integer pId = 1;

		// Mock the behavior of the ProductService
		when(productService.getProductById(pId)).thenReturn(null);

		// Perform the GET request
		mockMvc.perform(MockMvcRequestBuilders.get("/getProduct/{pId}", pId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	
	@Test
	public void testUpdateProductSuccess() throws Exception {
		ProductEntity sampleProduct = new ProductEntity(1, "Dell", 80000.0);

		// Mock the behavior of the ProductService
		when(productService.updateProductById(any(ProductEntity.class))).thenReturn("Product Updated Successfully");

		// Perform the PUT request
		mockMvc.perform(MockMvcRequestBuilders.put("/update/{pId}", sampleProduct.getProductId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sampleProduct)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Product Updated Successfully"));
	}

	@Test
	public void testUpdateProductFailure() throws Exception {
		// Create a sample ProductEntity for the request body
		ProductEntity sampleProduct = new ProductEntity(1, "Dell", 80000.0);

		// Mock the behavior of the ProductService
		when(productService.updateProductById(any(ProductEntity.class))).thenReturn("Problem occured");

		// Perform the PUT request
		mockMvc.perform(MockMvcRequestBuilders.put("/update/{pId}", sampleProduct.getProductId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sampleProduct)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.content().string("Problem occured"));
	}
	
	
	
	@Test
    public void testDeleteProductSuccess() throws Exception {
        int productId = 1;

        // Mock the behavior of the ProductService
        when(productService.deleteById(productId)).thenReturn("product deleted");

        // Perform the DELETE request
        
        mockMvc.perform(MockMvcRequestBuilders.get("/delete/{pId}", productId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("product deleted"));
    }

    @Test
    public void testDeleteProductFailure() throws Exception {
        Integer productId = 1;

        // Mock the behavior of the ProductService
        when(productService.deleteById(productId)).thenReturn("Record not found");

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.get("/delete/{pId}", productId))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.content().string("Record not found"));
    }

}