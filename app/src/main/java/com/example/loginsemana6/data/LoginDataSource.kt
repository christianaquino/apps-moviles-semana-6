package com.example.loginsemana6.data

import com.example.loginsemana6.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private lateinit var result: Result<LoggedInUser>
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val auth = FirebaseAuth.getInstance();

        try {
            val loginResult = auth.signInWithEmailAndPassword(username, password).await()
            val user = loginResult.user
            val fakeUser = LoggedInUser(user.toString(), user.toString())
            result = Result.Success(fakeUser)
        } catch (e: Exception) {
            result = Result.Error(Exception("Login error"))
        }

        return result;
    }

    fun logout() {
        // TODO: revoke authentication
    }
}