package org.generis.service

import org.generis.dto.CreateInvoiceItemDto
import org.generis.entity.InvoiceItem

interface InvoiceItemService {

    fun create(createInvoiceItemDto: CreateInvoiceItemDto) : InvoiceItem

    fun get(id: String) : InvoiceItem?
}