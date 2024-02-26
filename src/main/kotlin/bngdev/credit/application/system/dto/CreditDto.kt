package bngdev.credit.application.system.dto


import bngdev.credit.application.system.entity.Credit
import bngdev.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    val creditValue: BigDecimal,
    val dayFirstInstallment: LocalDate,
    val numberOfInstalments: Int,
    val customerId: Long,
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstalments = this.numberOfInstalments,
        customer = Customer(id = this.customerId)
    )
}