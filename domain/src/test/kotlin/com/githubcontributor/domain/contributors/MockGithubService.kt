package com.githubcontributor.domain.contributors

import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import com.githubcontributor.domain.contributors.reposMap
import com.githubcontributor.domain.repository.GitHubRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Response
import retrofit2.mock.Calls

object MockGithubService : GitHubRepository {
    override fun getOrgReposCall(org: String): Call<List<Repo>> {
        return Calls.response(repos)
    }

    override fun getRepoContributorsCall(owner: String, repo: String): Call<List<User>> {
        return Calls.response(reposMap.getValue(repo).users)
    }

    override suspend fun getOrgRepos(org: String): Response<List<Repo>> {
        delay(reposDelay)
        return Response.success(repos)
    }

    override suspend fun getRepoContributors(owner: String, repo: String): Response<List<User>> {
        val testRepo = reposMap.getValue(repo)
        delay(testRepo.delay)
        return Response.success(testRepo.users)
    }

    override fun getOrgReposSingle(org: String): Single<List<Repo>> {
        TODO("Not yet implemented")
    }

    override fun getRepoContributorsSingle(owner: String, repo: String): Single<List<User>> {
        TODO("Not yet implemented")
    }
}