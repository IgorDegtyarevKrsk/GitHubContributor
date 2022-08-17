package com.degtyarev.githubcontributor.data.storage

import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

private const val ORG_REPOS = "orgs/{org}/repos?per_page=100"
private const val REPO_CONTRIBUTORS = "repos/{owner}/{repo}/contributors?per_page=100"

interface GitHubService {
    @GET(ORG_REPOS)
    fun getOrgReposCall(
        @Path("org") org: String
    ): Call<List<Repo>>

    @GET(REPO_CONTRIBUTORS)
    fun getRepoContributorsCall(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<List<User>>

    @GET(ORG_REPOS)
    suspend fun getOrgRepos(
        @Path("org") org: String
    ): Response<List<Repo>>

    @GET(REPO_CONTRIBUTORS)
    suspend fun getRepoContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<List<User>>

    @GET(ORG_REPOS)
    fun getOrgReposSingle(
        @Path("org") org: String
    ): Single<List<Repo>>

    @GET(REPO_CONTRIBUTORS)
    fun getRepoContributorsSingle(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Single<List<User>>

}

