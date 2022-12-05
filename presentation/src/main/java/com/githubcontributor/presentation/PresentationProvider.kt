package com.githubcontributor.presentation

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.presentation.ui.MainActivity

interface PresentationProvider {
    fun getGithubRepository(): GitHubRepository
    fun inject(mainActivity: MainActivity)
}