package com.example.e_commerce.authentication.signin

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.authentication.viewmodel.AuthViewModel
import com.example.e_commerce.authentication.viewmodel.AuthViewModelFactory
import com.example.e_commerce.databinding.FragmentSignInBinding
import com.example.e_commerce.model.pojo.customer.Customer
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Functions.checkConnectivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    //TODO: Extract to a Singleton or Inject into Repo
    private lateinit var mAuth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var _viewModelFactory: AuthViewModelFactory
    private lateinit var _viewModel: AuthViewModel

    private val TAG = "SignInFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        _viewModelFactory = AuthViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireActivity())
            )
        )
        _viewModel =
            ViewModelProvider(requireActivity(), _viewModelFactory)[AuthViewModel::class.java]

        //TODO: Extract to External Method with the rest of Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignInToStartup.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignin.setOnClickListener {
            binding.groupSigninLoading.visibility = View.VISIBLE
            val email = binding.etSignInEmail.text.toString()
            val password = binding.etSignInPassword.text.toString()

            emailSignIn(email, password)
        }

        binding.btnGmailSignin.setOnClickListener {
            binding.groupSigninLoading.visibility = View.VISIBLE
            googleSignIn()
        }

        binding.tvSigninToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_passwordResetFragment)
        }

        return binding.root
    }

    private fun emailSignIn(email: String, password: String) {
        if (checkConnectivity(requireContext())) {
            if (email.isNotBlank() && password.isNotBlank()) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    requireActivity()
                ) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        showToast(getString(R.string.login_failed_please_check_your_email_and_password))
                        updateUI(null)
                    }
                }
            } else {
                showToast(getString(R.string.invalid_data_please_fill_in_all_the_fields))
            }
        } else {
            showToast(getString(R.string.no_internet_connection_please_check_your_network_settings))
        }
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
                val user = mAuth.currentUser
                var displayName = user?.displayName

                if (!displayName.isNullOrEmpty()) {
                    showToast(getString(R.string.welcome) + " $displayName")
                } else {
                    displayName = "Unknown"
                }

                val customerData = CustomerData(
                    Customer(
                        email = user!!.email!!, first_name = displayName
                    )
                )
                _viewModel.createNewCustomer(customerData)

                updateUI(user)
            } else {
                showToast(getString(R.string.google_verification_failed_please_try_again))
                updateUI(null)
            }
        }
    }

    private fun showToast(message: String) {
        binding.groupSigninLoading.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            if (user.isEmailVerified) {
                showToast(getString(R.string.welcome) + " ${user!!.displayName}")
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                showToast(getString(R.string.please_check_your_email_to_verify_your_account))
            }
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