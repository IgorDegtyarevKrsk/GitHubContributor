package com.githubcontributor.domain.usecase

import com.githubcontributor.domain.RequestVariant
import com.githubcontributor.domain.repository.UserRepository
import javax.inject.Inject

class GetSavedVariant @Inject constructor(
    private val userRepository: UserRepository
) {
    fun get(): RequestVariant = RequestVariant(
        userRepository.userName,
        userRepository.token,
        userRepository.organization,
        userRepository.variant
    )
}