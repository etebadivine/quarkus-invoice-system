package org.generis.business.company.repo

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator


@Entity
@Table(name = "company_staff")
class CompanyStaff : PanacheEntityBase() {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "staff_name")
    var staffName: String? = null

    @Column(name = "phone_number")
    var phoneNumber: String? = null

    @Column(name = "staff_email")
    var email: String? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    var company: Company? = null

    @Column(name = "staff_role")
    var role: String? = null
}