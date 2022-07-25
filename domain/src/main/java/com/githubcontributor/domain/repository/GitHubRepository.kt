package com.githubcontributor.domain.repository

import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import retrofit2.Call
import retrofit2.Response

interface GitHubRepository {

    fun getOrgReposCall(org: String): Call<List<Repo>>

    fun getRepoContributorsCall(owner: String, repo: String): Call<List<User>>

    suspend fun getOrgRepos(org: String): Response<List<Repo>>

    suspend fun getRepoContributors(owner: String, repo: String): Response<List<User>>

}