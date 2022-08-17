package com.degtyarev.githubcontributor.data.storage.api

import android.util.Log
import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import retrofit2.Response

fun logRepos(organization: String, response: Response<List<Repo>>) {
    val repos: List<Repo>? = response.body()
    if (!response.isSuccessful || repos == null) {
        val responseErrorMessage = "${response.code()}: ${response.message()}"
        logReposError(organization, responseErrorMessage)
    } else {
        logReposSuccess(organization, repos)
    }
}

fun logReposError(organization: String, errorMessage: String) {
    Log.e(null, "Failed loading repos for $organization with response: '$errorMessage'")
}

fun logReposSuccess(organization: String, repos: List<Repo>) {
    Log.i(null, "$organization: loaded ${repos.size} repos")
}

fun logUsers(repo: String, response: Response<List<User>>) {
    val users: List<User>? = response.body()
    if (!response.isSuccessful || users == null) {
        val responseErrorMessage = "${response.code()}: ${response.message()}"
        logUsersError(repo, responseErrorMessage)
    } else {
        logUsersSuccess(repo, users)
    }
}

fun logUsersError(repo: String, responseErrorMessage: String) {
    Log.e(null, "Failed loading contributors for $repo with response '$responseErrorMessage'")
}

fun logUsersSuccess(repo: String, users: List<User>) {
    Log.i(null, "$repo: loaded ${users.size} contributors")
}