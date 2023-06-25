package org.generis.service

import org.generis.dto.*
import org.generis.entity.Product
import org.generis.enums.ProductState

interface ProductService {
    fun getProduct(id: String): Product?
    fun getByProductName(productName: String): Product?
    fun geProductByType(productState: ProductState):  List<Product>
    fun getAllProducts(): List<Product>
    fun createProduct(createProductDto: CreateProductDto): Product?
    fun updateProduct(id: String?, updateProductDto: UpdateProductDto): Product?
    fun deleteProductById(id: String)
}