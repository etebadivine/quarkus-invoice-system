package org.generis.controller

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.CreateRecurringInvoiceDto
import org.generis.dto.RecurringInvoiceDto
import org.generis.service.RecurringInvoiceService
import org.generis.util.wrapSuccessInResponse
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("recurring-invoices")
@Produces(MediaType.APPLICATION_JSON)
class RecurringInvoiceController {

    private val logger = LoggerFactory.getLogger(RecurringInvoiceController::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var recurringInvoiceService: RecurringInvoiceService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(@Valid createRecurringInvoiceDto: CreateRecurringInvoiceDto): ApiResponse<RecurringInvoiceDto> {
        logger.info("http request: create")

        val recurringInvoice = recurringInvoiceService.create(createRecurringInvoiceDto)

        val recurringInvoiceDto = modelMapper.map(recurringInvoice, RecurringInvoiceDto::class.java)
        val apiResponse = wrapSuccessInResponse(recurringInvoiceDto)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getRecurringInvoiceById( @PathParam("id") id: String): ApiResponse<RecurringInvoiceDto?> {
        val recurringInvoice = recurringInvoiceService.get(id)
        return wrapSuccessInResponse(recurringInvoice)
    }

    @GET
    fun getAllRecurringInvoices(): ApiResponse<List<RecurringInvoiceDto>> {
        val invoices = recurringInvoiceService.getAll()
        return wrapSuccessInResponse(invoices)
    }

}