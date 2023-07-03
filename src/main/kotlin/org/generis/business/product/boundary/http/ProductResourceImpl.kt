package org.generis.business.product.boundary.http

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.product.boundary.ProductResource
import org.generis.business.product.dto.CreateProductDto
import org.generis.business.product.dto.UpdateProductDto
import org.generis.business.product.enums.ProductState
import org.generis.business.product.repo.Product
import org.generis.business.product.service.ProductService
import org.slf4j.LoggerFactory


//@RolesAllowed("ROLE_ADMIN")

@Path("products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ProductResourceImpl: ProductResource {

    private val logger = LoggerFactory.getLogger(ProductResourceImpl::class.java)

    @Inject
    lateinit var productService: ProductService

    @POST
    override fun create(@Valid createProductDto: CreateProductDto): ApiResponse<Product?> {
        logger.info("http request: create")

        val product = productService.createProduct(createProductDto)

        val apiResponse = wrapSuccessInResponse(product)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getProduct(@PathParam("id") id: String): ApiResponse<Product?> {
        val product = productService?.getProduct(id)
        return wrapSuccessInResponse(product)
    }

    @GET
    @Path("/type/{productType}")
    override fun geProductByType(@PathParam("productType") productType: ProductState): ApiResponse<List<Product>> {
        val products = productService.geProductByType(productType)
        return wrapSuccessInResponse(products)
    }

    @PUT
    @Path("/{id}")
    override fun updateProduct(
        @PathParam("id") id: String, updateProductDto: UpdateProductDto
    ): ApiResponse<Product?> {
        val updatedProduct = productService.updateProduct(id, updateProductDto)
        return wrapSuccessInResponse(updatedProduct)
    }

    @GET
    override fun getAllProducts(): ApiResponse<List<Product>> {
        val products = productService.getAllProducts()
        return wrapSuccessInResponse(products)
    }

    @DELETE
    @Path("/{id}")
    override fun deleteProductById(@PathParam("id") id: String): ApiResponse<Boolean> {
        productService.deleteProductById(id)
        return wrapSuccessInResponse(true)
    }
}