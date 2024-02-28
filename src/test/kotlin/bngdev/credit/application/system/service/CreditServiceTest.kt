package bngdev.credit.application.system.service

import bngdev.credit.application.system.entity.Credit
import bngdev.credit.application.system.entity.Customer
import bngdev.credit.application.system.exception.BusinessException
import bngdev.credit.application.system.repository.CreditRepository
import bngdev.credit.application.system.service.impl.CreditService
import bngdev.credit.application.system.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    //will prob need customer refence

    @Test
    fun `should create credit`() {
        //given
        val customerId = 1L
        val fakeCredit: Credit = buildCredit()

        every { customerService.findById(customerId) } returns fakeCredit.customer!!
        every { creditRepository.save(fakeCredit) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        verify(exactly = 1) { customerService.findById(customerId) }
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
    }

    @Test
    fun `should not create credit when day first installment is invalid`() {
        //given
        val invalidFirstDayInstallment: LocalDate = LocalDate.now().plusMonths(5)
        val invalidCredit: Credit = buildCredit(dayFirstInstallment = invalidFirstDayInstallment)
        every { creditRepository.save(invalidCredit) } answers { invalidCredit }
        //when
        Assertions.assertThatThrownBy { creditService.save(invalidCredit) }
            .isInstanceOfAny(BusinessException::class.java).hasMessage("Invalid date")
        //then
        verify(exactly = 0) { creditRepository.save(any()) }
    }

    @Test
    fun `should return list of credits for a customer id`() {
        //given
        val customerId = 1L
        val expectedCredits: List<Credit> = listOf(buildCredit(), buildCredit(), buildCredit())
        every { creditRepository.findAllByCustomer(customerId) } returns expectedCredits
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(customerId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(expectedCredits)
        verify(exactly = 1) { creditRepository.findAllByCustomer(customerId) }
    }

    @Test
    fun `should return credit for credit code`() {
        //given
        val customerId = 1L
        val creditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = customerId))
        every { creditRepository.findByCreditCode(creditCode) } returns fakeCredit
        //when
        val actual: Credit = creditService.findByCreditCode(customerId, creditCode)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    @Test
    fun `should throw BusinessException for invalid credit code`() {
        //given
        val customerId = 1L
        val invalidCreditCode: UUID = UUID.randomUUID()
        every { creditRepository.findByCreditCode(invalidCreditCode) } returns null
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(customerId, invalidCreditCode) }
            .isInstanceOf(BusinessException::class.java).hasMessage("Credit code $invalidCreditCode not found")
        verify(exactly = 1) { creditRepository.findByCreditCode(invalidCreditCode) }
    }

    @Test
    fun `should throw IllegalArgumentException for different customer id`() {
        //given
        val customerId = 1L
        val creditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = 2L))
        every { creditRepository.findByCreditCode(creditCode) } returns fakeCredit
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(customerId, creditCode) }
            .isInstanceOf(IllegalArgumentException::class.java).hasMessage("Contact admin")
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }

    }

    companion object {
        private fun buildCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(2000.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(2L),
            numberOfInstallments: Int = 10,
            customer: Customer = CustomerServiceTest.buildCustomer()
        ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer
        )
    }
}