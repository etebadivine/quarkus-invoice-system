package org.generis.business.company.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.company.dto.CreateCompanyDto
import org.generis.business.company.dto.UpdateCompanyDto
import org.generis.business.company.repo.Company
import org.generis.business.country.repo.Country
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

        val country = entityManager.find(Country::class.java, createCompanyDto.country)
            ?:  throw ServiceException(-1, "No country found")

        company.country =  country

        company.persist()
        return company
    }

    override fun updateCompany(id: String?, updateCompanyDto: UpdateCompanyDto): Company? {
        val company = entityManager.find(Company::class.java, id)
            ?:  throw ServiceException(-1, "No company found with id $id")

        val country = entityManager.find(Country::class.java, id)
            ?:  throw ServiceException(-1, "No country found with id $id")

        updateCompanyDto.name?.let { company.name = it }
        updateCompanyDto.email?.let { company.email = it }
        updateCompanyDto.phoneNumber?.let { company.phoneNumber = it }
        updateCompanyDto.address?.let { company.address = it }
        updateCompanyDto.country?.let { country.countryName = it }

        return company
    }

    override fun deleteCompanyById(id: String) {
        val company = entityManager.find(Company::class.java, id)
        if (company == null) {
            throw ServiceException(-1, "Company not found")
        } else {
            val invoices = entityManager.createQuery("SELECT i FROM Invoice i WHERE i.company = :company", Invoice::class.java)
                .setParameter("company", company)
                .resultList

            for (invoice in invoices) {
                invoice.customerId = null
                entityManager.remove(invoice)
            }

            val subscriptions = entityManager.createQuery("SELECT s FROM Subscription s WHERE s.company = :company", Subscription::class.java)
                .setParameter("customer", company)
                .resultList

            for (subscription in subscriptions) {
                subscription.customerId = null
                entityManager.merge(subscription)
            }

            entityManager.remove(company)
        }
    }


}