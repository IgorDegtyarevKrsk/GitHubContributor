package com.githubcontributor.domain.tasks.rx

import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.tasks.aggregate
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RequestRxSequence @Inject constructor(
    private val gitHubRepository: GitHubRepository
) {
   fun loadContributorsRxSequence(req: RequestData): Single<List<User>> {
       val repos = gitHubRepository
           .getOrgReposSingle(req.org)
           .onErrorResumeNext {
               Single.just(emptyList())
           }

       return repos.flatMap { reposList: List<Repo> ->
           if (reposList.isNotEmpty()) {
               val singleUsers: List<Single<List<User>>> = getSingleUsers(reposList, req)
               Single.zip(singleUsers) { outs: Array<Any> ->
                   val listUsers = mutableListOf<User>()
                   outs.forEach { any ->
                       listUsers.addAll(any as List<User>)
                   }
                   listUsers.aggregate()
               }
           } else Single.just(emptyList())
       }
   }

    private fun getSingleUsers(
        reposList: List<Repo>,
        req: RequestData
    ): List<Single<List<User>>> {
        val result = mutableListOf<Single<List<User>>>()
        reposList.forEach {
            val repoContributorsSingle = gitHubRepository.getRepoContributorsSingle(req.org, it.name)
                .onErrorResumeNext { Single.just(emptyList()) }
            result.add(repoContributorsSingle)
        }
        return result
    }
}