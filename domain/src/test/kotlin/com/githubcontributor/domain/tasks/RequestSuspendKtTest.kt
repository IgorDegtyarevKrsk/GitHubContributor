package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.contributors.MockGithubService
import com.githubcontributor.domain.contributors.expectedResults
import com.githubcontributor.domain.contributors.testRequestData
import com.githubcontributor.domain.tasks.coroutine.loadContributorsSuspend
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RequestSuspendKtTest {
    @Test
    fun testSuspend() = runTest {
        val startTime = currentTime
        val result = loadContributorsSuspend(MockGithubService, testRequestData)
        Assert.assertEquals("Wrong result for 'loadContributorSuspend'", expectedResults.users, result)
        val totalTime = currentTime - startTime
        Assert.assertEquals(
            "The calls run consequently," +
            "so the total virtual time should be 4000 ms: ",
            expectedResults.timeFromStart, totalTime
        )
    }
}