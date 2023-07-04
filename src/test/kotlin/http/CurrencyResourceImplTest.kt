package http

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.generis.base.domain.CODE_SUCCESS
import org.generis.business.currency.boundary.http.CurrencyResourceImpl
import org.generis.business.currency.dto.CreateCurrencyDto
import org.generis.business.currency.dto.UpdateCurrencyDto
import org.generis.business.currency.repo.Currency
import org.generis.business.currency.service.CurrencyService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.jemos.podam.api.PodamFactoryImpl

internal class CurrencyResourceImplTest {

    @MockK
    private lateinit var service: CurrencyService

    @InjectMockKs
    private lateinit var underTest: CurrencyResourceImpl

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
        val request = factory.manufacturePojoWithFullData(CreateCurrencyDto::class.java)
        val createdCurrency = factory.manufacturePojo(Currency::class.java)
        every { service.createCurrency(any()) } returns createdCurrency

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertNotNull(expected.data)
        assertEquals(createdCurrency, expected.data)

        verify { service.createCurrency(request) }
    }

    @Test
    fun `test create when create customer fails`() {
        // GIVEN
        val request = factory.manufacturePojoWithFullData(CreateCurrencyDto::class.java)
        every { service.createCurrency(any()) } returns null

        // WHEN
        val expected = underTest.create(request)

        // THEN
        assertNull(expected.data)
        assertFalse(expected.equals(false))
        verify { service.createCurrency(request) }

    }

    @Test
    fun getCurrency() {
        // GIVEN
        val currencyId = "123"
        val currency = factory.manufacturePojo(Currency::class.java)
        every { service.getCurrency(currencyId) } returns currency

        // WHEN
        val expected = underTest.getCurrency(currencyId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getCurrency(currencyId) }
    }

    @Test
    fun `get currency when id does not exist`() {
        // GIVEN
        val currencyId = "123"
        every { service.getCurrency(currencyId) } returns null

        // WHEN
        val expected = underTest.getCurrency(currencyId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getCurrency(currencyId) }
    }

    @Test
    fun getAllCurrencies() {
        // GIVEN
        val currencies = factory.manufacturePojoWithFullData(Currency::class.java)
        every { service.getAllCurrencies() } returns listOf(currencies)

        // WHEN
        val expected = underTest.getAllCurrencies()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllCurrencies() }
    }

    @Test
    fun `cannot get currencies when list is empty`() {
        // GIVEN
        every { service.getAllCurrencies() } returns emptyList()

        // WHEN
        val response = underTest.getAllCurrencies()

        // THEN
        assertEquals(CODE_SUCCESS, response.code)
        assertEquals(emptyList<Currency>(), response.data)
        verify { service.getAllCurrencies() }
    }

    @Test
    fun updateCurrency() {
        // GIVEN
        val currencyId = "123"
        val updateExchangeRate = factory.manufacturePojo(UpdateCurrencyDto::class.java)
        val updatedCurrency = factory.manufacturePojo(Currency::class.java)
        every { service.updateExchangeRate(currencyId, updateExchangeRate) } returns updatedCurrency

        // WHEN
        val expected = underTest.updateCurrency(currencyId, updateExchangeRate)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.updateExchangeRate(currencyId, updateExchangeRate) }
    }

    @Test
    fun `cannot update currency when id is not found`() {
        // GIVEN
        val currencyId = "123"
        val updateCurrencyDto = factory.manufacturePojo(UpdateCurrencyDto::class.java)
        every { service.updateExchangeRate(currencyId, updateCurrencyDto) } returns null

        // WHEN
        val expected = underTest.updateCurrency(currencyId, updateCurrencyDto)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(null, expected.data)
        verify { service.updateExchangeRate(currencyId, updateCurrencyDto) }
    }
}