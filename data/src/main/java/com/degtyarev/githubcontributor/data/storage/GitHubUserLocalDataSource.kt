package com.degtyarev.githubcontributor.data.storage

import com.githubcontributor.domain.User
import kotlinx.coroutines.flow.Flow

interface GitHubUserLocalDataSource {
    fun getAll(): Flow<List<User>>
    fun clear()
    fun saveAll(users: List<User>)
}