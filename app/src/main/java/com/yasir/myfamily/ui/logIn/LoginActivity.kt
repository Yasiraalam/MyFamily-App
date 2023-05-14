package com.yasir.myfamily.ui.logIn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myfamily.R
import com.example.myfamily.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yasir.myfamily.constants.Constants.IS_USER_LOGED_IN
import com.yasir.myfamily.sharedPrefrences.SharedPref
import com.yasir.myfamily.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var GoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN =100
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signInButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            GoogleSignInClient = GoogleSignIn.getClient(this,gso)
            signIn()

        }
    }
    private fun signIn() {
        val signInIntent = GoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("Fire", "FirebaseAuthWith-google : "+account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.d("Fire", "Google Signing failed ",e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken:String){
        val firebaseAuth =  FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task->
                if(task.isSuccessful){
                    Log.d("Fire", "signInWithCredential :Success: ")
                    SharedPref.putBoolean(IS_USER_LOGED_IN,true)
                    val username = firebaseAuth.currentUser
                    Log.d("CurrUserName", "firebaseAuthWithGoogle: ${username?.displayName}")
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    Log.d("Fire", "signInWithCredential: failure ")
                }
            }
    }

}