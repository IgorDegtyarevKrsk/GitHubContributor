package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.contributors.MockGithubService
import com.githubcontributor.domain.contributors.expectedResults
import com.githubcontributor.domain.contributors.testRequestData
import org.junit.Assert
import org.junit.Test

class RequestBackgroundKtTest {
    @Test
    fun testAggregation() {
        val users = loadContributorsBlocking(MockGithubService, testRequestData)
        Assert.assertEquals("List of contributors should be sorted " +
                "by the number of contributions in a descending order",
            expectedResults.users, users)
    }
}