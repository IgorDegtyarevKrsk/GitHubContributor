package com.degtyarev.githubcontributor.data.storage.api

import android.util.Log
import com.degtyarev.githubcontributor.data.storage.GitHubService
import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubServiceLogger(private val service: GitHubService): GitHubService {

    override fun getOrgReposCall(org: String): Call<List<Repo>> {
        return CallExecuteLogger(service.getOrgReposCall(org)) {
            logRepos(org, it)
        }
    }

    override fun getRepoContributorsCall(owner: String, repo: String): Call<List<User>> {
        return CallExecuteLogger(service.getRepoContributorsCall(owner, repo)) {
            logUsers(repo, it)
        }
    }

    override suspend fun getOrgRepos(org: String): Response<List<Repo>> {
        val result = service.getOrgRepos(org)
        logRepos(org, result)
        return result
    }

    override suspend fun getRepoContributors(owner: String, repo: String): Response<List<User>> {
        val result = service.getRepoContributors(owner, repo)
        logUsers(repo, result)
        return result
    }

    override fun getOrgReposSingle(org: String): Single<List<Repo>> {
        return service.getOrgReposSingle(org)
            .doOnSuccess { logReposSuccess(org, it) }
            .doOnError { logReposError(org, it.toString()) }
    }

    override fun getRepoContributorsSingle(owner: String, repo: String): Single<List<User>> {
        return service.getRepoContributorsSingle(owner, repo)
            .doOnSuccess { logUsersSuccess(repo, it) }
            .doOnError { logUsersError(repo, it.toString()) }
    }

    class CallExecuteLogger<T>(private val call: Call<T>, private val logger: (Response<T>) -> Unit): Call<T> by call {
        override fun execute(): Response<T> {
            return call.execute()
                .also { logger.invoke(it) }
        }

        override fun enqueue(callback: Callback<T>) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    logger.invoke(response)
                    callback.onResponse(call, response)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.e("Call failed", t.toString())
                    callback.onFailure(call, t)
                }
            })
        }
    }
}