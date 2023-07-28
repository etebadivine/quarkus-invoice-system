package org.generis.business.email.service

import org.generis.base.integrations.RecurringInvoice


interface EmailService {
    fun sendEmail(id: String, invoiceMail: RecurringInvoice)

}