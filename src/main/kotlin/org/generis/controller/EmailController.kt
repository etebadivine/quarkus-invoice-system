package org.generis.controller

import io.quarkus.scheduler.Scheduled
import io.quarkus.scheduler.Scheduler
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.generis.config.RecurringEmail
import org.generis.domain.ApiResponse
import org.generis.service.CustomerService
import org.generis.service.InvoiceService
import org.generis.util.wrapSuccessInResponse

@Path("email")
@Produces(MediaType.APPLICATION_JSON)
class EmailController {

    @Inject
    lateinit var scheduler: Scheduler


    @Inject
    lateinit var recurringEmail: RecurringEmail

    @Inject
    lateinit var customerService: CustomerService

    @Inject
    lateinit var invoiceService: InvoiceService

//@GET
//@Path("/send")
//@Produces(MediaType.TEXT_PLAIN)
//fun generateAndSendInvoices(): ApiResponse<Boolean> {
//    recurringEmail.generateAndSendInvoices()
//
//    return wrapSuccessInResponse(true)
//}

    @GET
    @Path("/send-email/{customerId}")
    @Produces(MediaType.TEXT_PLAIN)
    fun sendInvoiceByEmail(@PathParam("customerId") customerId: String): ApiResponse<Boolean> {
        val customer = customerService.getCustomer(customerId)

        val invoices = invoiceService.getInvoiceByCustomerId(customerId)

        val invoicePdf =  recurringEmail.generateInvoice(customer?.id!!, invoices) // Generate the invoice PDF

        recurringEmail.sendInvoiceByEmail(customer.id!!, invoicePdf!!) // Send the invoice email

        return wrapSuccessInResponse(true)
    }

}