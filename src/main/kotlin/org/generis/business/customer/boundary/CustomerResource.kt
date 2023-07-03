package org.generis.business.customer.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.customer.dto.CreateCustomerDto
import org.generis.business.customer.dto.UpdateCustomerDto
import org.generis.business.customer.repo.Customer

interface CustomerResource {

    fun create(@Valid createCustomerDto: CreateCustomerDto): ApiResponse<Customer?>

    fun getCustomer(id: String): ApiResponse<Customer?>

    fun updateCustomer(id: String, updateCustomerDto: UpdateCustomerDto): ApiResponse<Customer?>

    fun getAllCustomers(): ApiResponse<List<Customer>>

    fun deleteCustomerById(id: String): ApiResponse<Boolean>
}