package com.example.e_commerce.orders.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentOrdersBinding
import com.example.e_commerce.model.pojo.customer_order_response.CustomerOrderResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.orders.viewmodel.OrderViewModel
import com.example.e_commerce.orders.viewmodel.OrderViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var factory: OrderViewModelFactory
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderAdapter: OrderRecycleAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.btnBackToProfile.setOnClickListener {
            navigateBack()
        }

        factory = OrderViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )

        orderViewModel = ViewModelProvider(requireActivity(), factory)[OrderViewModel::class.java]

        orderAdapter = OrderRecycleAdapter {
            orderViewModel.getOrderById(it)
            navController.navigate(R.id.action_ordersFragment_to_orderDetailsFragment)
        }
        binding.rvCurrentOreders.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(view.context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        val customerId = orderViewModel.readFromSp(Constants.CUSTOMER_ID_KEY)
        orderViewModel.getCustomerOrders(customerId.toLong())

        lifecycleScope.launch {
            orderViewModel.listOfOrdersStateFlow.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        binding.orderLoading.visibility = View.GONE
                        val orders = it.data as CustomerOrderResponse
                        orderAdapter.submitList(orders.orders)
                    }

                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ApiState.Loading -> {
                        binding.orderLoading.apply {
                            visibility = View.VISIBLE
                            setAnimation(R.raw.loading)
                        }
                    }
                }
            }
        }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as HomeActivity).bottomNavigationBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as HomeActivity).bottomNavigationBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        orderViewModel.refreshOrderList()
        orderAdapter.submitList(emptyList())
    }
}