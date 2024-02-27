package bngdev.credit.application.system.dto.response

import bngdev.credit.application.system.entity.Credit
import bngdev.credit.application.system.enums.Status
import java.math.BigDecimal
import java.util.UUID

data class CreditView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int,
    val status: Status,
    val emailCustomer: String?,
    val incomeCustomer: BigDecimal?
) {
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income
    )
}
