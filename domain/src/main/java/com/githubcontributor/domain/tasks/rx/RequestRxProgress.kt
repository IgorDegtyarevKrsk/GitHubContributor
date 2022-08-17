package com.githubcontributor.domain.tasks.rx

import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.User
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.tasks.aggregate
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RequestRxProgress @Inject constructor(
    private val gitHubRepository: GitHubRepository
) {

    fun loadContributorsRxProgress(req: RequestData): Observable<List<User>> {
        return gitHubRepository.getOrgReposSingle(req.org)
            .onErrorResumeNext { Single.just(emptyList()) }
            .flatMapObservable { repos ->
                if (repos.isEmpty()) {
                    Observable.just(emptyList())
                } else {
                    val repoContributorsSingles = mutableListOf<Observable<List<User>>>()
                    repos.forEach { repo ->
                        val repoContributorsSingle = gitHubRepository.getRepoContributorsSingle(req.org, repo.name)
                            .onErrorResumeNext { Single.just(emptyList()) }
                            .toObservable()
                            .startWith(Observable.just(emptyList()))
                        repoContributorsSingles.add(repoContributorsSingle)
                    }
                    Observable.combineLatest(repoContributorsSingles) { outs: Array<Any> ->
                        val listUsers = mutableListOf<User>()
                        outs.forEach { any ->
                            listUsers.addAll(any as List<User>)
                        }
                        listUsers.aggregate()
                    }.filter { predicate -> predicate.isNotEmpty() }
                }
            }
    }

}