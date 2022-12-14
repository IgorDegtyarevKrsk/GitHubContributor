package com.githubcontributor.domain.tasks.coroutine

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import com.githubcontributor.domain.tasks.aggregate
import com.githubcontributor.domain.tasks.bodyList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun loadContributorsChannels(
    gitHubRepository: GitHubRepository,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) = coroutineScope {
    val repos = gitHubRepository
        .getOrgRepos(req.org)
        .bodyList()

    val channel = Channel<List<User>>()
    for(repo in repos) {
        launch {
            val users = gitHubRepository.getRepoContributors(req.org, repo.name)
                .bodyList()
            channel.send(users)
        }
    }
    var allUsers = emptyList<User>()
    repeat(repos.size) {
        val users = channel.receive()
        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers, it == repos.lastIndex)
    }
}