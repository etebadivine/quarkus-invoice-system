package org.generis.service

import org.generis.dto.CreateInvoiceDto
import org.generis.dto.InvoiceDto
import org.generis.dto.UpdateInvoiceStatusDto
import org.generis.entity.Invoice


interface InvoiceService {

    fun createInvoice(createInvoiceDto: CreateInvoiceDto): Invoice

    fun getInvoice(id: String): InvoiceDto?

    fun getAllInvoices(): List<InvoiceDto>

    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): Invoice
    
    fun getInvoiceByCustomerId(customerId: String): List<InvoiceDto>

    fun deleteInvoice(id: String)
}