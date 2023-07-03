package org.generis.business.product.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.product.dto.CreateProductDto
import org.generis.business.product.dto.UpdateProductDto
import org.generis.business.product.enums.ProductState
import org.generis.business.product.repo.Product

interface ProductResource {

    fun create(@Valid createProductDto: CreateProductDto): ApiResponse<Product?>

    fun getProduct(id: String): ApiResponse<Product?>

    fun geProductByType(productType: ProductState): ApiResponse<List<Product>>

    fun updateProduct(id: String, updateProductDto: UpdateProductDto): ApiResponse<Product?>

    fun getAllProducts(): ApiResponse<List<Product>>

    fun deleteProductById(id: String): ApiResponse<Boolean>
}