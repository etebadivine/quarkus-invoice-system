package org.generis.business.smtp.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "smtp")
class Smtp : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "auth_method")
    var authMethods: String? = null

    @Column(name = "smtp_source")
    var from: String? = null

    @Column(name = "smtp_host")
    var host: String? = null

    @Column(name = "smtp_port")
    var port: Int? = null

    @Column(name = "mailer_ssl")
    var ssl: Boolean? = null

    @Column(name = "smtp_username")
    var userName: String? = null

    @Column(name = "smtp_password")
    var password: String? = null

    @Column(name = "mailer_mock")
    var mock: Boolean? = null

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null
}