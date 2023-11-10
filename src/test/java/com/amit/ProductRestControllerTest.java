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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class ProductRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService proService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void saveSuccess() throws JsonProcessingException, Exception {

		ProductEntity entity = new ProductEntity(1, "CPU", 5000.00);

		when(proService.saveProduct(any(ProductEntity.class))).thenReturn("Saved successfully");

		mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entity))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().string("Saved successfully"));
	}

	@Test
	public void saveFailure() throws JsonProcessingException, Exception {

		ProductEntity entity = new ProductEntity(1, "CPU", 5000.00);

		when(proService.saveProduct(any(ProductEntity.class))).thenReturn("Problem occured");

		mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entity)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.content().string("Problem occured"));
	}

	@Test
	public void getProductSuccess() throws Exception {

		Integer pId = 1;
		ProductEntity entity = new ProductEntity(pId, "CPU", 5000.00);

		when(proService.getProductById(pId)).thenReturn(entity);

		mockMvc.perform(MockMvcRequestBuilders.get("/getProduct/{pId}", pId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getProductFailure() throws Exception {

		Integer pId = 1;
		ProductEntity entity = new ProductEntity(pId, "CPU", 5000.00);

		when(proService.getProductById(pId)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/getProduct/{pId}", pId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	public void getAllProductSuccess() throws Exception {

		ProductEntity product1 = new ProductEntity(1, "CPU", 5000.00);

		ProductEntity product2 = new ProductEntity(2, "CPU", 5000.00);

		List<ProductEntity> list = new ArrayList<>();
		list.add(product1);
		list.add(product2);

		when(proService.getProducts()).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/products")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(list.size()));
	}

	@Test
	public void getAllProductFailure() throws Exception {


		when(proService.getProducts()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/products"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	public void deleteSuccess() throws Exception {

		Integer pId = 1;
		when(proService.deleteById(1)).thenReturn("product deleted");

		mockMvc.perform(MockMvcRequestBuilders.get("/delete/{pId}", pId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("product deleted"));

	}

	@Test
	public void deleteFailure() throws Exception {

		Integer pId = 1;
		when(proService.deleteById(1)).thenReturn("Record not found");

		mockMvc.perform(MockMvcRequestBuilders.get("/delete/{pId}", pId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());

	}

	@Test
	public void updateSuccess() throws Exception {

		ProductEntity newEntity = new ProductEntity(1, "CPU", 5000.00);

		when(proService.updateProductById(any(ProductEntity.class))).thenReturn("Product Updated Successfully");
		// Perform the PUT request
		mockMvc.perform(MockMvcRequestBuilders.put("/update/{pId}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newEntity))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Product Updated Successfully"));
	}

	@Test
	public void updateFailure() throws Exception {

		ProductEntity newEntity = new ProductEntity(1, "CPU", 5000.00);

		when(proService.updateProductById(any(ProductEntity.class))).thenReturn("Product not Updated");
		// Perform the PUT request
		mockMvc.perform(MockMvcRequestBuilders.put("/update/{pId}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newEntity)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}