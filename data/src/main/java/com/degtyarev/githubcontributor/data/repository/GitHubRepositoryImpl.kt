package com.degtyarev.githubcontributor.data.repository

import com.degtyarev.githubcontributor.data.storage.GitHubService
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import retrofit2.Call
import retrofit2.Response

class GitHubRepositoryImpl(
    private val service: GitHubService
): GitHubRepository {
    override fun getOrgReposCall(org: String): Call<List<Repo>> {
        return service.getOrgReposCall(org)
    }

    override fun getRepoContributorsCall(owner: String, repo: String): Call<List<User>> {
        return service.getRepoContributorsCall(owner, repo)
    }

    override suspend fun getOrgRepos(org: String): Response<List<Repo>> {
        return service.getOrgRepos(org)
    }

    override suspend fun getRepoContributors(owner: String, repo: String): Response<List<User>> {
        return service.getRepoContributors(owner, repo)
    }
}