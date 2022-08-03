package com.degtyarev.githubcontributor.data.storage

import com.githubcontributor.domain.User

interface GitHubUserLocalDataSource {
    suspend fun getUsers(): List<User>
    suspend fun clear()
    suspend fun saveAll(users: List<User>)
}