package com.amrut.prabhu.exception;

public class ProductNotFound extends RuntimeException {

    public ProductNotFound() {
        super("Product Not found");
    }
}
