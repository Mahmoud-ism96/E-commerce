package com.example.e_commerce.checkout.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentCheckoutBinding
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.customer_resposnse.Addresse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.response.LineItem
import com.example.e_commerce.model.pojo.order.DiscountCode
import com.example.e_commerce.model.pojo.order.Order
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModelFactory
import com.example.e_commerce.utility.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var shareViewModelWithCart: CartViewModel
    private var email: String? = null
    private var discountCode: String = ""
    private var discountValue: String = ""
    private lateinit var checkOutAdapter: CheckOutAdapter
    private lateinit var currentAddresses: List<Addresse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackCheckOut.setOnClickListener {
            findNavController().popBackStack()
        }

        val factory = CartViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        shareViewModelWithCart =
            ViewModelProvider(requireActivity(), factory)[CartViewModel::class.java]

        binding.tvUserName.text = FirebaseAuth.getInstance().currentUser!!.displayName

        shareViewModelWithCart.getAddressesForCustomer(
            shareViewModelWithCart.readStringFromSettingSP(
                Constants.CUSTOMER_ID_KEY
            )
        )
        lifecycleScope.launch {
            shareViewModelWithCart.addressesStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        val response = it.data as AddressResponse
                        currentAddresses = response.addresses
                        setAddress(response.addresses)
                    }

                    is ApiState.Failure -> {}
                }
            }
        }

        checkOutAdapter = CheckOutAdapter()
        binding.rvCartItemsPayment.adapter = checkOutAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        email = firebaseAuth.currentUser?.email

        shareViewModelWithCart.getDraftOrderByDraftId(
            shareViewModelWithCart.readStringFromSettingSP(
                Constants.CART_KEY
            ).toLong()
        )

        binding.btnEditAddress.setOnClickListener {

        }
        binding.tvUserAddress.setOnClickListener {

        }

        binding.paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_paypal) {
                showPaypalTest()
            }
        }

        lifecycleScope.launch {
            shareViewModelWithCart.cartDraftOrderStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {

                    }

                    is ApiState.Success -> {
                        createListForAdapter(it.data as DraftResponse)
                        calculateTotal(it.data.draft_order.line_items)
                    }

                    is ApiState.Failure -> {

                    }
                }
            }
        }
        binding.btnPlaceOrder.setOnClickListener {
            lifecycleScope.launch {
                val lineItem = listOf(
                    com.example.e_commerce.model.pojo.order.LineItem(
                        quantity = 1,
                        variant_id = 45786104529195
                    )
                )
                val order =
                    Order(email!!, lineItem, listOf(DiscountCode(discountCode, discountValue)))
                shareViewModelWithCart.createOrder(OrderData(order))

            }
        }


        binding.btnApplyVoucher.setOnClickListener {
            val voucherText = binding.etVoucherCode.text.toString()
            if (!binding.etVoucherCode.text.isNullOrBlank()) {
                if (voucherText == Constants.CODE_DISCOUNT_100) {
                    //calculateTotal(0.0)
                    Toast.makeText(requireContext(), "congrats 100% off", Toast.LENGTH_SHORT)
                        .show()
                } else if (voucherText == Constants.CODE_DISCOUNT_35) {
                    //   calculateTotal(0.35)
                    Toast.makeText(requireContext(), "congrats 35% off", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showPaypalTest() {
        Toast.makeText(requireContext(), "paypal", Toast.LENGTH_SHORT).show()
    }

    private fun createListForAdapter(draftResponse: DraftResponse) {
        val lineItems = draftResponse.draft_order.line_items
        val viewedLineItems = lineItems.filterIndexed { index, _ ->
            index > 0
        }
        checkOutAdapter.submitList(viewedLineItems)
    }

    private fun calculateTotal(lineItems: List<LineItem>) {
        var totalPrice = 0.0
        for (lineItem in lineItems) {
            if (lineItems.indexOf(lineItem) != 0) {
                totalPrice += lineItem.price.toDouble() * lineItem.quantity
            }
        }

        binding.tvSumTotalPayment.text = totalPrice.toString()
    }


    override fun onDestroy() {
        super.onDestroy()
        super.onStart()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setAddress(addresses: List<Addresse>) {
        for (address in addresses) {
            if (address.default) {
                binding.tvUserAddress.text =
                    "${address.address1}, ${address.city},\n${address.country}"

            }
        }
    }
}