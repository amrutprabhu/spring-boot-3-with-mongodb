package com.amrut.prabhu.repository;

import com.amrut.prabhu.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
