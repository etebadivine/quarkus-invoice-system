package org.generis.business.company.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.business.country.repo.Country
import org.generis.business.invoice.repo.InvoiceItem
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "companies")
class Company : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "company_name")
    var name: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "phone_number")
    var phoneNumber: String? = null

    @Column(name = "address")
    var address: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country")
    var country: Country? = null

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company", cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    val staff: MutableList<CompanyStaff> = mutableListOf()

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null
}

