package com.degtyarev.githubcontributor.data.storage.db

import com.degtyarev.githubcontributor.data.storage.GitHubUserDao
import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.githubcontributor.domain.User

class GitHubUserLocalDataSourceImpl(
    private val userDao: GitHubUserDao
): GitHubUserLocalDataSource {

    override suspend fun getUsers(): List<User> =
        userDao.getAll()
            .map { User(it.login, it.contributions) }

    override suspend fun clear() = userDao.clear()

    override suspend fun saveAll(users: List<User>) {
        val dbUsers = users.map { user ->
            GitHubUser(
                user.login,
                user.contributions
            )
        }
        userDao.insertAll(dbUsers)
    }

}