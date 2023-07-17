package org.generis.business.product.service

import org.generis.business.product.dto.CreateProductDto
import org.generis.business.product.dto.UpdateProductDto
import org.generis.business.product.repo.Product


interface ProductService {
    fun getProduct(id: String): Product?
    fun getByProductName(productName: String): Product?
    fun getProductByType(): List<Product>
    fun getAllProducts(): List<Product>
    fun createProduct(createProductDto: CreateProductDto): Product?
    fun updateProduct(id: String?, updateProductDto: UpdateProductDto): Product?
    fun deleteProductById(id: String)
}