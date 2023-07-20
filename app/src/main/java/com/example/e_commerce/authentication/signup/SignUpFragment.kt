package com.example.e_commerce.authentication.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentSignUpBinding
import com.example.e_commerce.utility.Functions.checkConnectivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    //TODO: Extract to a Singleton or Inject into Repo
    private lateinit var mAuth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val TAG = "SignUpFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        //TODO: Extract to External Method with the rest of Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUpToStartup.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignup.setOnClickListener {
            val name = binding.etSignUpName.text.toString()
            val email = binding.etSignUpEmail.text.toString()
            val password = binding.etSignUpPassword.text.toString()

            emailSignUp(name, email, password)
        }

        binding.btnGmailSignup.setOnClickListener {
            googleSignIn()
        }

        binding.tvSignupToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return binding.root
    }

    //TODO: Extract String

    private fun emailSignUp(name: String, email: String, password: String) {
        if (checkConnectivity(requireContext())) {
            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    requireActivity()
                ) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")

                        val profileUpdates =
                            UserProfileChangeRequest.Builder().setDisplayName(name).build()

                        mAuth.currentUser?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    mAuth.currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Registration successful. Please check your email to verify your account.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Registration failed. Please check your email and password.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Registration failed. Please check your email and password.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Registration failed. Please check your data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else Toast.makeText(
                requireContext(), "Invalid data. Please fill in all the fields.", Toast.LENGTH_SHORT
            ).show()
        } else Toast.makeText(
            requireContext(),
            "No internet connection. Please check your network settings.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(
            requireActivity()
        ) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential:success")
                val user = mAuth.currentUser
                updateUI(user)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Google verification failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    //TODO: Refactor this code so it can be used once
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if (data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
                        account?.idToken?.let { firebaseAuthWithGoogle(it) }
                    } catch (e: ApiException) {
                        Log.w(TAG, "Google sign in failed", e)
                    }
                }
            }
        }
}