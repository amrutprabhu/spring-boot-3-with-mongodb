package com.amrut.prabhu.web;

import com.amrut.prabhu.exception.ProductNotFound;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(ProductNotFound.class)
    public ProblemDetail handleProductNotFound(ProductNotFound productNotFound) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404),
                productNotFound.getMessage());
        problemDetail.setType(URI.create("https://github.com/amrutprabhu/spring-boot-3-with-mongodb"));
        return problemDetail;
    }
}
