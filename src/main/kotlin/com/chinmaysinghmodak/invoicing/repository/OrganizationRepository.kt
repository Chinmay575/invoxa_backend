package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : JpaRepository<Organization, Long>{
}