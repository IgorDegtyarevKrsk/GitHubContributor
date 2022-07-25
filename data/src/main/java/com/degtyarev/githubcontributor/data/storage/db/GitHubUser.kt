package com.degtyarev.githubcontributor.data.storage.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = USER_TABLE)
data class GitHubUser(
    @PrimaryKey val login: String,
    val contributions: Int
)