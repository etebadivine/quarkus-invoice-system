package org.generis.controller

import io.quarkus.mailer.Mailer
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.*
import org.generis.entity.Invoice
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
    fun create(@Valid createInvoiceDto: CreateInvoiceDto): ApiResponse<Invoice?> {
        logger.info("http request: create")

        val invoice = invoiceService.createInvoice(createInvoiceDto)

        val apiResponse = wrapSuccessInResponse(invoice)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getInvoiceById(@PathParam("id") id: String): ApiResponse<Invoice?> {
        val invoice = invoiceService.getInvoice(id)
        return wrapSuccessInResponse(invoice)
    }

    @GET
    fun getAllInvoices(): ApiResponse<List<Invoice>> {
        val invoices = invoiceService.getAllInvoices()
        return wrapSuccessInResponse(invoices)
    }

    @PUT
    @Path("/update-status")
    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): ApiResponse<Invoice?> {
        val invoice = invoiceService.updateInvoiceStatus(updateInvoiceStatusDto)

        val invoiceDto = modelMapper.map(invoice, Invoice::class.java)
        return wrapSuccessInResponse(invoiceDto)
    }

    @DELETE
    @Path("/{id}")
    fun deleteInvoice(@PathParam("id") id: String): ApiResponse<Boolean> {
        invoiceService.deleteInvoice(id)
        return wrapSuccessInResponse(true)
    }
}
