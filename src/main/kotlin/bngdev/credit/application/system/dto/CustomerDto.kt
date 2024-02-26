package bngdev.credit.application.system.dto

import bngdev.credit.application.system.entity.Address
import bngdev.credit.application.system.entity.Customer
import org.aspectj.apache.bcel.classfile.Code
import java.math.BigDecimal

class CustomerDto(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val password: String,
    val zipCode: String,
    val street: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}
