package com.example.e_commerce.orders.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.Home.viewmodel.HomeViewModelFactory
import com.example.e_commerce.databinding.FragmentOrdersBinding
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.order_response.CustomerOrderResponse
import com.example.e_commerce.model.pojo.order_response.OrderResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.orders.viewmodel.OrderViewModel
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var factory: HomeViewModelFactory
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var orderAdapter:OrderRecycleAdapter
    private var email: String? = null
    private var name: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBackToProfile.setOnClickListener {
            navigateBack()
        }

        factory = HomeViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )

        orderViewModel = ViewModelProvider(requireActivity(), factory)[OrderViewModel::class.java]

        orderAdapter= OrderRecycleAdapter(){
            orderViewModel.getOrderById(it)
            TODO("navigate to order Details")
        }
        binding.rvCurrentOreders.apply {
            adapter=orderAdapter
            layoutManager = LinearLayoutManager(view.context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        email = firebaseAuth.currentUser?.email
        name = firebaseAuth.currentUser?.displayName

        orderViewModel.getCustomerId(email!!,name!!)

        lifecycleScope.launch {
            orderViewModel.customerStateFlow.collectLatest {
                when (it){
                    is ApiState.Success ->{
                        val customer = it.data as CustomerResponse
                        val customerId=customer.customers[0].id
                        orderViewModel.getCustomerOrders(customerId)
                    }
                    else -> {}
                }
            }

            orderViewModel.listOfOrdersStateFlow.collectLatest {
                when(it){
                    is ApiState.Success ->{
                        val orders = it.data as CustomerOrderResponse
                        orderAdapter.submitList(orders.order)
                    }
                    else -> {}
                }
            }

        }


    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }
}