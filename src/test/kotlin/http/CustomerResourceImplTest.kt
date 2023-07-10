package http

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.generis.base.domain.CODE_SUCCESS
import org.generis.base.exception.ServiceException
import org.generis.business.company.boundary.http.CompanyResourceImpl
import org.generis.business.customer.dto.CreateCustomerDto
import org.generis.business.company.dto.UpdateCompanyDto
import org.generis.business.company.repo.Company
import org.generis.business.company.service.CompanyService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import uk.co.jemos.podam.api.PodamFactoryImpl

internal class CustomerResourceImplTest {

    @MockK
    private lateinit var service: CompanyService

    @InjectMockKs
    private lateinit var underTest: CompanyResourceImpl

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
        val request = factory.manufacturePojoWithFullData(CreateCustomerDto::class.java)
        val createdCustomer = factory.manufacturePojo(Company::class.java)
        every { service.createCustomer(any()) } returns createdCustomer

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertNotNull(expected.data)
        assertEquals(createdCustomer, expected.data)

        verify { service.createCustomer(request) }
    }

    @Test
    fun `test create when create customer fails`() {
        // GIVEN
        val request = factory.manufacturePojoWithFullData(CreateCustomerDto::class.java)
        every { service.createCustomer(any()) } returns null

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertNull(expected.data)
        assertFalse(expected.equals(false))
        verify { service.createCustomer(request) }
    }


    @Test
    fun getCustomer() {
        // GIVEN
        val customerId = "123"
        val customer = factory.manufacturePojo(Company::class.java)
        every { service.getCustomer(customerId) } returns customer

        // WHEN
        val expected = underTest.getCustomer(customerId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getCustomer(customerId) }
    }

    @Test
    fun `get customer when id does not exist`() {
        // GIVEN
        val customerId = "123"
        every { service.getCustomer(customerId) } returns null

        // WHEN
        val expected = underTest.getCustomer(customerId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getCustomer(customerId) }
    }

    @Test
    fun updateCustomer() {
        // GIVEN
        val customerId = "123"
        val updateCustomerDto = factory.manufacturePojo(UpdateCompanyDto::class.java)
        val updatedCustomer = factory.manufacturePojo(Company::class.java)
        every { service.updateCustomer(customerId, updateCustomerDto) } returns updatedCustomer

        // WHEN
        val expected = underTest.updateCustomer(customerId, updateCustomerDto)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.updateCustomer(customerId, updateCustomerDto) }
    }

    @Test
    fun `cannot update customer when customer is not found`() {
        // GIVEN
        val customerId = "123"
        val updateCustomerDto = factory.manufacturePojo(UpdateCompanyDto::class.java)
        every { service.updateCustomer(customerId, updateCustomerDto) } returns null

        // WHEN
        val expected = underTest.updateCustomer(customerId, updateCustomerDto)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(null, expected.data)
        verify { service.updateCustomer(customerId, updateCustomerDto) }
        }

    @Test
    fun getAllCustomers() {
        // GIVEN
        val customers = factory.manufacturePojoWithFullData(Company::class.java)
        every { service.getAllCustomers() } returns listOf(customers)

        // WHEN
        val expected = underTest.getAllCustomers()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllCustomers() }
    }

    @Test
    fun `cannot get all customers because they do not exist`() {
        // GIVEN
        every { service.getAllCustomers() } returns emptyList()

        // WHEN
        val response = underTest.getAllCustomers()

        // THEN
        assertEquals(CODE_SUCCESS, response.code)
        assertEquals(emptyList<Company>(), response.data)
        verify { service.getAllCustomers() }
    }

    @Test
    fun deleteCustomerById() {
        // GIVEN
        val customerId = "123"
        every { service.deleteCustomerById(customerId) } just runs

        // WHEN
        val expected = underTest.deleteCustomerById(customerId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.deleteCustomerById(customerId) }
    }

    @Test
    fun `cannot delete customer because it does not exist`() {
        // GIVEN
        val customerId = "456"
        every { service.deleteCustomerById(customerId) } throws ServiceException(-1, "Customer not found")

        // WHEN
        val response = assertThrows<ServiceException> {
            underTest.deleteCustomerById(customerId)
        }

        // THEN
        assertEquals(-1, response.code)
        assertEquals("Customer not found", response.message)
        verify { service.deleteCustomerById(customerId) }
    }
}