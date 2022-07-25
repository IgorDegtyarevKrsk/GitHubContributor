package com.githubcontributor.domain.repository

import com.githubcontributor.domain.Variant

interface UserRepository {
    var userName: String
    var token: String
    var organization: String
    var variant: Variant
}