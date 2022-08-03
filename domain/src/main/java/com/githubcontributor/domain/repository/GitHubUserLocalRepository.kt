package com.githubcontributor.domain.repository

import com.githubcontributor.domain.User

interface GitHubUserLocalRepository {
    suspend fun getUsers(): List<User>
    suspend fun clear()
    suspend fun saveAll(users: List<User>)
}