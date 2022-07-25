package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsConcurrent(gitHubRepository: GitHubRepository, req: RequestData): List<User> = coroutineScope {
    val repos = gitHubRepository
        .getOrgRepos(req.org)
        .bodyList()

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        async {
            gitHubRepository.getRepoContributors(req.org, repo.name)
                .bodyList()
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}