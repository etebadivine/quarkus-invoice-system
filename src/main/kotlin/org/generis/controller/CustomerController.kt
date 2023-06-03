package org.generis.controller

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.*
import org.generis.entity.Customer
import org.generis.service.CustomerService
import org.generis.util.wrapSuccessInResponse
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
class CustomerController {

    private val logger = LoggerFactory.getLogger(CustomerController::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var customerService: CustomerService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(@Valid createCustomerDto:CreateCustomerDto): ApiResponse<CustomerDto> {
        logger.info("http request: create")

        val customer = customerService.createCustomer(createCustomerDto)

        val customerDto = modelMapper.map(customer, CustomerDto::class.java)
        val apiResponse = wrapSuccessInResponse(customerDto)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getCustomer(@PathParam("id") id: String): ApiResponse<Customer?> {
        val customer = customerService?.getCustomer(id)
        return wrapSuccessInResponse(customer)
    }

    @PUT
    @Path("/{id}")
    fun updateCustomer(
        @PathParam("id") id: String, updateCustomerDto: UpdateCustomerDto
    ): ApiResponse<Customer> {
        val updatedCustomer = customerService.updateCustomer(id, updateCustomerDto)
        return wrapSuccessInResponse(updatedCustomer)
    }

    @GET
    fun getAllCustomers(): ApiResponse<List<CustomerDto>> {
        val customers = customerService.getAllCustomers()
        val customerDto = customers.map { customer -> mapProductToDto(customer) }
        return wrapSuccessInResponse(customerDto)
    }

    private fun mapProductToDto(customer: Customer): CustomerDto {
        return CustomerDto(
            id = customer.id,
            name = customer.name,
            email = customer.email,
            phoneNumber = customer.phoneNumber,
            country = customer.country,
            city = customer.city,
            taxNumber = customer.taxNumber,
            currency = customer.currency
        )
    }

    @DELETE
    @Path("/{id}")
    fun deleteCustomerById(@PathParam("id") id: String): ApiResponse<Boolean> {
        customerService.deleteCustomerById(id)
        return wrapSuccessInResponse(true)
    }

}