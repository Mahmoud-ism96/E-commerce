package com.example.e_commerce.checkout.view

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
import com.example.e_commerce.checkout.viewmodel.CheckOutViewModel
import com.example.e_commerce.checkout.viewmodel.CheckOutViewModelFactory
import com.example.e_commerce.databinding.FragmentCheckoutBinding
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.level.InventoryLevelData
import com.example.e_commerce.model.pojo.order.DiscountCode
import com.example.e_commerce.model.pojo.order.LineItem
import com.example.e_commerce.model.pojo.order.Order
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var checkOutViewModel: CheckOutViewModel
    private lateinit var factory: CheckOutViewModelFactory
    private lateinit var firebaseAuth: FirebaseAuth
    private var email: String? = null
    private var discountCode:String=""
    private var discountValue:String=""

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

        factory = CheckOutViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        checkOutViewModel =
            ViewModelProvider(requireActivity(), factory)[CheckOutViewModel::class.java]

        firebaseAuth = FirebaseAuth.getInstance()
        email = firebaseAuth.currentUser?.email

        binding.btnPlaceOrder.setOnClickListener {
            lifecycleScope.launch {
                val lineItem = listOf(
                    LineItem(quantity = 1, variant_id = 45786104529195)
                )
                val order = Order(email!!,lineItem, listOf(DiscountCode(discountCode,discountValue)))
                checkOutViewModel.createOrder(OrderData(order))

//                val inventoryLevelData = InventoryLevelData(87897276715, 47836051603755, 20)
//                checkOutViewModel.updateVariantQuantity(inventoryLevelData)
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
}