package com.degtyarev.githubcontributor.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.degtyarev.githubcontributor.data.storage.db.GitHubUser
import com.degtyarev.githubcontributor.data.storage.db.USER_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface GitHubUserDao {
    @Query("SELECT * FROM $USER_TABLE")
    fun getAllFlow(): Flow<List<GitHubUser>>

    @Query("DELETE FROM $USER_TABLE")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<GitHubUser>)
}