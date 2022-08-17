package com.githubcontributor.domain.repository

import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response

interface GitHubRepository {

    fun getOrgReposCall(org: String): Call<List<Repo>>

    fun getRepoContributorsCall(owner: String, repo: String): Call<List<User>>

    suspend fun getOrgRepos(org: String): Response<List<Repo>>

    suspend fun getRepoContributors(owner: String, repo: String): Response<List<User>>

    fun getOrgReposSingle(org: String): Single<List<Repo>>

    fun getRepoContributorsSingle(owner: String, repo: String): Single<List<User>>

}