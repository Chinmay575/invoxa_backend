package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.customer.CreateCustomerRequest
import com.chinmaysinghmodak.invoicing.dto.customer.CustomerDto
import com.chinmaysinghmodak.invoicing.dto.customer.UpdateCustomerRequest
import com.chinmaysinghmodak.invoicing.dto.customer.toCustomerDto
import com.chinmaysinghmodak.invoicing.model.Customer
import com.chinmaysinghmodak.invoicing.repository.CustomerRepository
import com.chinmaysinghmodak.invoicing.repository.InvoiceItemRepository
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val orgUserRepository: OrgUserRepository,
) {

    fun createCustomer(request: CreateCustomerRequest, userId: Long): CustomerDto {

        try {
            val user = orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }


            val newCustomer = Customer(
                name = request.name,
                email = request.email,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                mobile = request.mobile,
                taxNumber = request.taxNumber,
                address = request.address,
                organization = user.organization,
            )
            val customer = customerRepository.save(newCustomer)

            return toCustomerDto(customer)
        } catch (e: Exception) {
            throw e;
        }
    }


    fun getAllCustomers(userId: Long): List<CustomerDto> {
        try {
            val user = orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }
            val customers = customerRepository.findAllByOrganization(user.organization)
            return customers.map { toCustomerDto(it) }
        } catch (e: Exception) {
            throw e;
        }

    }

    fun getCustomerById(customerId: Long, userId: Long): CustomerDto {
        try {
            val user = orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }
            val customer = customerRepository.findById(customerId).orElseThrow { Exception("Customer does not exist") }

            if (customer.organization?.id != user.organization?.id) {
                throw Exception("Customer does not belong to user's organization")
            }

            return toCustomerDto(customer)
        } catch (e: Exception) {
            throw e;
        }
    }

    fun updateCustomer(id: Long, request: UpdateCustomerRequest, userId: Long): CustomerDto {
        try {
            val user = orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }
            val customer = customerRepository.findById(id).orElseThrow { Exception("Customer does not exist") }

            if (customer.organization?.id != user.organization?.id) {
                throw Exception("Customer does not belong to user's organization")
            }

            customer.name = request.name
            customer.email = request.email
            customer.address = request.address
            customer.mobile = request.mobile
            customer.taxNumber = request.taxNumber
            customer.updatedAt = Instant.now()

            val updatedCustomer = customerRepository.save(customer)

            return toCustomerDto(updatedCustomer)
        } catch (e: Exception) {
            throw e;
        }
    }
}