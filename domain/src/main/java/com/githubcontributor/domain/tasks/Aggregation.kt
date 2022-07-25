package com.githubcontributor.domain.tasks

import com.githubcontributor.domain.User

internal fun List<User>.aggregate(): List<User> =
    groupBy { it.login }
        .map { (login, group) -> User(login, group.sumOf { it.contributions }) }
        .sortedByDescending { it.contributions }