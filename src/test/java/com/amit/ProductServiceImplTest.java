package com.amit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.amit.entity.ProductEntity;
import com.amit.repository.ProductRepo;
import com.amit.service.impl.ProductServiceImpl;

@SpringBootTest
public class ProductServiceImplTest {

	@InjectMocks
	private ProductServiceImpl productServiceImpl;
	@Mock
	private ProductRepo productRepository;

	// save product object

	@Test
	public void testSaveProductSuccess() {
		
		// Create a sample ProductEntity for the request body
		ProductEntity sampleProduct = new ProductEntity(1, "Laptop", 60000.0);

		when(productRepository.save(any(ProductEntity.class))).thenReturn(sampleProduct);

		// Perform the saveProduct operation
		String result = productServiceImpl.saveProduct(sampleProduct);

		assertEquals("Saved successfully", result);

		// Verify that the repository's save method was called
		verify(productRepository).save(sampleProduct);
	}
	
	@Test
	public void testSaveProductFailure() {
		ProductEntity sampleProduct = new ProductEntity(1, "Laptop", 60000.0);
		when(productRepository.save(any(ProductEntity.class))).thenReturn(null);

		// Perform the saveProduct operation
		String result = productServiceImpl.saveProduct(sampleProduct);

		assertEquals("Problem occured", result);

		
		// Verify that the repository's save method was called
		verify(productRepository, times(1)).save(sampleProduct);
	}

	@Test
	public void testGetExistingProduct() {
		Integer pId = 1;

		ProductEntity product = new ProductEntity(1, "Laptop", 60000.0);

		when(productRepository.findById(pId)).thenReturn(Optional.of(product));

		ProductEntity result = productServiceImpl.getProductById(pId);
		
		verify(productRepository, times(1)).findById(pId);

		assertNotNull(result);
		assertEquals(product.getProductId(), result.getProductId());
		assertEquals(product.getPriductName(), result.getPriductName());
		
	}

	@Test
	public void testGetNonExistingProduct() {
		Integer pId = 1;

		when(productRepository.findById(pId)).thenReturn(Optional.empty());

		ProductEntity result = productServiceImpl.getProductById(pId);

		verify(productRepository, times(1)).findById(pId);

		assertNull(result);
	}

//  if  product is not exist

	@Test
	public void testDeleteNonExistingProduct() {
		int productIdToDelete = 1;

		// Mock the behavior of the repository
		when(productRepository.existsById(productIdToDelete)).thenReturn(false);

		// Perform the delete operation
		String result = productServiceImpl.deleteById(productIdToDelete);

		assertEquals("Record not found", result);

		// Verify that the repository's deleteById method was not called
		verify(productRepository, never()).deleteById(productIdToDelete);
	}

	// if product is exist

	@Test
	public void testDelete() {
		int productIdToDelete = 1;

		when(productRepository.existsById(productIdToDelete)).thenReturn(true);

		String result = productServiceImpl.deleteById(productIdToDelete);

		assertEquals("product deleted", result);

		verify(productRepository, times(1)).deleteById(productIdToDelete);
	}

	// update product if product object is available

	@Test
	public void testUpdateExistingProduct() {
		Integer pId = 1;

		ProductEntity sampleProduct = new ProductEntity(pId, "Mobile", 60000.0);

		ProductEntity existingProduct = new ProductEntity();
		existingProduct.setProductId(pId);

		when(productRepository.findById(pId)).thenReturn(Optional.of(existingProduct));
		when(productRepository.save(any(ProductEntity.class))).thenReturn(sampleProduct);

		// Perform the update operation
		String result = productServiceImpl.updateProductById(sampleProduct);

		assertEquals("Product Updated Successfully", result);

		// Verify that the repository's findById and save methods were called
		verify(productRepository, times(1)).findById(pId);
		verify(productRepository, times(1)).save(existingProduct);
	}

	@Test
	public void testUpdateNonExistingProduct() {
		Integer pId = 1;

		// Create a sample ProductEntity for the request body
		ProductEntity sampleProduct = new ProductEntity(pId, "Mobile", 5500.00);

		when(productRepository.findById(pId)).thenReturn(Optional.empty());

		// Perform the update operation
		String result = productServiceImpl.updateProductById(sampleProduct);

		assertEquals("Product not Updated", result);

		// Verify that the repository's findById method was called
		verify(productRepository, times(1)).findById(pId);
		verify(productRepository, never()).save(any(ProductEntity.class));
	}

	// if list of product is not empty

	@Test
	public void testGetProductsNotEmptyList() {
		// Create a list of sample products
		List<ProductEntity> Products = new ArrayList<>();
		Products.add(new ProductEntity(1, "Product 1", 6000.0));
		Products.add(new ProductEntity(2, "Product 2", 7000.0));

		when(productRepository.findAll()).thenReturn(Products);

		List<ProductEntity> result = productServiceImpl.getProducts();

		verify(productRepository, times(1)).findAll();

		assertEquals(Products.size(), result.size());
		assertEquals(Products.get(0).getProductId(), result.get(0).getProductId());
		assertEquals(Products.get(0).getPriductName(), result.get(0).getPriductName());
		assertEquals(Products.get(1).getProductId(), result.get(1).getProductId());
		assertEquals(Products.get(1).getPriductName(), result.get(1).getPriductName());
	}

	// if list of product is empty

	    @Test
	    public void testGetProductsEmptyList() {
	        when(productRepository.findAll()).thenReturn(Collections.emptyList());

	        List<ProductEntity> result = productServiceImpl.getProducts();
	        verify(productRepository, times(1)).findAll();

	        assertTrue(result.isEmpty());
	    }

	
}

