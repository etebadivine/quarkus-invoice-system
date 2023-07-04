package org.generis.business.email.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator


@Entity
@Table(name = "emails")
class Email : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "recipient")
    var recipient: String? = null

    @Column(name = "subject")
    var subject: String? = null

    @Column(name = "body")
    var body: String? = null

    @Column(name = "attachment")
    var attachment: String? = null

}