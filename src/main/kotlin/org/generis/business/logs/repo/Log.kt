package org.generis.business.logs.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.business.logs.enums.LogAction
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "logs")
class Log : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "log_action")
    @Enumerated(value = EnumType.STRING)
    var action: LogAction? = null

    var target: String? = null

    @Column(name = "user")
    var userId: String? = null

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null
}