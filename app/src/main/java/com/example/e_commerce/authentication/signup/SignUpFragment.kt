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
import com.example.e_commerce.model.pojo.customer_modified_response.CustomerModifiedResponse
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftOrder
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.draftorder.send.SendLineItem
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants.CART_KEY
import com.example.e_commerce.utility.Constants.CUSTOMER_ID_KEY
import com.example.e_commerce.utility.Constants.WISHLIST_KEY
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

    private lateinit var cartID: String
    private lateinit var wishlistID: String

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
            binding.groupSignupLoading.visibility = View.VISIBLE
            val name = binding.etSignUpName.text.toString()
            val email = binding.etSignUpEmail.text.toString()
            val password = binding.etSignUpPassword.text.toString()

            emailSignUp(name, email, password)
        }

        binding.btnGmailSignup.setOnClickListener {
            binding.groupSignupLoading.visibility = View.VISIBLE
            googleSignIn()
        }

        binding.tvSignupToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        lifecycleScope.launch {
            _viewModel.createDraftStatusStateFlow.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        val draftResponse: DraftResponse = it.data as DraftResponse
                        if (draftResponse.draft_order.note == CART_KEY) {
                            cartID = draftResponse.draft_order.id.toString()
                            _viewModel.createDraftOrder(
                                SendDraftRequest(
                                    SendDraftOrder(
                                        listOf(SendLineItem(45786113737003, 1, listOf())),
                                        it.data.draft_order.email,
                                        WISHLIST_KEY
                                    )
                                )
                            )
                        } else if (draftResponse.draft_order.note == WISHLIST_KEY) {
                            wishlistID = draftResponse.draft_order.id.toString()
                            _viewModel.modifyCustomer(
                                draftResponse.draft_order.customer.id, CustomerData(
                                    Customer(
                                        email = draftResponse.draft_order.email,
                                        first_name = draftResponse.draft_order.customer.first_name,
                                        note = cartID,
                                        tags = wishlistID
                                    )
                                )
                            )
                            _viewModel.modifyCustomerMutableStateFlow.collectLatest { customerState ->
                                when (customerState) {
                                    is ApiState.Success -> {
                                        val customerResponse: CustomerModifiedResponse =
                                            customerState.data as CustomerModifiedResponse
                                        _viewModel.writeStringToSettingSP(
                                            CART_KEY, cartID
                                        )
                                        _viewModel.writeStringToSettingSP(
                                            WISHLIST_KEY, wishlistID
                                        )
                                        _viewModel.writeStringToSettingSP(
                                            CUSTOMER_ID_KEY,
                                            customerResponse.customer.id.toString()
                                        )
                                        showToast(getString(R.string.registration_successful_please_check_your_email_to_verify_your_account))
                                        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                                    }

                                    is ApiState.Failure -> {
                                        showToast(getString(R.string.registration_failed_please_check_your_email_and_password))
                                    }

                                    is ApiState.Loading -> {}
                                }
                            }

                        }
                    }

                    is ApiState.Failure -> {
                        showToast(getString(R.string.registration_failed_please_check_your_email_and_password))
                    }

                    is ApiState.Loading -> {}
                }
            }
        }

        return binding.root
    }

    private fun emailSignUp(name: String, email: String, password: String) {
        if (!checkConnectivity(requireContext())) {
            showToast(getString(R.string.no_internet_connection_please_check_your_network_settings))
            return
        }

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            showToast(getString(R.string.invalid_data_please_fill_in_all_the_fields))
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
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
                                            _viewModel.createNewEmailCustomer(customerData)
                                            lifecycleScope.launch {
                                                _viewModel.emailCustomerMutableStateFlow.collectLatest { state ->
                                                    when (state) {
                                                        is ApiState.Success -> {
                                                            _viewModel.createDraftOrder(
                                                                SendDraftRequest(
                                                                    SendDraftOrder(
                                                                        listOf(
                                                                            SendLineItem(
                                                                                45786113737003,
                                                                                1,
                                                                                listOf()
                                                                            )
                                                                        ), email, CART_KEY
                                                                    )
                                                                )
                                                            )
                                                        }

                                                        is ApiState.Failure -> {
                                                            showToast(getString(R.string.registration_failed_please_check_your_email_and_password))
                                                        }

                                                        is ApiState.Loading -> {
                                                            // Handle loading if needed
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
                if (user != null) {
                    var displayName = user.displayName

                    if (displayName.isNullOrEmpty()) {
                        displayName = "Unknown"
                        showToast(getString(R.string.welcome) + " $displayName")
                    }

                    val customerData = CustomerData(
                        Customer(
                            email = user.email!!, first_name = displayName
                        )
                    )
                    _viewModel.createGmailEmailCustomer(customerData)

                    lifecycleScope.launch {
                        _viewModel.gmailCustomerMutableStateFlow.collectLatest {
                            when (it) {
                                is ApiState.Success -> {
                                    _viewModel.createGoogleDraftOrder(
                                        SendDraftRequest(
                                            SendDraftOrder(
                                                listOf(
                                                    SendLineItem(
                                                        45786113737003, 1, listOf()
                                                    )
                                                ), user.email!!, CART_KEY
                                            )
                                        )
                                    )

                                }

                                is ApiState.Failure -> {
                                    _viewModel.getCustomerData(user.email!!, user.displayName!!)

                                    _viewModel.loggedCustomerStateFlow.collectLatest { customerState ->
                                        when (customerState) {
                                            is ApiState.Success -> {
                                                val customerResponse: CustomerResponse =
                                                    customerState.data as CustomerResponse
                                                cartID = customerResponse.customers[0].note
                                                wishlistID = customerResponse.customers[0].tags

                                                _viewModel.writeStringToSettingSP(
                                                    CART_KEY, cartID
                                                )
                                                _viewModel.writeStringToSettingSP(
                                                    WISHLIST_KEY, wishlistID
                                                )
                                                _viewModel.writeStringToSettingSP(
                                                    CUSTOMER_ID_KEY,
                                                    customerResponse.customers[0].id.toString()
                                                )

                                                if (cartID.isNotBlank()) {
                                                    showToast(getString(R.string.welcome) + " $displayName")
                                                    updateUI(user)
                                                }else{
                                                    showToast(getString(R.string.google_verification_failed_please_try_again))
                                                }
                                            }

                                            is ApiState.Failure -> {
                                                showToast(getString(R.string.google_verification_failed_please_try_again))
                                            }

                                            is ApiState.Loading -> {}
                                        }
                                    }
                                }

                                is ApiState.Loading -> {
                                    // Handle loading if needed
                                }
                            }
                        }
                    }

                    lifecycleScope.launch {
                        _viewModel.createGoogleDraftStatusStateFlow.collectLatest {
                            when (it) {
                                is ApiState.Success -> {
                                    val draftResponse: DraftResponse = it.data as DraftResponse
                                    if (draftResponse.draft_order.note == CART_KEY) {
                                        cartID = draftResponse.draft_order.id.toString()
                                        _viewModel.createGoogleDraftOrder(
                                            SendDraftRequest(
                                                SendDraftOrder(
                                                    listOf(
                                                        SendLineItem(
                                                            45786113737003, 1, listOf()
                                                        )
                                                    ), user.email!!, WISHLIST_KEY
                                                )
                                            )
                                        )
                                    } else if (draftResponse.draft_order.note == WISHLIST_KEY) {
                                        wishlistID = draftResponse.draft_order.id.toString()

                                        Log.i(TAG, "firebaseAuthWithGoogle: $cartID")
                                        Log.i(TAG, "firebaseAuthWithGoogle: $wishlistID")

                                        _viewModel.modifyGoogleCustomer(
                                            draftResponse.draft_order.customer.id, CustomerData(
                                                Customer(
                                                    email = draftResponse.draft_order.email,
                                                    first_name = draftResponse.draft_order.customer.first_name,
                                                    note = cartID,
                                                    tags = wishlistID
                                                )
                                            )
                                        )
                                        _viewModel.modifyGoogleCustomerMutableStateFlow.collectLatest { customerState ->
                                            when (customerState) {
                                                is ApiState.Success -> {
                                                    val customerResponse: CustomerModifiedResponse =
                                                        customerState.data as CustomerModifiedResponse
                                                    _viewModel.writeStringToSettingSP(
                                                        CART_KEY, cartID
                                                    )
                                                    _viewModel.writeStringToSettingSP(
                                                        WISHLIST_KEY, wishlistID
                                                    )
                                                    _viewModel.writeStringToSettingSP(
                                                        CUSTOMER_ID_KEY,
                                                        customerResponse.customer.id.toString()
                                                    )
                                                    showToast(getString(R.string.welcome) + " $displayName")
                                                    updateUI(user)
                                                }

                                                is ApiState.Failure -> {
                                                    showToast(getString(R.string.google_verification_failed_please_try_again))
                                                }

                                                is ApiState.Loading -> {}
                                            }
                                        }
                                    }
                                }

                                is ApiState.Failure -> {
                                    showToast(getString(R.string.google_verification_failed_please_try_again))
                                }

                                is ApiState.Loading -> {
                                    // Handle loading if needed
                                }
                            }
                        }
                    }

                } else {
                    showToast(getString(R.string.google_verification_failed_please_try_again))
                }
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
                        account?.idToken?.let { firebaseAuthWithGoogle(it) }
                    } catch (e: ApiException) {
                        Log.w(TAG, "Google sign in failed", e)
                    }
                }
            }
        }
}