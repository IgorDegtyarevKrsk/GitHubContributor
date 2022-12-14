package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.contributors.MockGithubService
import com.githubcontributor.domain.contributors.progressResults
import com.githubcontributor.domain.contributors.testRequestData
import com.githubcontributor.domain.tasks.coroutine.loadContributorsProgress
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RequestProgressKtTest {

    @Test
    fun testProgress() = runTest {
        val startTime = currentTime
        var index = 0
        loadContributorsProgress(MockGithubService, testRequestData) {
            users, _ ->
            val expected = progressResults[index++]
            val time = currentTime - startTime
            Assert.assertEquals("Expected intermediate result after virtual ${expected.timeFromStart} ms: ",
                expected.timeFromStart, time)
            Assert.assertEquals("Wrong intermediate result after $time:", expected.users, users)
        }
    }

}