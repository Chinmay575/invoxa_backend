package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.PendingVerification
import com.chinmaysinghmodak.invoicing.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PendingVerificationRepository : JpaRepository<PendingVerification, Long> {
    fun findByUniqueToken(token: String): PendingVerification?
    fun findByUserAndType(user: User, type: String): PendingVerification?
    fun deleteByUserAndType(user: User, type: String)
}