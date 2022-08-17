package com.githubcontributor.domain.tasks.rx

import com.githubcontributor.domain.Repo
import com.githubcontributor.domain.User
import com.githubcontributor.domain.contributors.repos
import com.githubcontributor.domain.contributors.reposDelay
import com.githubcontributor.domain.contributors.reposMap
import com.githubcontributor.domain.contributors.testRepos
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

val reposSingle = Single.just(repos)
    .delay(reposDelay, TimeUnit.MILLISECONDS)

val errorRepo = repos.first()

fun Repo.getAnswer(): Single<List<User>> {
    val testRepo = reposMap.get(this.name)!!
    return Single.just(testRepo.users)
        .delay(testRepo.delay, TimeUnit.MILLISECONDS)
}

val totalTimes = reposDelay + testRepos.map { it.delay }.max()