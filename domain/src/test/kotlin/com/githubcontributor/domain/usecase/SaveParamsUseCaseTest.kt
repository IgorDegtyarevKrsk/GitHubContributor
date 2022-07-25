package com.githubcontributor.domain.usecase

import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.Variant
import com.githubcontributor.domain.repository.UserRepository
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class SaveParamsUseCaseTest {

    private val userRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    private val saveParamsUseCase: SaveParamsUseCase = SaveParamsUseCase(userRepository)

    @Test
    fun save() {
        val username = "userName"
        val password = "password"
        val org = "org"
        val variant = Variant.BACKGROUND
        saveParamsUseCase.save(
            RequestData(username, password, org),
            variant
        )
        Mockito.verify(userRepository).userName = username
        Mockito.verify(userRepository).token = password
        Mockito.verify(userRepository).organization = org
        Mockito.verify(userRepository).variant = variant
    }
}