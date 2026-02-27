package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.model.Organization
import com.chinmaysinghmodak.invoicing.repository.OrganizationRepository
import org.springframework.stereotype.Service

@Service
class OrganizationService(var organizationRepository: OrganizationRepository) {

    fun createOrganization(name : String?) : Organization {

        val organization = Organization(
            name = name ?: ""
        )
        return organizationRepository.save(organization)
    }


    fun updateOrganization(name: String, id: Long ) : Organization {
        try {
            val organization = organizationRepository.findById(id).get()
            organization.name = name
            return organizationRepository.save(organization)
        } catch (e: Exception) {
            throw e
        }
    }


    fun joinOrganization() {

    }
}