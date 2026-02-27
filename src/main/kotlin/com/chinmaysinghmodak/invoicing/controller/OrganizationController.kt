package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.CreateOrganizationRequest
import com.chinmaysinghmodak.invoicing.model.Organization
import com.chinmaysinghmodak.invoicing.service.OrganizationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/organization")
class OrganizationController(
    var organizationService: OrganizationService,
) {


    @PostMapping("")
    fun createOrganization(@RequestBody @Valid request: CreateOrganizationRequest) : ResponseEntity<ApiResponse<Organization>> {
        try {
            val organization =  organizationService.createOrganization(request.name)
            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    data = organization,
                    message = "Organization created successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.ok(
                ApiResponse(
                    success = false,
                    error = e.message,
                    message = "Organization creation failed"
                )
            )
        }
    }

    @PostMapping("/{id}/join")
    fun joinOrganization() {

    }

    @PostMapping("/{id}")
    fun updateOrganization() {

    }
}