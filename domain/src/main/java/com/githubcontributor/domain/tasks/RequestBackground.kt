package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import kotlin.concurrent.thread

fun loadContributorsBackground(gitHubRepository: GitHubRepository, req: RequestData, updateResult: (List<User>) -> Unit) {
    thread {
        updateResult(loadContributorsBlocking(gitHubRepository, req))
    }
}

private fun loadContributorsBlocking(gitHubRepository: GitHubRepository, req: RequestData) : List<User> {
    val repos = gitHubRepository
        .getOrgReposCall(req.org)
        .execute() // Executes request and blocks the current thread
        .body() ?: listOf()

    return repos.flatMap { repo ->
        gitHubRepository
            .getRepoContributorsCall(req.org, repo.name)
            .execute() // Executes request and blocks the current thread
            .bodyList()
    }.aggregate()
}