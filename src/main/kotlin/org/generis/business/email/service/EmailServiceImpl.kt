package org.generis.business.email.service

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.generis.base.integrations.RecurringInvoice
import org.generis.business.invoice.repo.Invoice
import org.generis.business.invoice.service.InvoiceService


@Transactional
@ApplicationScoped
class EmailServiceImpl: EmailService {
    @Inject
    lateinit var mailer: Mailer

    @Inject
    lateinit var invoiceService: InvoiceService

    override fun sendEmail(id: String, invoiceMail: RecurringInvoice) {

        val invoice = invoiceService.getInvoice(id)

        val customerMail = invoice?.customerId?.email ?: invoice?.company?.email

        val mailMessage = invoiceMail.invoiceMail(invoice!!)

         mailer.send(Mail.withHtml(customerMail, "Invoice", "")
             .setHtml(mailMessage))
    }
}