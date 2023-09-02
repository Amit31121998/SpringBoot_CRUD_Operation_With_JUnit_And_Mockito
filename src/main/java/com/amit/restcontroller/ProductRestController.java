package com.amit.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amit.entity.ProductEntity;
import com.amit.service.ProductService;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService pService;

	@PostMapping("/save")
	public ResponseEntity<String> saveProduct(@RequestBody ProductEntity entity) {

		String msg = pService.saveProduct(entity);
		if ("Saved successfully".equals(msg)) {
			return new ResponseEntity<>(msg, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/products")
	public ResponseEntity<List<ProductEntity>> getAllProduct() {

		List<ProductEntity> products = pService.getProducts();
		if (products.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(products, HttpStatus.OK);
		}
	}

	@GetMapping("/getProduct/{pId}")
	public ResponseEntity<ProductEntity> getById(@PathVariable Integer pId) {

		ProductEntity product = pService.getProductById(pId);
		if (product != null) {
			return new ResponseEntity<>(product, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update/{pId}")
	public ResponseEntity<String> updateProduct(@RequestBody ProductEntity data) {
		String msg = pService.updateProductById(data);
		if ("Product Updated Successfully".equals(msg)) {
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/delete/{pId}")
	public ResponseEntity<String> deleteProduct(@PathVariable("pId") Integer pId) {

		String msg = pService.deleteById(pId);
		if ("product deleted".equals(msg)) {
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
