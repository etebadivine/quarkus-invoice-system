package org.generis.business.email.boundary

import jakarta.ws.rs.core.Response

interface EmailResource {
    fun sendMail(id: String): Response
}