package com.githubcontributor.domain.usecase

import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.repository.UserRepository
import com.githubcontributor.domain.Variant
import javax.inject.Inject

class SaveParamsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun save(req: RequestData, variant: Variant) {
        userRepository.userName = req.username
        userRepository.token = req.password
        userRepository.organization = req.org
        userRepository.variant = variant
    }
}