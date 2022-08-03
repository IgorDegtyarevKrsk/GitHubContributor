package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.contributors.MockGithubService
import com.githubcontributor.domain.contributors.expectedConcurrentResults
import com.githubcontributor.domain.contributors.testRequestData
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RequestConcurrentKtTest {
    @Test
    fun testConcurrent() = runTest {
        val startTime = currentTime
        val result = loadContributorsConcurrent(MockGithubService, testRequestData)
        Assert.assertEquals("Wrong result for 'loadContributorsConcurrent'", expectedConcurrentResults.users, result)
        val totalTime = currentTime - startTime
        Assert.assertEquals(
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
            "1000 ms for repos request plux max (1000, 1200, 800) = 1200 ms for concurrent contributors requests",
            expectedConcurrentResults.timeFromStart, totalTime
        )
    }
}