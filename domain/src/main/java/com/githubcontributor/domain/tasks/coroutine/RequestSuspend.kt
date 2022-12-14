package com.githubcontributor.domain.tasks.coroutine

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import com.githubcontributor.domain.tasks.aggregate
import com.githubcontributor.domain.tasks.bodyList

suspend fun loadContributorsSuspend(gitHubRepository: GitHubRepository, req: RequestData): List<User> {
    val repos = gitHubRepository
        .getOrgRepos(req.org)
        .bodyList()

    return repos.flatMap { repo ->
        gitHubRepository.getRepoContributors(req.org, repo.name)
            .bodyList()
    }.aggregate()
}