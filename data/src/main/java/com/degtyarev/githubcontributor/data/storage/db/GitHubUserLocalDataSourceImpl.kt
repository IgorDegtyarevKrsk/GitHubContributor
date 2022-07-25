package com.degtyarev.githubcontributor.data.storage.db

import com.degtyarev.githubcontributor.data.storage.GitHubUserDao
import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.githubcontributor.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GitHubUserLocalDataSourceImpl(
    private val userDao: GitHubUserDao
): GitHubUserLocalDataSource {

    override fun getAll(): Flow<List<User>> {
        return userDao.getAllFlow().map {
            mutableListOf<User>().apply {
                it.forEach {
                    add(User(it.login, it.contributions))
                }
            }
        }
    }

    override fun clear() = userDao.clear()

    override fun saveAll(users: List<User>) {
        val dbUsers = users.map { user ->
            GitHubUser(
                user.login,
                user.contributions
            )
        }
        userDao.insertAll(dbUsers)
    }

}