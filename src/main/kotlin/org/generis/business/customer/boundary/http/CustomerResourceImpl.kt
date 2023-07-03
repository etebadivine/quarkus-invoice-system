package org.generis.business.customer.boundary.http

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.customer.boundary.CustomerResource
import org.generis.business.customer.dto.CreateCustomerDto
import org.generis.business.customer.dto.UpdateCustomerDto
import org.generis.business.customer.repo.Customer
import org.generis.business.customer.service.CustomerService
import org.slf4j.LoggerFactory


@Path("customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CustomerResourceImpl: CustomerResource {

    private val logger = LoggerFactory.getLogger(CustomerResourceImpl::class.java)

    @Inject
    lateinit var customerService: CustomerService
    @POST
    override fun create(@Valid createCustomerDto: CreateCustomerDto): ApiResponse<Customer?> {
        logger.info("http request: create")

        val customer = customerService.createCustomer(createCustomerDto)

        val apiResponse = wrapSuccessInResponse(customer)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getCustomer(@PathParam("id") id: String): ApiResponse<Customer?> {
        val customer = customerService?.getCustomer(id)
        return wrapSuccessInResponse(customer)
    }

    @PUT
    @Path("/{id}")
    override fun updateCustomer(
        @PathParam("id") id: String, updateCustomerDto: UpdateCustomerDto
    ): ApiResponse<Customer?> {
        val updatedCustomer = customerService.updateCustomer(id, updateCustomerDto)
        return wrapSuccessInResponse(updatedCustomer)
    }

    @GET
    override fun getAllCustomers(): ApiResponse<List<Customer>> {
        val customers = customerService.getAllCustomers()
        return wrapSuccessInResponse(customers)
    }

    @DELETE
    @Path("/{id}")
    override fun deleteCustomerById(@PathParam("id") id: String): ApiResponse<Boolean> {
        customerService.deleteCustomerById(id)
        return wrapSuccessInResponse(true)
    }
}