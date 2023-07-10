package org.generis.business.company.service

import org.generis.business.company.dto.CreateCompanyDto
import org.generis.business.company.dto.UpdateCompanyDto
import org.generis.business.company.repo.Company


interface CompanyService {
    fun getCompany(id: String): Company?
    fun getAllCompanies(): List<Company>
    fun createCompany(createCompanyDto: CreateCompanyDto): Company?
    fun updateCompany(id: String?, updateCompanyDto: UpdateCompanyDto): Company?
    fun deleteCompanyById(id: String)


}