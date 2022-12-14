package com.githubcontributor.domain.tasks.coroutine

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import com.githubcontributor.domain.tasks.aggregate
import com.githubcontributor.domain.tasks.bodyList
import kotlinx.coroutines.*


@OptIn(DelicateCoroutinesApi::class)
suspend fun loadContributorsNotCancellable(gitHubRepository: GitHubRepository, req: RequestData): List<User> {
    val repos = gitHubRepository
        .getOrgRepos(req.org)
        .bodyList()

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        GlobalScope.async(Dispatchers.Default) {
            delay(3000)
            gitHubRepository.getRepoContributors(req.org, repo.name)
                .bodyList()
        }
    }
    return deferreds.awaitAll().flatten().aggregate()
}