package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrgUserRepository : JpaRepository<OrgUser, Long> {
    fun findByUser(user: User): OrgUser?

    @Query("SELECT * FROM org_user ou WHERE ou.user_id = :#{#user.id}", nativeQuery = true)
    fun findAllByUserId(user: User): List<OrgUser>?
}