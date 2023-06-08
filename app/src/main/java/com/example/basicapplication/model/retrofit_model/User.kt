package com.example.basicapplication.model.retrofit_model


import com.example.basicapplication.model.base_model.UserInterface
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("accountNonExpired")
    val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked")
    val accountNonLocked: Boolean,
    @SerializedName("birthday")
    override val birthday: String,
    @SerializedName("code")
    val code: Any,
    @SerializedName("confirmationToken")
    val confirmationToken: Any,
    @SerializedName("credentialsNonExpired")
    val credentialsNonExpired: Boolean,
    @SerializedName("email")
    override val email: String,
    @SerializedName("emailCanonical")
    val emailCanonical: String,
    @SerializedName("enabled")
    override val enabled: Boolean,
    @SerializedName("fullName")
    val fullName: Any,
    @SerializedName("groupNames")
    val groupNames: List<Any>,
    @SerializedName("groups")
    val groups: List<Any>,
    @SerializedName("id")
    override val id: Int,
    @SerializedName("lastLogin")
    val lastLogin: Any,
    @SerializedName("newPassword")
    val newPassword: Any,
    @SerializedName("oldPassword")
    val oldPassword: Any,
    @SerializedName("password")
    val password: String,
    @SerializedName("passwordRequestedAt")
    val passwordRequestedAt: Any,
    @SerializedName("phone")
    val phone: Any,
    @SerializedName("photos")
    val photos: List<Any>,
    @SerializedName("plainPassword")
    val plainPassword: Any,
    @SerializedName("roles")
    override val roles: List<String>,
    @SerializedName("salt")
    val salt: Any,
    @SerializedName("superAdmin")
    val superAdmin: Boolean,
    @SerializedName("user")
    val user: Boolean,
    @SerializedName("username")
    override val username: String,
    @SerializedName("usernameCanonical")
    val usernameCanonical: String
): UserInterface