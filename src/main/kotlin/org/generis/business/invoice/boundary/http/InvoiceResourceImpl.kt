package org.generis.business.invoice.boundary.http

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.invoice.boundary.InvoiceResource
import org.generis.business.invoice.dto.CreateInvoiceDto
import org.generis.business.invoice.dto.UpdateInvoiceStatusDto
import org.generis.business.invoice.repo.Invoice
import org.generis.business.invoice.service.InvoiceService
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
class InvoiceResourceImpl : InvoiceResource {

    private val logger = LoggerFactory.getLogger(InvoiceResourceImpl::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var invoiceService: InvoiceService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    override fun create(@Valid createInvoiceDto: CreateInvoiceDto): ApiResponse<Invoice?> {
        logger.info("http request: create")

        val invoice = invoiceService.createInvoice(createInvoiceDto)

        val apiResponse = wrapSuccessInResponse(invoice)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getInvoiceById(@PathParam("id") id: String): ApiResponse<Invoice?> {
        val invoice = invoiceService.getInvoice(id)
        return wrapSuccessInResponse(invoice)
    }

    @GET
    override fun getAllInvoices(): ApiResponse<List<Invoice>> {
        val invoices = invoiceService.getAllInvoices()
        return wrapSuccessInResponse(invoices)
    }

    @PUT
    @Path("/update-status")
    override fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): ApiResponse<Invoice?> {
        val invoice = invoiceService.updateInvoiceStatus(updateInvoiceStatusDto)

        val invoiceDto = modelMapper.map(invoice, Invoice::class.java)
        return wrapSuccessInResponse(invoiceDto)
    }

    @DELETE
    @Path("/{id}")
    override fun deleteInvoice(@PathParam("id") id: String): ApiResponse<Boolean> {
        invoiceService.deleteInvoice(id)
        return wrapSuccessInResponse(true)
    }
}
