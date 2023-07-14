package org.generis.business.product.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.enums.LogAction
import org.generis.business.logs.service.JwtService
import org.generis.business.logs.service.LogService
import org.generis.business.product.dto.CreateProductDto
import org.generis.business.product.dto.UpdateProductDto
import org.generis.business.product.enums.ProductState
import org.generis.business.product.repo.Product
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class ProductServiceImpl: ProductService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var jwtService: JwtService

    override fun getProduct(id: String): Product {
        return entityManager.find(Product::class.java, id) ?:
        throw ServiceException(-1, "No product found with id $id")
    }

    override fun getByProductName(productName: String): Product? {
        val query: TypedQuery<Product> = entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.productName = :productName",
            Product::class.java
        )
        query.setParameter("productName", productName)
        return query.singleResult
    }

    override fun geProductByType(productState: ProductState): List<Product> {
        val query: TypedQuery<Product> = entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.productState = :productState",
            Product::class.java
        )
        query.setParameter("productState", productState)
        return query.resultList
    }

    override fun getAllProducts(): List<Product> {
        val query = entityManager.createQuery("SELECT p FROM Product p", Product::class.java)
        return query.resultList ?: throw ServiceException(-1, "No products found")
    }

    override fun createProduct(createProductDto: CreateProductDto): Product? {
        val product = modelMapper.map(createProductDto, Product::class.java)
        product.persist()

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.CREATED_PRODUCT,
            target = product.productName!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return product
    }

    override fun updateProduct(id: String?, updateProductDto: UpdateProductDto): Product? {
        val product = entityManager.find(Product::class.java, id)
            ?:  throw ServiceException(-1, "No product found with id $id")

        updateProductDto.productName?.let { product.productName = it }
        updateProductDto.unitPrice?.let { product.unitPrice = it }
        updateProductDto.description?.let { product.description = it }
        updateProductDto.productState?.let { product.productState = it }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.UPDATED_PRODUCT,
            target = product.productName!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return product
    }

    override fun deleteProductById(id: String){
        val product = entityManager.find(Product::class.java, id)
        if (product == null) {
            throw ServiceException(-1, "Product not found")
        } else {
            entityManager.remove(product)
        }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.DELETED_PRODUCT,
            target = product.productName!!,
            userId = user.id
        )
        logService.createLog(createLog)
    }
}