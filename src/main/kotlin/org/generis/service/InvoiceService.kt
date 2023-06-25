package org.generis.service

import org.generis.dto.CreateInvoiceDto
import org.generis.dto.UpdateInvoiceStatusDto
import org.generis.entity.Invoice


interface InvoiceService {

    fun createInvoice(createInvoiceDto: CreateInvoiceDto): Invoice?

    fun getInvoice(id: String): Invoice?

    fun getAllInvoices(): List<Invoice>

    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): Invoice?

    fun deleteInvoice(id: String)
}