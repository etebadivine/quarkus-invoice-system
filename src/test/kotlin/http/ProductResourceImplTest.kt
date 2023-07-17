package http

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.generis.base.domain.CODE_SUCCESS
import org.generis.base.exception.ServiceException
import org.generis.business.product.boundary.http.ProductResourceImpl
import org.generis.business.product.dto.CreateProductDto
import org.generis.business.product.dto.UpdateProductDto
import org.generis.business.product.repo.Product
import org.generis.business.product.service.ProductService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.jemos.podam.api.PodamFactoryImpl

internal class ProductResourceImplTest {

    @MockK
    private lateinit var service: ProductService

    @InjectMockKs
    private lateinit var underTest: ProductResourceImpl

    private val factory : PodamFactoryImpl = PodamFactoryImpl()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun create() {
        //GIVEN
        val request = factory.manufacturePojoWithFullData(CreateProductDto::class.java)
        val createdProduct = factory.manufacturePojo(Product::class.java)
        every { service.createProduct(any()) } returns createdProduct

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertNotNull(expected.data)
        assertEquals(createdProduct, expected.data)

        verify { service.createProduct(request) }
    }

    @Test
    fun `test when create customer fails`() {
        // GIVEN
        val request = factory.manufacturePojoWithFullData(CreateProductDto::class.java)
        every { service.createProduct(any()) } returns null

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertNull(expected.data)
        assertFalse(expected.equals(false))
        verify { service.createProduct(request) }
    }

    @Test
    fun getProduct() {
        // GIVEN
        val productId = "123"
        val product = factory.manufacturePojo(Product::class.java)
        every { service.getProduct(productId) } returns product

        // WHEN
        val expected = underTest.getProduct(productId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getProduct(productId) }
    }

    @Test
    fun `get product when id does not exist`() {
        // GIVEN
        val productId = "123"
        every { service.getProduct(productId) } returns null

        // WHEN
        val expected = underTest.getProduct(productId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getProduct(productId) }
    }


    @Test
    fun getProductByType() {
        // GIVEN
        val products = factory.manufacturePojo(Product::class.java)
        every { service.getProductByType() } returns listOf(products)

        // WHEN
        val expected = underTest.getProductByType()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getProductByType() }
    }

    @Test
    fun `Get product by type when no product is found`() {
        // GIVEN
        every { service.getProductByType() } returns emptyList()

        // WHEN
        val expected = underTest.getProductByType()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(emptyList<Product>(), expected.data)
        verify { service.getProductByType() }
    }

    @Test
    fun updateProduct() {
        // GIVEN
        val productId = "123"
        val updateProductDto = factory.manufacturePojo(UpdateProductDto::class.java)
        val updatedProduct = factory.manufacturePojo(Product::class.java)
        every { service.updateProduct(productId, updateProductDto) } returns updatedProduct

        // WHEN
        val expected = underTest.updateProduct(productId, updateProductDto)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.updateProduct(productId, updateProductDto) }
    }

    @Test
    fun `cannot update product when product is not found`() {
        // GIVEN
        val productId = "123"
        val updateProductDto = factory.manufacturePojo(UpdateProductDto::class.java)
        every { service.updateProduct(productId, updateProductDto) } returns null

        // WHEN
        val expected = underTest.updateProduct(productId, updateProductDto)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(null, expected.data)
        verify { service.updateProduct(productId, updateProductDto) }

    }

    @Test
    fun getAllProducts() {
        // GIVEN
        val products = factory.manufacturePojoWithFullData(Product::class.java)
        every { service.getAllProducts() } returns listOf(products)

        // WHEN
        val expected = underTest.getAllProducts()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllProducts() }
    }

    @Test
    fun `cannot get all products because they do not exist`() {
        // GIVEN
        every { service.getAllProducts() } returns emptyList()

        // WHEN
        val expected = underTest.getAllProducts()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(emptyList<Product>(), expected.data)
        verify { service.getAllProducts() }
    }

    @Test
    fun deleteProductById() {
        // GIVEN
        val productId = "123"
        every { service.deleteProductById(productId) } just runs

        // WHEN
        val expected = underTest.deleteProductById(productId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.deleteProductById(productId) }
    }

    @Test
    fun `cannot delete product because it does not exist`() {
        // GIVEN
        val productId = "456"
        every { service.deleteProductById(productId) } throws ServiceException(-1, "Product not found")

        // WHEN
        val expected = org.junit.jupiter.api.assertThrows<ServiceException> {
            underTest.deleteProductById(productId)
        }

        // THEN
        assertEquals(-1, expected.code)
        assertEquals("Product not found", expected.message)
        verify { service.deleteProductById(productId) }
    }
}