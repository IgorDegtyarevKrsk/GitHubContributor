package com.githubcontributor.domain

import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val id: Long,
    val name: String
)

data class RequestData(
    val username: String,
    val password: String,
    val org: String
)

data class RequestVariant(
    val username: String,
    val password: String,
    val org: String,
    val variant: Variant
)

@Serializable
data class User(
    val login: String,
    val contributions: Int
)

enum class Variant {
    BACKGROUND,       // RequestBackground
    CALLBACKS,        // RequestCallbacks
    SUSPEND,          // RequestCoroutine // Using coroutines
    CONCURRENT,       // RequestConcurrent // Performing requests concurrently
    NOT_CANCELLABLE,  // RequestNotCancellable // Performing requests in a non-cancellable way
    PROGRESS,         // RequestProgress // Showing progress
    CHANNELS,          // RequestChannels // Performing requests concurrently and showing progress
    Rx,
    RxProgress
}