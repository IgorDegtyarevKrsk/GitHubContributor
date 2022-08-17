package com.githubcontributor.domain.tasks.rx

import com.githubcontributor.domain.User
import com.githubcontributor.domain.contributors.*
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.tasks.aggregate
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class RequestRxSequenceTest {

    private val gitHubRepository = Mockito.mock(GitHubRepository::class.java)
    private val requestRxSequence = RequestRxSequence(gitHubRepository)

    @Test
    fun testRxSequence() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        Mockito.`when`(gitHubRepository.getOrgReposSingle(testRequestData.org))
            .thenReturn(Single.error(Throwable("org repo error")))
            .thenReturn(reposSingle)

        requestRxSequence.loadContributorsRxSequence(testRequestData)
            .test()
            .assertValue(emptyList())

        for(repo in repos) {
            val answer = repo.getAnswer()
            if (repo.name == errorRepo.name) {
                Mockito.`when`(gitHubRepository.getRepoContributorsSingle(testRequestData.org, repo.name))
                    .thenReturn(Single.error(Throwable("Repo Contributor error")))
                    .thenReturn(answer)
            } else {
                Mockito.`when`(gitHubRepository.getRepoContributorsSingle(testRequestData.org, repo.name))
                    .thenReturn(answer)
            }
        }

        val users: List<List<User>> =
            reposMap
                .filter { predicate -> predicate.value.name != errorRepo.name }
                .map { it.value.users }
        val answer = users.flatten().aggregate()
        val test = requestRxSequence.loadContributorsRxSequence(testRequestData)
            .test()
        testScheduler.advanceTimeBy(totalTimes, TimeUnit.MILLISECONDS)
        test.assertValue(answer)

        val testObserver = requestRxSequence
            .loadContributorsRxSequence(testRequestData)
            .test()

        testScheduler.advanceTimeBy(totalTimes, TimeUnit.MILLISECONDS)

        testObserver.assertComplete()
        testObserver.assertValues(expectedResults.users)
    }

}