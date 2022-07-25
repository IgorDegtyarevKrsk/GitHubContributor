package com.githubcontributor.domain.usecase

import com.githubcontributor.domain.repository.UserRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getToken(): String = userRepository.token
}