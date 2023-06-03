package org.generis.service

import org.generis.dto.CreateCustomerDto
import org.generis.dto.UpdateCustomerDto
import org.generis.entity.Customer

interface CustomerService {
    fun getCustomer(id: String): Customer?
    fun getCustomerByName(customerName: String): Customer?
    fun getAllCustomers(): List<Customer>
    fun createCustomer(createCustomerDto: CreateCustomerDto): Customer
    fun updateCustomer(id: String?, updateCustomerDto: UpdateCustomerDto): Customer
    fun deleteCustomerById(id: String)


}