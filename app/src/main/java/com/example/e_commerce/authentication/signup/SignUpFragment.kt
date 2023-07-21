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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.authentication.viewmodel.AuthViewModel
import com.example.e_commerce.authentication.viewmodel.AuthViewModelFactory
import com.example.e_commerce.databinding.FragmentSignUpBinding
import com.example.e_commerce.model.pojo.customer.Customer
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Functions.checkConnectivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    //TODO: Extract to a Singleton or Inject into Repo
    private lateinit var mAuth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val TAG = "SignUpFragment"

    private lateinit var _viewModelFactory: AuthViewModelFactory
    private lateinit var _viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

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

    private fun emailSignUp(name: String, email: String, password: String) {
        binding.groupSignupLoading.visibility = View.VISIBLE

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
                            ?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    mAuth.currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener { verificationTask ->
                                            if (verificationTask.isSuccessful) {
                                                val customerData = CustomerData(
                                                    Customer(
                                                        email = email, first_name = name
                                                    )
                                                )
                                                _viewModel.createNewCustomer(customerData)
                                                lifecycleScope.launch {
                                                    _viewModel.customerMutableStateFlow.collectLatest {

                                                        when (it) {
                                                            is ApiState.Success -> {
                                                                showToast(getString(R.string.registration_successful_please_check_your_email_to_verify_your_account))
                                                                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                                                            }

                                                            else -> {
                                                                showToast(getString(R.string.registration_failed_please_check_your_email_and_password))
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                showToast(getString(R.string.registration_failed_please_check_your_email_and_password))
                                            }
                                        }
                                } else {
                                    showToast(getString(R.string.registration_failed_please_check_your_email_and_password))
                                }
                            }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        showToast(getString(R.string.registration_failed_please_check_your_data))
                    }
                }
            } else {
                showToast(getString(R.string.invalid_data_please_fill_in_all_the_fields))
            }
        } else {
            showToast(getString(R.string.no_internet_connection_please_check_your_network_settings))
        }
    }

    private fun showToast(message: String) {
        binding.groupSignupLoading.visibility = View.GONE

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                }else {
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