package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

}