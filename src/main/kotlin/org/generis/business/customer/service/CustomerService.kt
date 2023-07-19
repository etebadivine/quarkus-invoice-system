package org.generis.business.customer.service

import org.generis.base.domain.ApiResponse
import org.generis.business.customer.dto.CreateCustomerDto
import org.generis.business.customer.dto.UpdateCustomerDto
import org.generis.business.customer.repo.Customer


interface CustomerService {
    fun getCustomer(id: String): Customer?
    fun getAllCustomers(): List<Customer>
    fun createCustomer(createCustomerDto: CreateCustomerDto): Customer?
    fun updateCustomer(id: String?, updateCustomerDto: UpdateCustomerDto): Customer?
    fun deleteCustomerById(id: String)


}