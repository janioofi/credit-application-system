package br.com.jan1ooo.creditrequestsystem.service

import br.com.jan1ooo.creditrequestsystem.entity.Address
import br.com.jan1ooo.creditrequestsystem.entity.Customer
import br.com.jan1ooo.creditrequestsystem.exception.BusinessException
import br.com.jan1ooo.creditrequestsystem.repository.CustomerRepository
import br.com.jan1ooo.creditrequestsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK lateinit var  customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun `should create customer`(){
        // given
        val fakeCustomer: Customer = buildCustomer()
        every{customerRepository.save(any())} returns fakeCustomer
        // when
        val actual: Customer = customerService.save(fakeCustomer)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify (exactly = 1){ customerRepository.save(fakeCustomer)  }
    }

    @Test
    fun `should find customer by id` (){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        //when
        val customer: Customer = customerService.findById(fakeId)
        //then
        Assertions.assertThat(customer).isNotNull
        Assertions.assertThat(customer).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(customer).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should not find customer by id and throw BusinessException`(){
        //given
        val fakeId: Long = Random().nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
                .isThrownBy {  customerService.findById(fakeId)}
                .withMessage("Id $fakeId not found")
        verify(exactly = 1) { customerRepository.findById(fakeId)  }
    }

    @Test
    fun `should delete customer by id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs
        //when
        customerService.delete(fakeId)
        //then
        verify(exactly = 1) { customerRepository.findById(fakeId)  }
        verify(exactly = 1) { customerRepository.delete(fakeCustomer)  }
    }

    private fun buildCustomer(
            firstName: String = "Janio",
            lastName: String = "Silva",
            cpf: String = "88573884096",
            email: String = "janinho@gmail.com",
            password: String = "12345",
            zipCode: String = "02900000",
            street: String = "Miguel Silveira",
            income: BigDecimal = BigDecimal.valueOf(1000.0),
            id: Long = 1L
    ) = Customer(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            password = password,
            address = Address(
                    zipCode = zipCode,
                    street = street
            ),
            income = income,
            id = id
    )
}