package org.generis.service.Impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.dto.*
import org.generis.entity.Product
import org.generis.enums.ProductState
import org.generis.exception.ServiceException
import org.generis.service.ProductService
import org.modelmapper.ModelMapper


@Singleton
@Transactional
class ProductServiceImpl: ProductService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

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
            "SELECT p FROM Product p WHERE p.isRecurring = :isRecurring",
            Product::class.java
        )
        query.setParameter("isRecurring", productState)
        return query.resultList
    }

    override fun getAllProducts(): List<Product> {
        val query = entityManager.createQuery("SELECT p FROM Product p", Product::class.java)
        return query.resultList ?: throw ServiceException(-1, "No products found")
    }

    override fun createProduct(createProductDto: CreateProductDto): Product {
        val product = modelMapper.map(createProductDto, Product::class.java)
        product.persist()
        return product
    }

    override fun updateProduct(id: String?, updateProductDto: UpdateProductDto): Product {
        val product = entityManager.find(Product::class.java, id)
            ?:  throw ServiceException(-1, "No product found with id $id")

        updateProductDto.productName?.let { product.productName = it }
        updateProductDto.unitPrice?.let { product.unitPrice = it }
        updateProductDto.description?.let { product.description = it }
        updateProductDto.isRecurring?.let { product.isRecurring = it }

        return product
    }

    override fun deleteProductById(id: String){
        val product = entityManager.find(Product::class.java, id)
        product?.let {
            entityManager.remove(it)
        }
    }
}