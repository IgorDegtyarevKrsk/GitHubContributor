package com.degtyarev.githubcontributor.data.storage

import com.githubcontributor.domain.Variant

interface UserDataSource {
    fun getGithubUserName(): String
    fun getPasswordOrToken(): String
    fun getOrganization(): String
    fun getVariant(): Variant
    fun saveGithubUserName(newName: String): Unit
    fun savePassword(value: String): Unit
    fun saveOrganization(value: String): Unit
    fun saveVariant(variant: Variant)
}