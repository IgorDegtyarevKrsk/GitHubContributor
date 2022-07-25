package com.degtyarev.githubcontributor.data.repository

import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.githubcontributor.domain.User
import com.githubcontributor.domain.repository.GitHubUserLocalRepository
import kotlinx.coroutines.flow.Flow

class GitHubUserLocalRepositoryImpl(
    private val gitHubUserLocalDataSource: GitHubUserLocalDataSource
): GitHubUserLocalRepository {
    override fun getAll(): Flow<List<User>> = gitHubUserLocalDataSource.getAll()
    override fun clear(): Unit = gitHubUserLocalDataSource.clear()
    override fun saveAll(users: List<User>) = gitHubUserLocalDataSource.saveAll(users)
}