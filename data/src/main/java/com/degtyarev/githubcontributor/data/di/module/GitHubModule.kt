package com.degtyarev.githubcontributor.data.di.module

import com.degtyarev.githubcontributor.data.repository.GitHubRepositoryImpl
import com.degtyarev.githubcontributor.data.storage.GitHubService
import com.degtyarev.githubcontributor.data.storage.api.GithubServiceLogger
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.repository.UserRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.*

@Module
class GitHubModule {

    @Provides
    fun provideGitHubService(userRepository: UserRepository): GitHubService {
        val userName = userRepository.userName
        val token = userRepository.token
        return createGitHubService(userName, token)
    }

    private fun createGitHubService(username: String, password: String): GitHubService {
        val authToken = "Basic " + Base64.getEncoder().encode("$username:$password".toByteArray()).toString(Charsets.UTF_8)
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", authToken)
                val request = builder.build()
                chain.proceed(request)
            }
            .build()

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient)
            .build()
        val create = retrofit.create(GitHubService::class.java)
        return GithubServiceLogger(create)
    }

    @Provides
    fun provideGitHubRepository(gitHubService: GitHubService): GitHubRepository = GitHubRepositoryImpl(gitHubService)

}