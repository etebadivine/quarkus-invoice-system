package org.generis.business.customer.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.company.dto.CreateCompanyDto
import org.generis.business.customer.dto.CreateCustomerDto
import org.generis.business.company.dto.UpdateCompanyDto
import org.generis.business.company.repo.Company

interface CompanyResource {

    fun createCompany(@Valid createCompanyDto: CreateCompanyDto): ApiResponse<Company?>

    fun getCompany(id: String): ApiResponse<Company?>

    fun updateCompany(id: String?, updateCompanyDto: UpdateCompanyDto): ApiResponse<Company?>

    fun getAllCompanies(): ApiResponse<List<Company>>

    fun deleteCompanyById(id: String): ApiResponse<Boolean>
}