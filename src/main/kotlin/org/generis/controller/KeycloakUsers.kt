package org.generis.controller

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.Keycloak


@Authenticated
@Path("keycloak")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class KeycloakUsers {

    @Inject
    lateinit var keycloak : Keycloak

    @GET
    fun getUsers(): Response {
        val users = keycloak.realm("nsano").users().list()
        return Response.ok(users).build()
    }
}