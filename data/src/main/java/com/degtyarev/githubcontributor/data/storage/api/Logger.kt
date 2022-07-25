package com.degtyarev.githubcontributor.data.storage.api

import android.util.Log
import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import retrofit2.Response

fun logRepos(organization: String, response: Response<List<Repo>>) {
    val repos = response.body()
    if (!response.isSuccessful || repos == null) {
        Log.e(null, "Failed loading repos for $organization with response: '${response.code()}: ${response.message()}'")
    }
    else {
        Log.i(null, "$organization: loaded ${repos.size} repos")
    }
}

fun logUsers(repo: String, response: Response<List<User>>) {
    val users = response.body()
    if (!response.isSuccessful || users == null) {
        Log.e(null, "Failed loading contributors for $repo with response '${response.code()}: ${response.message()}'")
    }
    else {
        Log.i(null, "$repo: loaded ${users.size} contributors")
    }
}