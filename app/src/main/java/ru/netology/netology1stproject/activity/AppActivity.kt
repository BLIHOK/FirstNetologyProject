package ru.netology.netology1stproject.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.netology1stproject.FCMService
import ru.netology.netology1stproject.activity.NewPostFragment.Companion.textArg
import ru.netology.netology1stproject.R
import ru.netology.netology1stproject.databinding.ActivityAppBinding


class AppActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppBinding.inflate(layoutInflater)

        setContentView(binding.root)


        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }
                    .show()
                return@let
            }

            findNavController(R.id.nav_host).navigate(
                R.id.action_feedFragment_to_newPostFragment2,
                Bundle().apply {
                    textArg = text
                }
            )
        }


        getFCMToken()
        checkGoogleapiAvailability()

    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get new FCM registration token
                val token = task.result
                Log.d("MainActivity", "FCM Token: $token")
                // You can send the token to your server if needed
            } else {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
            }
        }
    }
    private fun checkGoogleapiAvailability(){
        with(GoogleApiAvailability.getInstance()){
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code ==ConnectionResult.SUCCESS) {
                return@with
            }
            if(isUserResolvableError(code)){
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, "Google Api Unavailable", Toast.LENGTH_SHORT).show()
        }
    }
}