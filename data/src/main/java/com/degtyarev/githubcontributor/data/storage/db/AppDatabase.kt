package com.degtyarev.githubcontributor.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.degtyarev.githubcontributor.data.storage.GitHubUserDao

@Database(entities = [GitHubUser::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): GitHubUserDao
}