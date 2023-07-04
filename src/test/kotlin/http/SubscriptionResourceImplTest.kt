package http

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.generis.base.domain.CODE_SUCCESS
import org.generis.business.subscription.boundary.http.SubscriptionResourceImpl
import org.generis.business.subscription.dto.CreateSubscriptionDto
import org.generis.business.subscription.repo.Subscription
import org.generis.business.subscription.service.SubscriptionService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.jemos.podam.api.PodamFactoryImpl

internal class SubscriptionResourceImplTest {

    @MockK
    private lateinit var service: SubscriptionService

    @InjectMockKs
    private lateinit var underTest: SubscriptionResourceImpl

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
    fun createSubscription() {
        //GIVEN
        val request = factory.manufacturePojoWithFullData(CreateSubscriptionDto::class.java)
        val createSubscription = factory.manufacturePojo(Subscription::class.java)
        every { service.createSubscription(any()) } returns createSubscription

        // WHEN
        val expected = underTest.createSubscription(request)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertNotNull(expected.data)
        assertEquals(createSubscription, expected.data)

        verify { service.createSubscription(request) }
    }

    @Test
    fun `test when create subscription fails`() {
        // GIVEN
        val request = factory.manufacturePojoWithFullData(CreateSubscriptionDto::class.java)
        every { service.createSubscription(any()) } returns null

        // WHEN
        val expected = underTest.createSubscription(request)

        // THEN
        assertNull(expected.data)
        assertFalse(expected.equals(false))
        verify { service.createSubscription(request) }
    }

    @Test
    fun getSubscription() {
        // GIVEN
        val subscriptionId = "123"
        val subscription = factory.manufacturePojo(Subscription::class.java)
        every { service.getSubscription(subscriptionId) } returns subscription

        // WHEN
        val expected = underTest.getSubscription(subscriptionId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getSubscription(subscriptionId) }
    }

    @Test
    fun `get subscription when id does not exist`() {
        // GIVEN
        val subscriptionId = "123"
        every { service.getSubscription(subscriptionId) } returns null

        // WHEN
        val expected = underTest.getSubscription(subscriptionId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getSubscription(subscriptionId) }
    }

    @Test
    fun getAllSubscriptions() {
        // GIVEN
        val subscriptions = factory.manufacturePojoWithFullData(Subscription::class.java)
        every { service.getAllSubscriptions() } returns listOf(subscriptions)

        // WHEN
        val expected = underTest.getAllSubscriptions()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllSubscriptions() }
    }

    @Test
    fun `cannot get all subscriptions because they do not exist`() {
        // GIVEN
        every { service.getAllSubscriptions() } returns emptyList()

        // WHEN
        val expected = underTest.getAllSubscriptions()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals(emptyList<Subscription>(), expected.data)
        verify { service.getAllSubscriptions() }
    }


    @Test
    fun getAllActiveSubscriptions() {
        // GIVEN
        val subscriptions = factory.manufacturePojoWithFullData(Subscription::class.java)
        every { service.getAllActiveSubscriptions() } returns listOf(subscriptions)

        // WHEN
        val expected = underTest.getAllActiveSubscriptions()

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        verify { service.getAllActiveSubscriptions() }
    }

    @Test
    fun cancelSubscription() {
        // GIVEN
        val subscriptionId = "123"
        every { service.cancelSubscription(subscriptionId) } just runs

        // WHEN
        val expected = underTest.cancelSubscription(subscriptionId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals("Subscription canceled successfully.", expected.data)
        verify { service.cancelSubscription(subscriptionId) }
    }

    @Test
    fun reactivateSubscription() {
        // GIVEN
        val subscriptionId = "565"
        every { service.reactivateSubscription(subscriptionId) } just runs

        // WHEN
        val expected = underTest.reactivateSubscription(subscriptionId)

        // THEN
        assertEquals(CODE_SUCCESS, expected.code)
        assertEquals("Subscription reactivated successfully.", expected.data)
        verify { service.reactivateSubscription(subscriptionId) }
    }
}