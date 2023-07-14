package org.generis.business.logs.service

import io.quarkus.security.identity.SecurityIdentity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken
import org.generis.business.logs.dto.UserInfo


@ApplicationScoped
class JwtServiceImpl: JwtService {

    @Inject
    lateinit var  jwt: JsonWebToken

    @Inject
    lateinit var securityContext : SecurityIdentity

    override fun getUserInfo(): UserInfo {
        val id = jwt.subject
        val username = jwt.name
        val email =  jwt.claim<String>("email").toString()
            .replace("Optional[","").replace("]","")
        val roles = securityContext.roles.toList()
        return UserInfo(id, username, email, roles)
    }
}