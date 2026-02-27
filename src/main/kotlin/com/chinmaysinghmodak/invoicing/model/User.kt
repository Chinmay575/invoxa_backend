package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.time.Instant


@Entity
@Table(name = "tr_users")
@NoArgsConstructor
@AllArgsConstructor
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true)
    var username: String? = null,
    var fullName: String? = null,

    @Column(unique = true)
    var email: String? = null,
    var password: String? = null,
    var isEmailVerified: Boolean = false,
    var profilePic: String? = null,

    @Column(unique = true)
    var mobile: String? = null,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
