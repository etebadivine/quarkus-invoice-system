package org.generis.business.customer.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.currency.repo.Currency
import org.generis.business.customer.dto.CreateCustomerDto
import org.generis.business.customer.dto.UpdateCustomerDto
import org.generis.business.customer.repo.Customer
import org.generis.business.invoice.repo.Invoice
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.enums.LogAction
import org.generis.business.logs.service.JwtService
import org.generis.business.logs.service.LogService
import org.generis.business.subscription.repo.Subscription
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class CustomerServiceImpl : CustomerService {

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var jwtService: JwtService

    private val modelMapper = ModelMapper()

    override fun getCustomer(id: String): Customer? {
        return entityManager.find(Customer::class.java, id) ?:
        throw ServiceException(-1, "No customer found with id $id")
    }

    override fun getCustomerByName(customerName: String): Customer? {
        val query: TypedQuery<Customer> = entityManager.createQuery(
            "SELECT c FROM Customer c WHERE c.name = :name",
            Customer::class.java
        )
        query.setParameter("name", customerName)
        return query.singleResult

    }

    override fun getAllCustomers(): List<Customer> {
        val query = entityManager.createQuery("SELECT c FROM Customer c", Customer::class.java)
        return query.resultList ?: throw ServiceException(-1, "No customers found")
    }

    override fun createCustomer(createCustomerDto: CreateCustomerDto): Customer? {
        val customer = modelMapper.map(createCustomerDto, Customer::class.java)

        val currency = entityManager.find(Currency::class.java, createCustomerDto.currency)
            ?:  throw ServiceException(-1, "No currency found")

        customer.currency =  currency

        customer.persist()

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.CREATED_CUSTOMER,
            target = customer.email!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return customer
    }

    override fun updateCustomer(id: String?, updateCustomerDto: UpdateCustomerDto): Customer? {
        val customer = entityManager.find(Customer::class.java, id)
            ?:  throw ServiceException(-1, "No customer found with id $id")

        val currency = entityManager.find(Currency::class.java, updateCustomerDto.currency)
            ?:  throw ServiceException(-1, "No currency found")

        updateCustomerDto.name?.let { customer.name = it }
        updateCustomerDto.email?.let { customer.email = it }
        updateCustomerDto.phoneNumber?.let { customer.phoneNumber = it }
        updateCustomerDto.city?.let { customer.city = it }
        updateCustomerDto.country?.let { customer.country = it }
        updateCustomerDto.currency?.let { customer.currency = currency }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.UPDATED_CUSTOMER,
            target = customer.name!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return customer
    }

    override fun deleteCustomerById(id: String) {
        val customer = entityManager.find(Customer::class.java, id)
        if (customer == null) {
            throw ServiceException(-1, "Customer not found")
        } else {
            val invoices = entityManager.createQuery("SELECT i FROM Invoice i WHERE i.customerId = :customer", Invoice::class.java)
                .setParameter("customer", customer)
                .resultList

            for (invoice in invoices) {
                invoice.customerId = null
                entityManager.remove(invoice)
            }

            val subscriptions = entityManager.createQuery("SELECT s FROM Subscription s WHERE s.customerId = :customer", Subscription::class.java)
                .setParameter("customer", customer)
                .resultList

            for (subscription in subscriptions) {
                subscription.customerId = null
                entityManager.merge(subscription)
            }

            entityManager.remove(customer)

            val user =  jwtService.getUserInfo()

            val createLog = CreateLogDto(
                action = LogAction.DELETED_CUSTOMER,
                target = customer.name!!,
                userId = user.id
            )
            logService.createLog(createLog)
        }
    }
}