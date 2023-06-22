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
//import org.keycloak.admin.client.Keycloak
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CustomerController {

    private val logger = LoggerFactory.getLogger(CustomerController::class.java)
    private val modelMapper = ModelMapper()

//    @Inject
//    lateinit var keycloak : Keycloak

    @Inject
    lateinit var customerService: CustomerService
    @POST
    fun create(@Valid createCustomerDto:CreateCustomerDto): ApiResponse<Customer> {
        logger.info("http request: create")

        val customer = customerService.createCustomer(createCustomerDto)

        val apiResponse = wrapSuccessInResponse(customer)

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
    fun getAllCustomers(): ApiResponse<List<Customer>> {
        val customers = customerService.getAllCustomers()
        return wrapSuccessInResponse(customers)
    }

    private fun mapCustomerToDto(customer: Customer): CustomerDto {
        return CustomerDto(
            id = customer.id,
            name = customer.name,
            email = customer.email,
            phoneNumber = customer.phoneNumber,
            country = customer.country,
            city = customer.city,
            taxNumber = customer.taxNumber,
            currency = customer.currency,
            createdDate = customer.createdDate
        )
    }

    @DELETE
    @Path("/{id}")
    fun deleteCustomerById(@PathParam("id") id: String): ApiResponse<Boolean> {
        customerService.deleteCustomerById(id)
        return wrapSuccessInResponse(true)
    }

//    @GET
//    @Path("/keycloak-users")
//    fun getUsers(): Response {
//        val users = keycloak.realm("master").users()
//        return Response.ok(users).build()
//    }
}