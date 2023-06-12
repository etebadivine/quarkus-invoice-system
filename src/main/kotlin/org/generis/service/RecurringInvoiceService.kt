package org.generis.service

import org.generis.dto.*
import org.generis.entity.RecurringInvoice


interface RecurringInvoiceService {

    fun create(createRecurringInvoiceDto: CreateRecurringInvoiceDto): RecurringInvoice

    fun get(id: String): RecurringInvoiceDto?

    fun getAllIds(): List<String>?

    fun getAll(): List<RecurringInvoiceDto>

//    fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): RecurringInvoice

//    fun deleteInvoice(id: String)
}