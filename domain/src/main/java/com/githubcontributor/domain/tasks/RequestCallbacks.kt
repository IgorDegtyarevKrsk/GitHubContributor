package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

fun loadContributorsCallbacks(gitHubRepository: GitHubRepository, req: RequestData, updateResults: (List<User>) -> Unit) {
    gitHubRepository.getOrgReposCall(req.org)
        .onResponse { responseRepos ->
            val repos = responseRepos.bodyList()
            val allUsers = Collections.synchronizedList(mutableListOf<User>())
            val count = AtomicInteger(0)
            for(repo in repos) {
                gitHubRepository.getRepoContributorsCall(req.org, repo.name).onResponse { responseUsers ->
                    val users = responseUsers.bodyList()
                    allUsers += users
                    val countValue = count.addAndGet(1)
                    if (countValue == repos.size) {
                        updateResults(allUsers.aggregate())
                    }
                }
            }
        }
}

inline fun <T> Call<T>.onResponse(crossinline  callback: (Response<T>) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {

        }
    })
}