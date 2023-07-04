package http

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.generis.base.domain.CODE_SUCCESS
import org.generis.business.invoice.boundary.http.InvoiceResourceImpl
import org.generis.business.invoice.dto.CreateInvoiceDto
import org.generis.business.invoice.dto.UpdateInvoiceStatusDto
import org.generis.business.invoice.repo.Invoice
import org.generis.business.invoice.service.InvoiceService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.jemos.podam.api.PodamFactoryImpl

internal class InvoiceResourceImplTest {

    @MockK
    private lateinit var service: InvoiceService

    @InjectMockKs
    private lateinit var underTest: InvoiceResourceImpl

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
        val request = factory.manufacturePojoWithFullData(CreateInvoiceDto::class.java)
        val createInvoice = factory.manufacturePojo(Invoice::class.java)
        every { service.createInvoice(any()) } returns createInvoice

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertNotNull(expected.data)
        assertEquals(createInvoice, expected.data)

        verify { service.createInvoice(request) }
    }

    @Test
    fun `test when create invoice fails`() {
        // GIVEN
        val request = factory.manufacturePojoWithFullData(CreateInvoiceDto::class.java)
        every { service.createInvoice(any()) } returns null

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertNull(expected.data)
        assertFalse(expected.equals(false))
        verify { service.createInvoice(request) }
    }


    @Test
    fun getInvoiceById() {
        // GIVEN
        val invoiceId = "123"
        val invoice = factory.manufacturePojo(Invoice::class.java)
        every { service.getInvoice(invoiceId) } returns invoice

        // WHEN
        val expected = underTest.getInvoiceById(invoiceId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getInvoice(invoiceId) }
    }

    @Test
    fun `get invoice when id does not exist`() {
        // GIVEN
        val invoiceId = "123"
        every { service.getInvoice(invoiceId) } returns null

        // WHEN
        val expected = underTest.getInvoiceById(invoiceId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getInvoice(invoiceId) }
    }
    @Test
    fun getAllInvoices() {
        // GIVEN
        val invoices = factory.manufacturePojoWithFullData(Invoice::class.java)
        every { service.getAllInvoices() } returns listOf(invoices)

        // WHEN
        val expected = underTest.getAllInvoices()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllInvoices() }
    }

    @Test
    fun `cannot get all customers because they do not exist`() {
        // GIVEN
        every { service.getAllInvoices() } returns emptyList()

        // WHEN
        val expected = underTest.getAllInvoices()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(emptyList<Invoice>(), expected.data)
        verify { service.getAllInvoices() }
    }

    @Test
    fun updateInvoiceStatus() {
        // GIVEN
        val updateInvoiceStatusDto = factory.manufacturePojo(UpdateInvoiceStatusDto::class.java)
        val invoice = factory.manufacturePojo(Invoice::class.java)
        every { service.updateInvoiceStatus(updateInvoiceStatusDto) } returns invoice

        // WHEN
        val response = underTest.updateInvoiceStatus(updateInvoiceStatusDto)

        // THEN
        assertEquals(CODE_SUCCESS, response.code)
        assertEquals(invoice, response.data)
        verify { service.updateInvoiceStatus(updateInvoiceStatusDto) }
    }


    @Test
    fun deleteInvoice() {
        // GIVEN
        val invoiceId = "123"
        every { service.deleteInvoice(invoiceId) } just runs

        // WHEN
        val expected = underTest.deleteInvoice(invoiceId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.deleteInvoice(invoiceId) }
    }

    @Test
    fun getInvoiceByCustomerId() {
    }
}