package org.generis.service.Impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.dto.CreateCustomerDto
import org.generis.dto.UpdateCustomerDto
import org.generis.entity.Currency
import org.generis.entity.Customer
import org.generis.entity.Product
import org.generis.exception.ServiceException
import org.generis.service.CustomerService
import org.modelmapper.ModelMapper


@Singleton
@Transactional
class CustomerServiceImpl: CustomerService {

    @Inject
    var entityManager: EntityManager? = null

    private val modelMapper = ModelMapper()

    override fun getCustomer(id: String): Customer? {
        return entityManager?.find(Customer::class.java, id) ?:
        throw ServiceException(-1, "No customer found with id $id")
    }

    override fun getCustomerByName(customerName: String): Customer? {
        val query: TypedQuery<Customer> = entityManager!!.createQuery(
            "SELECT c FROM Customer c WHERE c.name = :name",
            Customer::class.java
        )
        query.setParameter("name", customerName)
        return query.singleResult

    }

    override fun getAllCustomers(): List<Customer> {
        val query = entityManager?.createQuery("SELECT c FROM Customer c", Customer::class.java)
        return query?.resultList ?: throw ServiceException(-1, "No customers found")

    }

    override fun createCustomer(createCustomerDto: CreateCustomerDto): Customer {
        val customer = modelMapper.map(createCustomerDto, Customer::class.java)

        val currency = entityManager!!.find(Currency::class.java, createCustomerDto.currency)
            ?:  throw ServiceException(-1, "No currency found")

        customer.currency =  currency

        customer.persist()
        return customer
    }

    override fun updateCustomer(id: String?, updateCustomerDto: UpdateCustomerDto): Customer {
        val customer = entityManager!!.find(Customer::class.java, id)
            ?:  throw ServiceException(-1, "No customer found with id $id")

        val currency = entityManager!!.find(Currency::class.java, id)
            ?:  throw ServiceException(-1, "No currency found with id $id")

        updateCustomerDto.name?.let { customer.name = it }
        updateCustomerDto.email?.let { customer.email = it }
        updateCustomerDto.phoneNumber?.let { customer.phoneNumber = it }
        updateCustomerDto.city?.let { customer.city = it }
        updateCustomerDto.country?.let { customer.country = it }
        updateCustomerDto.taxNumber?.let { customer.taxNumber = it }
        updateCustomerDto.currency?.let { currency.currencyName = it }

        return customer
    }

    override fun deleteCustomerById(id: String) {
        val customer = entityManager?.find(Customer::class.java, id)
        customer?.let {
            entityManager?.remove(it)
        }
    }
}