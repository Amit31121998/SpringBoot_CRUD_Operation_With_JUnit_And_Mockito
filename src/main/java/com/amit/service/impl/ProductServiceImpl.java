package com.amit.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amit.entity.ProductEntity;
import com.amit.repository.ProductRepo;
import com.amit.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo pRepo;
	
	Logger logger=LogManager.getLogger(ProductServiceImpl.class);

	@Override
	public String saveProduct(ProductEntity data) {

		ProductEntity save = pRepo.save(data);
		if (save != null) {
			return "Saved successfully";
		} else {
			return "Problem occured";
		}
	}

	@Override
	public ProductEntity getProductById(Integer pId) {

		Optional<ProductEntity> entity = pRepo.findById(pId);

		ProductEntity product = null;
		if (entity.isPresent()) {
			return entity.get();
		} else {
			return product;
		}
	}

	@Override
	public String deleteById(Integer pId) {

		if (pRepo.existsById(pId)) {
			pRepo.deleteById(pId);

			return "product deleted";
		}
		return "Record not found";
	}

	@Override
	public String updateProductById(ProductEntity data) {

		Optional<ProductEntity> productEntity = pRepo.findById(data.getProductId());

		if (productEntity.isPresent()) {
			ProductEntity entity = productEntity.get();
			BeanUtils.copyProperties(data, entity);
			entity.setProductId(data.getProductId());
			pRepo.save(entity);
			return "Product Updated Successfully";
		}
		return "Product not Updated";
	}
	

	@Override
	public List<ProductEntity> getProducts() {
		
		logger.info("fetching all product information");
		
		List<ProductEntity> list = pRepo.findAll();
		logger.info("we successfully getting all product information");
		if (list.isEmpty()) {
			return Collections.emptyList();
		} else {
			logger.info("no any  product information available");
			return list;
		}
	}
}