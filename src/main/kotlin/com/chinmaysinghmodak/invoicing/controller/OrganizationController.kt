package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.CreateOrganizationRequest
import com.chinmaysinghmodak.invoicing.model.Organization
import com.chinmaysinghmodak.invoicing.service.OrganizationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/organization")
@Tag(name = "Organization", description = "Organization creation and management")
class OrganizationController(
    var organizationService: OrganizationService,
) {


    @Operation(summary = "Create organization", description = "Creates a new organization")
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

    @Operation(summary = "Join organization", description = "Allows a user to join an existing organization")
    @PostMapping("/{id}/join")
    fun joinOrganization() {

    }

    @Operation(summary = "Update organization", description = "Updates an existing organization's details")
    @PostMapping("/{id}")
    fun updateOrganization() {

    }
}