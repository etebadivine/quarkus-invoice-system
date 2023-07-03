package org.generis.business.invoice.service

import org.generis.business.invoice.dto.CreateInvoiceDto
import org.generis.business.invoice.dto.UpdateInvoiceStatusDto
import org.generis.business.invoice.repo.Invoice


interface InvoiceService {

    fun createInvoice(createInvoiceDto: CreateInvoiceDto): Invoice?

    fun getInvoice(id: String): Invoice?

    fun getAllInvoices(): List<Invoice>

    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): Invoice?

    fun deleteInvoice(id: String)
}