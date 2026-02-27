package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.Token
import com.chinmaysinghmodak.invoicing.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<Token, Long> {

    fun findByTokenHashAndRevokedFalse(tokenHash: String): Token?

    fun findByUserAndDeviceIdAndRevokedFalse(user: User, deviceId: String): Token?

    @Modifying
    @Query("UPDATE Token t SET t.revoked = true, t.updatedAt = CURRENT_TIMESTAMP WHERE t.user = :user")
    fun revokeAllTokensByUser(user: User): Int

    @Modifying
    @Query("UPDATE Token t SET t.revoked = true, t.updatedAt = CURRENT_TIMESTAMP WHERE t.user = :user AND t.deviceId = :deviceId")
    fun revokeAllTokensByUserAndDevice(user: User, deviceId: String): Int
}