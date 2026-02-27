package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.model.Organization
import com.chinmaysinghmodak.invoicing.model.Role
import com.chinmaysinghmodak.invoicing.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    var roleRepository: RoleRepository
) {
   fun createRole(name: String, organization: Organization): Role {

        val role = Role(
            name = name,
            organization = organization
        )

       return roleRepository.save(role)
    }
}