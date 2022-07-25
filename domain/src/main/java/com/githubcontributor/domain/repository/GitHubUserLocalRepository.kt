package com.githubcontributor.domain.repository

import com.githubcontributor.domain.User
import kotlinx.coroutines.flow.Flow

interface GitHubUserLocalRepository {
    fun getAll(): Flow<List<User>>
    fun clear()
    fun saveAll(users: List<User>)
}