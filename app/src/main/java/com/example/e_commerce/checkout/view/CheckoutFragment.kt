package com.example.e_commerce.checkout.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.BottomsheetchooseaddressBinding
import com.example.e_commerce.databinding.FragmentCheckoutBinding
import com.example.e_commerce.databinding.SuccessDialogLayoutBinding
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.customer_resposnse.Addresse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.response.LineItem
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftOrder
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.draftorder.send.SendLineItem
import com.example.e_commerce.model.pojo.order.DiscountCode
import com.example.e_commerce.model.pojo.order.Order
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.view.TAG
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModelFactory
import com.example.e_commerce.utility.Constants
import com.google.firebase.auth.FirebaseAuth
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var shareViewModelWithCart: CartViewModel
    private var email: String? = null
    private var discountCode: String = ""
    private var discountValue: String = ""
    private lateinit var checkOutAdapter: CheckOutAdapter
    private lateinit var currentAddresses: List<Addresse>
    private lateinit var lastLineItems: List<LineItem>
    private lateinit var priceRuleResponse: PriceRuleResponse

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

        lifecycleScope.launch {
            shareViewModelWithCart.pricesRulesStateFlow.collectLatest {
                if (it is ApiState.Success) {
                    priceRuleResponse = it.data as PriceRuleResponse
                }
            }
        }
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
            showAddressesBottomSheet()
        }
        binding.tvUserAddress.setOnClickListener {
            showAddressesBottomSheet()
        }

        binding.paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_paypal) {
                binding.btnPlaceOrder.visibility = View.GONE
                binding.paymentButtonContainer.visibility = View.VISIBLE
                if (!freeOrder()) {
                    showPaypalTest()
                }
            } else {
                binding.btnPlaceOrder.visibility = View.VISIBLE
                binding.paymentButtonContainer.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            shareViewModelWithCart.cartDraftOrderStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {

                    }

                    is ApiState.Success -> {
                        createListForAdapter(it.data as DraftResponse)
                        lastLineItems = it.data.draft_order.line_items
                        calculateTotal()
                    }

                    is ApiState.Failure -> {

                    }
                }
            }
        }
        binding.btnPlaceOrder.setOnClickListener {
            confirmOrder()
        }


        binding.btnApplyVoucher.setOnClickListener {
            val voucherText = binding.etVoucherCode.text.toString()
            for (priceRule in priceRuleResponse.price_rules) {
                when (voucherText) {
                    priceRule.title -> {
                        calculateTotalWithDiscount(priceRule.value, priceRule.value_type)
                        discountCode = priceRule.title
                        discountValue = priceRule.value.absoluteValue.toString()
                    }
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAddressesBottomSheet() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val sheetBinding = BottomsheetchooseaddressBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        var addresse: Addresse? = null

        val addressAdapter = AddressSheetAdapter {
            addresse = it
        }
        sheetBinding.rvAddresses.adapter = addressAdapter
        addressAdapter.submitList(currentAddresses)
        sheetBinding.btnSave.setOnClickListener {
            if (addresse != null) {
                binding.tvUserAddress.text =
                    "${addresse!!.address1}, ${addresse!!.city},\n${addresse!!.country}"
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "please select an Address", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun calculateTotalWithDiscount(discountValue: Int, discountType: String) {
        val totalBeforeDiscount = binding.tvSumTotalPayment.text.toString().toFloat()
        var totalAfterDiscount: Float = 0.0F
        if (discountType == "percentage") {
            totalAfterDiscount =
                (totalBeforeDiscount - totalBeforeDiscount * (discountValue.absoluteValue / 100.0f))
            Toast.makeText(
                requireContext(),
                "congrats ${discountValue.absoluteValue} % off",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            totalAfterDiscount = (totalBeforeDiscount - discountValue.absoluteValue).absoluteValue
            Toast.makeText(
                requireContext(),
                "congrats ${discountValue.absoluteValue} EGP off",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        binding.tvSumTotalPayment.text = totalAfterDiscount.toString()
        freeOrder()
        binding.etVoucherCode.isEnabled = false
        binding.btnApplyVoucher.isEnabled = false
    }

    private fun showPaypalTest() {
        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(
                                    currencyCode = CurrencyCode.USD,
                                    value = binding.tvSumTotalPayment.text.toString()
                                )
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i(TAG, "CaptureOrderResult: $captureOrderResult")
                    Toast.makeText(requireContext(), "Payment Approved", Toast.LENGTH_SHORT).show()
                    confirmOrder()
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "Buyer canceled the PayPal experience.")
                Toast.makeText(requireContext(), "Payment Canceled", Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Log.d(TAG, "Error: $errorInfo")
                Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun confirmOrder() {
        val sendLineItems: MutableList<com.example.e_commerce.model.pojo.order.LineItem> =
            mutableListOf()
        for (lineItem in lastLineItems) {
            if (lastLineItems.indexOf(lineItem) > 0) {
                sendLineItems.add(
                    com.example.e_commerce.model.pojo.order.LineItem(
                        lineItem.quantity, lineItem.variant_id
                    )
                )
            }
        }
        lifecycleScope.launch {
            val order =
                Order(email!!, sendLineItems, listOf(DiscountCode(discountCode, discountValue)))
            shareViewModelWithCart.createOrder(OrderData(order))

        }

        showSuccessDialog()
    }

    private fun createListForAdapter(draftResponse: DraftResponse) {
        val lineItems = draftResponse.draft_order.line_items
        val viewedLineItems = lineItems.filterIndexed { index, _ ->
            index > 0
        }
        checkOutAdapter.submitList(viewedLineItems)
    }

    private fun calculateTotal() {
        var totalPrice = 0.0
        for (lineItem in lastLineItems) {
            if (lastLineItems.indexOf(lineItem) != 0) {
                totalPrice += lineItem.price.toDouble() * lineItem.quantity
            }
        }

        binding.tvSumTotalPayment.text = totalPrice.toString()
        freeOrder()
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

    private fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        val bindingSuccessDialog = SuccessDialogLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bindingSuccessDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val window = dialog.window
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = layoutParams

        bindingSuccessDialog.btnContinue.setOnClickListener {
            clearDraftOrder()
            val action = CheckoutFragmentDirections.actionCheckoutFragmentToHomeFragment()
            findNavController().navigate(action)
            dialog.dismiss()
        }

        dialog.setOnShowListener {

        }

        dialog.show()

    }

    private fun clearDraftOrder() {
        shareViewModelWithCart.modifyDraftOrder(
            shareViewModelWithCart.readStringFromSettingSP(Constants.CART_KEY).toLong(),
            SendDraftRequest(
                SendDraftOrder(
                    listOf(
                        SendLineItem(
                            lastLineItems[0].variant_id, lastLineItems[0].quantity,
                            listOf()
                        )
                    ), FirebaseAuth.getInstance().currentUser!!.email!!, "cart"
                )
            )
        )
    }

    private fun freeOrder(): Boolean {
        if (binding.tvSumTotalPayment.text == "0.0") {
            binding.btnPlaceOrder.visibility = View.VISIBLE
            binding.paymentButtonContainer.visibility = View.GONE
            return true
        }
        return false
    }

}