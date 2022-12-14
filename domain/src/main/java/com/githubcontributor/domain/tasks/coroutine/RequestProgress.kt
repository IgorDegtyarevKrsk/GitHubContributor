package com.githubcontributor.domain.tasks.coroutine

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import com.githubcontributor.domain.tasks.aggregate
import com.githubcontributor.domain.tasks.bodyList

suspend fun loadContributorsProgress(
    gitHubRepository: GitHubRepository,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = gitHubRepository
        .getOrgRepos(req.org)
        .bodyList()

    var allUsers = emptyList<User>()
    for((index, repo) in repos.withIndex()) {
        val users = gitHubRepository.getRepoContributors(req.org, repo.name)
            .bodyList()

        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers, index == repos.lastIndex)
    }
}