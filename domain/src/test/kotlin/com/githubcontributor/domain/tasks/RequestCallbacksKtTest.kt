package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.contributors.MockGithubService
import com.githubcontributor.domain.contributors.expectedResults
import com.githubcontributor.domain.contributors.testRequestData
import org.junit.Assert
import org.junit.Test

class RequestCallbacksKtTest {
    @Test
    fun testDataIsLoaded() {
        loadContributorsCallbacks(MockGithubService, testRequestData) {
            Assert.assertEquals(
                "Wrong result for 'loadContributorsCallbacks'",
                expectedResults.users, it
            )
        }
    }
}