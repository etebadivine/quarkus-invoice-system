package org.generis.business.company.boundary.http

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.company.dto.CreateCompanyDto
import org.generis.business.company.dto.UpdateCompanyDto
import org.generis.business.company.repo.Company
import org.generis.business.company.service.CompanyService
import org.generis.business.customer.boundary.CompanyResource
import org.slf4j.LoggerFactory


@Path("companies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CompanyResourceImpl: CompanyResource {

    private val logger = LoggerFactory.getLogger(CompanyResourceImpl::class.java)

    @Inject
    lateinit var companyService: CompanyService
    @POST
    override fun createCompany(@Valid createCompanyDto: CreateCompanyDto): ApiResponse<Company?> {
        logger.info("http request: create")

        val company = companyService.createCompany(createCompanyDto)

        val apiResponse = wrapSuccessInResponse(company)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getCompany(@PathParam("id") id: String): ApiResponse<Company?> {
        val company = companyService?.getCompany(id)
        return wrapSuccessInResponse(company)
    }

    @PUT
    @Path("/{id}")
    override fun updateCompany(@PathParam("id") id: String?, updateCompanyDto: UpdateCompanyDto): ApiResponse<Company?> {
        val updatedCompany = companyService.updateCompany(id, updateCompanyDto)
        return wrapSuccessInResponse(updatedCompany)
    }

    @GET
    override fun getAllCompanies(): ApiResponse<List<Company>> {
        val companies = companyService.getAllCompanies()
        return wrapSuccessInResponse(companies)
    }

    @DELETE
    @Path("/{id}")
    override fun deleteCompanyById(@PathParam("id") id: String): ApiResponse<Boolean> {
        companyService.deleteCompanyById(id)
        return wrapSuccessInResponse(true)
    }
}