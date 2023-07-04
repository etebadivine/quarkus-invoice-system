package org.generis.business.invoice.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.invoice.dto.CreateInvoiceDto
import org.generis.business.invoice.dto.UpdateInvoiceStatusDto
import org.generis.business.invoice.repo.Invoice

interface InvoiceResource {

    fun create(@Valid createInvoiceDto: CreateInvoiceDto): ApiResponse<Invoice?>

    fun getInvoiceById(id: String): ApiResponse<Invoice?>

    fun getAllInvoices(): ApiResponse<List<Invoice>>

    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): ApiResponse<Invoice?>

    fun deleteInvoice(id: String): ApiResponse<Boolean>
}