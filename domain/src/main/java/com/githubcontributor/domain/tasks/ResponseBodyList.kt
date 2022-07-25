package com.githubcontributor.domain.tasks

import retrofit2.Response

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: listOf()
}