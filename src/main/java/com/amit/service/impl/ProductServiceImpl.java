package com.amit.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
		List<ProductEntity> list = pRepo.findAll();
		if (list.isEmpty()) {
			return Collections.emptyList();
		} else {
			return list;
		}
	}
}