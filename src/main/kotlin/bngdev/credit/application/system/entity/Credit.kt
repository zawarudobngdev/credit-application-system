package bngdev.credit.application.system.entity

import bngdev.credit.application.system.enums.Status
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
data class Credit(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
        @Column(nullable = false) val creditCode: UUID = UUID.randomUUID(),
        @Column(nullable = false) val creditValue: BigDecimal = BigDecimal.ZERO,
        @Column(nullable = false) val dayFirstInstallment: LocalDate,
        @Column(nullable = false) val numberOfInstalments: Int = 0,
        @Enumerated val status: Status = Status.IN_PROGRESS,
        @ManyToOne val customer: Customer? = null
)
