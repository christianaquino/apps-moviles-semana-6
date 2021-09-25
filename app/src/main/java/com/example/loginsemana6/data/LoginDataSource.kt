package com.example.loginsemana6.data

import com.example.loginsemana6.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private lateinit var result: Result<LoggedInUser>
    private var auth = FirebaseAuth.getInstance()

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        result = try {
            val loginResult = auth.signInWithEmailAndPassword(username, password).await()
            val authUser = loginResult.user
            val loggedInUser = LoggedInUser(authUser?.uid.toString(), authUser?.email.toString())
            Result.Success(loggedInUser)
        } catch (e: Exception) {
            Result.Error(Exception("Login error"))
        }

        return result
    }

    fun logout() {
        auth.signOut()
    }
}