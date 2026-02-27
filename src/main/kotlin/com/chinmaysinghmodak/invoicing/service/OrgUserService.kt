package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.Organization
import com.chinmaysinghmodak.invoicing.model.Role
import com.chinmaysinghmodak.invoicing.model.User
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import org.springframework.stereotype.Service

@Service
class OrgUserService(
   var orgUserRepository: OrgUserRepository
) {

    fun createOrgUser(user: User, organization: Organization, role: Role): OrgUser {
       val orgUser = OrgUser(
           user = user,
           organization = organization,
           role = role
       )

        if(orgUserRepository.findByUser(user) != null) {
            throw Exception("User already belongs to an organization")
        }

        orgUserRepository.save(orgUser)

        return orgUser
    }

    fun updateRole(userId: Long, roleId: Long) {

    }

    fun getOrgUserByUserId(user: User): List<OrgUser>? {
        return orgUserRepository.findAllByUserId(user)
    }
}