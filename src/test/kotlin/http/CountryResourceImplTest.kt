package http

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.generis.base.domain.CODE_SUCCESS
import org.generis.business.country.boundary.http.CountryResourceImpl
import org.generis.business.country.dto.CreateCountryDto
import org.generis.business.country.dto.UpdateCountryExchangeRate
import org.generis.business.country.repo.Country
import org.generis.business.country.service.CountryService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.jemos.podam.api.PodamFactoryImpl


internal class CountryResourceImplTest {

    @MockK
    private lateinit var service: CountryService

    @InjectMockKs
    private lateinit var underTest: CountryResourceImpl

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
    fun createCountry() {
        //GIVEN
        val request = factory.manufacturePojoWithFullData(CreateCountryDto::class.java)
        val createdCountry = factory.manufacturePojo(Country::class.java)
        every { service.createCountry(any()) } returns createdCountry

        // WHEN
        val expected = underTest.createCountry(request)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertNotNull(expected.data)
        assertEquals(createdCountry, expected.data)

        verify { service.createCountry(request) }
    }

    @Test
    fun `test create when create country fails`() {
        // GIVEN
        val country = factory.manufacturePojoWithFullData(CreateCountryDto::class.java)
        every { service.createCountry(any()) } returns null

        // WHEN
        val expected = underTest.createCountry(country)

        // THEN
        assertNull(expected.data)
        assertFalse(expected.equals(false))
        verify { service.createCountry(country) }
    }

    @Test
    fun getCountry() {
        // GIVEN
        val countryId = "123"
        val country = factory.manufacturePojo(Country::class.java)
        every { service.getCountry(countryId) } returns country

        // WHEN
        val expected = underTest.getCountry(countryId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getCountry(countryId) }
    }

    @Test
    fun `get country when id does not exist`() {
        // GIVEN
        val countryId = "123"
        every { service.getCountry(countryId) } returns null

        // WHEN
        val expected = underTest.getCountry(countryId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getCountry(countryId) }
    }

    @Test
    fun getAllCountries() {
        // GIVEN
        val countries = factory.manufacturePojoWithFullData(Country::class.java)
        every { service.getAllCountries() } returns listOf(countries)

        // WHEN
        val expected = underTest.getAllCountries()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllCountries() }
    }

    @Test
    fun `cannot get countries when list is empty`() {
        // GIVEN
        every { service.getAllCountries() } returns emptyList()

        // WHEN
        val response = underTest.getAllCountries()

        // THEN
        assertEquals(CODE_SUCCESS, response.code)
        assertEquals(emptyList<Country>(), response.data)
        verify { service.getAllCountries() }
    }

    @Test
    fun updateCountry() {
        // GIVEN
        val countryId = "123"
        val updateExchangeRate = factory.manufacturePojo(UpdateCountryExchangeRate::class.java)
        val updatedCurrency = factory.manufacturePojo(Country::class.java)
        every { service.updateCountry(countryId, updateExchangeRate) } returns updatedCurrency

        // WHEN
        val expected = underTest.updateCountry(countryId, updateExchangeRate)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.updateCountry(countryId, updateExchangeRate) }
    }

    @Test
    fun `cannot update country because id is not found`() {
        // GIVEN
        val countryId = "123"
        val updateCountryDto = factory.manufacturePojo(UpdateCountryExchangeRate::class.java)
        every { service.updateCountry(countryId, updateCountryDto) } returns null

        // WHEN
        val expected = underTest.updateCountry(countryId, updateCountryDto)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(null, expected.data)
        verify { service.updateCountry(countryId, updateCountryDto) }
    }
}