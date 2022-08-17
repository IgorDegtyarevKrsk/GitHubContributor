package com.githubcontributor.domain.tasks.rx

import com.githubcontributor.domain.contributors.*
import com.githubcontributor.domain.repository.GitHubRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class RequestRxProgressTest {

    private val repository: GitHubRepository = Mockito.mock(GitHubRepository::class.java)
    private val requestRxProgress = RequestRxProgress(repository)

    @Test
    fun test() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        val repoError = Throwable("Need some money for request")
        Mockito.`when`(repository.getOrgReposSingle(testRequestData.org))
            .thenReturn(Single.error(repoError))
            .thenReturn(reposSingle)

        requestRxProgress.loadContributorsRxProgress(testRequestData)
            .test()
            .assertValue(emptyList())

        for(repo in repos) {
            val answer = repo.getAnswer()
            Mockito.`when`(repository.getRepoContributorsSingle(testRequestData.org, repo.name))
                .thenReturn(answer)
        }

        var testObserver = requestRxProgress.loadContributorsRxProgress(testRequestData)
            .test()

        concurrentProgressResults.forEachIndexed { index, testResults ->
            testScheduler.advanceTimeTo(testResults.timeFromStart, TimeUnit.MILLISECONDS)
            testObserver.assertValueAt(index, testResults.users)
        }

        Mockito.`when`(repository.getRepoContributorsSingle(testRequestData.org, errorRepo.name))
            .thenReturn(Single.error(Throwable("Some error")))
        testObserver = requestRxProgress.loadContributorsRxProgress(testRequestData)
            .test()
        testScheduler.advanceTimeBy(totalTimes, TimeUnit.MILLISECONDS)
        testObserver.assertNoErrors()
    }

}