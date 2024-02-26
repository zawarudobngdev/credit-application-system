package bngdev.credit.application.system.entity

import bngdev.credit.application.system.enums.Status
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
class Credit(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
        @Column(nullable = false) var creditCode: UUID = UUID.randomUUID(),
        @Column(nullable = false) var creditValue: BigDecimal = BigDecimal.ZERO,
        @Column(nullable = false) var dayFirstInstallment: LocalDate,
        @Column(nullable = false) var numberOfInstallments: Int = 0,
        @Enumerated(value = EnumType.STRING) var status: Status = Status.IN_PROGRESS,
        @ManyToOne var customer: Customer? = null
)
