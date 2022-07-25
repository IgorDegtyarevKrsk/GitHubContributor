package com.degtyarev.githubcontributor.data.storage.prefs

import android.content.Context
import com.degtyarev.githubcontributor.data.storage.UserDataSource
import com.githubcontributor.domain.Variant

private const val SHARED_PREFS_NAME = "UserSharedPrefsName"

private const val KEY_USER_NAME = "user_name"
private const val KEY_PASSWORD_OR_TOKEN = "password_or_token"
private const val KEY_ORGANIZATION = "organization"
private const val KEY_VARIANT = "variant"

class UserDataSourceImpl(
    context: Context
): UserDataSource {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun getGithubUserName(): String = getStringOrEmpty(KEY_USER_NAME)
    override fun getPasswordOrToken(): String = getStringOrEmpty(KEY_PASSWORD_OR_TOKEN)
    override fun getOrganization(): String = getStringOrEmpty(KEY_ORGANIZATION)

    override fun getVariant(): Variant {
        val defaultVariantString: String = Variant.BACKGROUND.toString()
        val indexVariant = sharedPreferences.getString(KEY_VARIANT, defaultVariantString) ?: defaultVariantString
        return Variant.valueOf(indexVariant)
    }

    override fun saveGithubUserName(newName: String): Unit = editString(KEY_USER_NAME, newName)
    override fun savePassword(value: String): Unit = editString(KEY_PASSWORD_OR_TOKEN, value)
    override fun saveOrganization(value: String): Unit = editString(KEY_ORGANIZATION, value)
    override fun saveVariant(variant: Variant) {
        sharedPreferences.edit()
            .putString(KEY_VARIANT, variant.toString())
            .apply()
    }

    private fun getStringOrEmpty(key: String): String = sharedPreferences.getString(key, "") ?: ""
    private fun editString(key: String, value: String) = sharedPreferences.edit()
        .putString(key, value)
        .apply()

}