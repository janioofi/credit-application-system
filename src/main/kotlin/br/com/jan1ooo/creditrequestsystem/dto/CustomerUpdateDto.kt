package br.com.jan1ooo.creditrequestsystem.dto

import br.com.jan1ooo.creditrequestsystem.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
        val firstName: String,
        val lastName: String,
        val income: BigDecimal,
        val zipCode: String,
        val street: String
) {
    fun toEntity(customer: Customer): Customer{
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.street = this.street
        customer.address.zipCode = this.zipCode
        return customer
    }
}
