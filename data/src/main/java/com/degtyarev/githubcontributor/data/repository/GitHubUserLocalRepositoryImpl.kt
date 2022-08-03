package com.degtyarev.githubcontributor.data.repository

import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.githubcontributor.domain.User
import com.githubcontributor.domain.repository.GitHubUserLocalRepository

class GitHubUserLocalRepositoryImpl(
    private val gitHubUserLocalDataSource: GitHubUserLocalDataSource
): GitHubUserLocalRepository {
    override suspend fun getUsers(): List<User> = gitHubUserLocalDataSource.getUsers()
    override suspend fun clear() = gitHubUserLocalDataSource.clear()
    override suspend fun saveAll(users: List<User>) = gitHubUserLocalDataSource.saveAll(users)
}