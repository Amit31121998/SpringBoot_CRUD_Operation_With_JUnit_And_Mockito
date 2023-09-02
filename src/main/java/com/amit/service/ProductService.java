package com.amit.service;

import java.util.List;

import com.amit.entity.ProductEntity;

public interface ProductService {
	
	public String saveProduct(ProductEntity data);
	
	public ProductEntity getProductById(Integer pId);
	
	public String deleteById(Integer pId);
	
	public String updateProductById(ProductEntity data);
	
	public List<ProductEntity> getProducts();

}
