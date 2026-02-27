package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
}