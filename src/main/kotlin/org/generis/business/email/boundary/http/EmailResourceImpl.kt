package org.generis.business.email.boundary.http


import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.generis.base.integrations.RecurringInvoice
import org.generis.business.email.boundary.EmailResource
import org.generis.business.email.service.EmailService
import org.generis.business.invoice.service.InvoiceService


@Path("send-mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EmailResourceImpl: EmailResource {

    @Inject
    lateinit var emailService: EmailService

    @Inject
    lateinit var invoiceService: InvoiceService

    @Inject
    lateinit var invoiceMail: RecurringInvoice

    @POST
    @Path("{id}")
    override fun sendMail(@PathParam("id")id: String):Response {

        val invoice = invoiceService.getInvoice(id)

        emailService.sendEmail(invoice?.id!!, invoiceMail)
        return Response.ok().build()
    }
}
