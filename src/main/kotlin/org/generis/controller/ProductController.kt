package org.generis.controller

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.*
import org.generis.entity.Product
import org.generis.enums.ProductState
import org.generis.service.ProductService
import org.generis.util.*
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory



@Path("products")
//@RolesAllowed("ROLE_ADMIN")
@Produces(MediaType.APPLICATION_JSON)
class ProductController {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var productService: ProductService


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(@Valid createProductDto: CreateProductDto): ApiResponse<Product> {
        logger.info("http request: create")

        val product = productService.createProduct(createProductDto)

        val apiResponse = wrapSuccessInResponse(product)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getProduct(@PathParam("id") id: String): ApiResponse<Product?> {
        val product = productService?.getProduct(id)
        return wrapSuccessInResponse(product)
    }

    @GET
    @Path("/type/{productType}")
    fun geProductByType(@PathParam("productType") productType: ProductState): ApiResponse<List<Product>> {
        val products = productService.geProductByType(productType)
        return wrapSuccessInResponse(products)
    }

    @PUT
    @Path("/{id}")
    fun updateProduct(
        @PathParam("id") id: String, updateProductDto: UpdateProductDto
    ): ApiResponse<Product> {
        val updatedProduct = productService.updateProduct(id, updateProductDto)
        return wrapSuccessInResponse(updatedProduct)
    }

    @GET
    fun getAllProducts(): ApiResponse<List<Product>> {
        val products = productService.getAllProducts()
        return wrapSuccessInResponse(products)
    }

    private fun mapProductToDto(product: Product): ProductDto {
        return ProductDto(
            id = product.id,
            unitPrice = product.unitPrice,
            productName = product.productName,
            description = product.description,
            isRecurring = product.isRecurring,
            createdDate = product.createdDate
        )
    }

    @DELETE
    @Path("/{id}")
    fun deleteProductById(@PathParam("id") id: String): ApiResponse<Boolean> {
        productService.deleteProductById(id)
        return wrapSuccessInResponse(true)
    }
}