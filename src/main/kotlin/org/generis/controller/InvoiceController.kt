package org.generis.controller

import io.quarkus.mailer.Mailer
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.*
import org.generis.service.InvoiceService
import org.generis.util.wrapSuccessInResponse
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
class InvoiceController {

    private val logger = LoggerFactory.getLogger(InvoiceController::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var invoiceService: InvoiceService

    @Inject
    private lateinit var mailer: Mailer

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(@Valid createInvoiceDto: CreateInvoiceDto): ApiResponse<InvoiceDto> {
        logger.info("http request: create")

        val invoice = invoiceService.createInvoice(createInvoiceDto)

        val invoiceDto = modelMapper.map(invoice, InvoiceDto::class.java)
        val apiResponse = wrapSuccessInResponse(invoiceDto)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getInvoiceById(@PathParam("id") id: String): ApiResponse<InvoiceDto?> {
        val invoice = invoiceService.getInvoice(id)
        return wrapSuccessInResponse(invoice)
    }

    @GET
    fun getAllInvoices(): ApiResponse<List<InvoiceDto>> {
        val invoices = invoiceService.getAllInvoices()
        return wrapSuccessInResponse(invoices)
    }

    @PUT
    @Path("/update-status")
    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): ApiResponse<InvoiceDto> {
        val invoice = invoiceService.updateInvoiceStatus(updateInvoiceStatusDto)

        val invoiceDto = modelMapper.map(invoice, InvoiceDto::class.java)
        return wrapSuccessInResponse(invoiceDto)
    }

    @DELETE
    @Path("/{id}")
    fun deleteInvoice(@PathParam("id") id: String): ApiResponse<Boolean> {
        invoiceService.deleteInvoice(id)
        return wrapSuccessInResponse(true)
    }


    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getInvoiceByCustomerId(@PathParam("customerId") customerId: String): ApiResponse<List<InvoiceDto>> {
        val invoices = invoiceService.getInvoiceByCustomerId(customerId)
        return wrapSuccessInResponse(invoices)
    }
}
