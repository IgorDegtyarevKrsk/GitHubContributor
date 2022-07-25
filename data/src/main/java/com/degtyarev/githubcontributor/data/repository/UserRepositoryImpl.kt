package com.degtyarev.githubcontributor.data.repository

import com.degtyarev.githubcontributor.data.storage.UserDataSource
import com.githubcontributor.domain.repository.UserRepository
import com.githubcontributor.domain.Variant

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository {
    override var userName: String
        get() = userDataSource.getGithubUserName()
        set(value) { userDataSource.saveGithubUserName(value) }
    override var token: String
        get() = userDataSource.getPasswordOrToken()
        set(value) { userDataSource.savePassword(value) }
    override var organization: String
        get() = userDataSource.getOrganization()
        set(value) { userDataSource.saveOrganization(value) }
    override var variant: Variant
        get() = userDataSource.getVariant()
        set(value) { userDataSource.saveVariant(value) }
}