package org.generis.business.company.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.company.dto.CreateCompanyDto
import org.generis.business.currency.repo.Currency
import org.generis.business.company.dto.UpdateCompanyDto
import org.generis.business.company.repo.Company
import org.generis.business.invoice.repo.Invoice
import org.generis.business.subscription.repo.Subscription
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class CompanyServiceImpl : CompanyService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

    override fun getCompany(id: String): Company? {
        return entityManager.find(Company::class.java, id) ?:
        throw ServiceException(-1, "No company found with id $id")
    }


    override fun getAllCompanies(): List<Company> {
        val query = entityManager.createQuery("SELECT c FROM Company c", Company::class.java)
        return query?.resultList ?: throw ServiceException(-1, "No companies found")
    }

    override fun createCompany(createCompanyDto: CreateCompanyDto): Company? {
        val company = modelMapper.map(createCompanyDto, Company::class.java)

        val currency = entityManager.find(Currency::class.java, createCompanyDto.currency)
            ?:  throw ServiceException(-1, "No currency found")

        company.currency =  currency

        company.persist()
        return company
    }

    override fun updateCompany(id: String?, updateCompanyDto: UpdateCompanyDto): Company? {
        val company = entityManager.find(Company::class.java, id)
            ?:  throw ServiceException(-1, "No company found with id $id")

        val currency = entityManager!!.find(Currency::class.java, id)
            ?:  throw ServiceException(-1, "No currency found with id $id")

        updateCompanyDto.name?.let { company.name = it }
        updateCompanyDto.email?.let { company.email = it }
        updateCompanyDto.phoneNumber?.let { company.phoneNumber = it }
        updateCompanyDto.currency?.let { currency.currencyName = it }

        return company
    }

    override fun deleteCompanyById(id: String) {
        val customer = entityManager.find(Company::class.java, id)
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
        }
    }


}