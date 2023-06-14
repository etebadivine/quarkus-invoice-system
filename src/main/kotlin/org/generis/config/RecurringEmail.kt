package org.generis.config


import io.quarkus.mailer.Attachment
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.quarkus.mailer.reactive.ReactiveMailer
import io.quarkus.scheduler.Scheduled
import io.quarkus.scheduler.Scheduler
import jakarta.inject.Inject

import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import jakarta.ws.rs.PathParam
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.generis.dto.InvoiceDto
import org.generis.dto.SubscriptionDto
import org.generis.entity.Customer
import org.generis.service.CustomerService
import org.generis.service.SubscriptionService
import java.io.ByteArrayOutputStream



@Singleton
class RecurringEmail(
    private val subscriptionService: SubscriptionService,
    private val customerService: CustomerService,
    private val mailer: Mailer
)
{
     fun generateRecurringInvoice(customer: Customer, subscriptions: List<SubscriptionDto>): ByteArray {
        val document = PDDocument()
        val page = PDPage(PDRectangle.A4)
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)
        contentStream.beginText()
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12f)
        contentStream.newLineAtOffset(50f, 700f)

        contentStream.newLineAtOffset(50f, 700f)
        contentStream.showText("Customer Name: ${customer.name}")
        contentStream.newLine()
        contentStream.showText("Email: ${customer.email}")
        contentStream.newLine()

        subscriptions.forEach { subscription ->
            contentStream.showText("Product: ${subscription.items} - Price: ${subscription.totalAmount}")
        }

        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()

        return outputStream.toByteArray()
    }


    fun generateInvoice(customerId: String, invoice: List<InvoiceDto>): ByteArray {
        val customer = customerService.getCustomer(customerId)

        val document = PDDocument()
        val page = PDPage(PDRectangle.A4)
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)
        contentStream.beginText()
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12f)
        contentStream.newLineAtOffset(50f, 700f)

        contentStream.newLineAtOffset(50f, 700f)
        contentStream.showText("Customer Name: ${customer?.name}")
        contentStream.newLine()
        contentStream.showText("Email: ${customer?.email}")
        contentStream.newLine()

        invoice.forEach { invoice ->
            contentStream.showText("Product: ${invoice.items} - Price: ${invoice.totalAmount}")
        }

        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()

        return outputStream.toByteArray()
    }



//
////    (cron = "0 0 0 1 * ?")
//    @Scheduled(cron = "*/5 * * * * ?") // Run at 12:00 AM on the 1st day of every month
//    @Transactional
//    fun generateAndSendInvoices() {
//        val subscriptions = subscriptionService.getAllSubscriptions()
//        val customers = customerService.getAllCustomers()
//
//        customers.forEach { customer ->
//            val customerSubscriptions = subscriptions.filter { it.customerId == customer.id }
//            if (customerSubscriptions.isNotEmpty()) {
//                val invoicePdf = generateInvoice(customer, customerSubscriptions)
//                sendInvoiceByEmail(customer, invoicePdf)
//            }
//        }
//    }

   fun sendInvoiceByEmail(customerId: String, invoicePdf: ByteArray) {

       val customer = customerService.getCustomer(customerId)

        val emailContent = "Dear customer, here is your monthly invoice in pdf $invoicePdf"
        val recipientEmail = customer?.email ?: "etebadivine358@gmail.com"

        mailer.send(Mail.withText(recipientEmail, emailContent, "Your Invoice."));

        println("Email sent successfully!")
    }
}



