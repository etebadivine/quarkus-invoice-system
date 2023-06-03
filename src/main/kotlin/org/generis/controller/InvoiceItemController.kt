package org.generis.controller

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.CreateInvoiceItemDto
import org.generis.dto.InvoiceItemDto
import org.generis.entity.InvoiceItem
import org.generis.service.InvoiceItemService
import org.generis.util.wrapSuccessInResponse
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("invoice-items")
@Produces(MediaType.APPLICATION_JSON)
class InvoiceItemController {

    private val logger = LoggerFactory.getLogger(InvoiceItemController::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var invoiceItemService: InvoiceItemService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createInvoiceItem(createInvoiceItemDto: CreateInvoiceItemDto): ApiResponse<InvoiceItem> {
        val invoice = invoiceItemService.create(createInvoiceItemDto)
        return wrapSuccessInResponse(invoice)
    }

    @GET
    @Path("{id}")
    fun getInvoiceItem(@PathParam("id") id: String): ApiResponse<InvoiceItem?> {
        val invoiceItem = invoiceItemService?.get(id)
        return wrapSuccessInResponse(invoiceItem)
    }
}