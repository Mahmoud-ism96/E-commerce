package com.example.e_commerce.shoppingcart.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.MainActivity
import com.example.e_commerce.databinding.FragmentCartBinding
import com.example.e_commerce.model.pojo.CheckArgument
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.response.LineItem
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftOrder
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.draftorder.send.SendLineItem
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModelFactory
import com.example.e_commerce.utility.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "hassankamal"

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var mAuth: FirebaseAuth
    private var lastLineItems: List<LineItem> = listOf()
        set(value) {
            calculateTotal()
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val factory = CartViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        cartViewModel = ViewModelProvider(requireActivity(), factory)[CartViewModel::class.java]
        if (mAuth.currentUser == null) {
            binding.groupSigned.visibility = View.GONE
            binding.groupNotSigned.visibility = View.VISIBLE
            binding.btnSigninSetting.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            cartViewModel.getPriceRules()
            cartAdapter = CartAdapter(onOperationClicked = { index, quantity ->
                val list = lastLineItems.toMutableList()
                list[index + 1].quantity = quantity
                lastLineItems = list
            }, onItemClick = {
                val action = CartFragmentDirections.actionCartFragment2ToProductDetailsFragment(it)
                findNavController().navigate(action)
            })

            binding.rvCartItems.adapter = cartAdapter
            deleteWhenSwipe()

            cartViewModel.getDraftOrderByDraftId(
                cartViewModel.readStringFromSettingSP(Constants.CART_KEY).toLong()
            )

            lifecycleScope.launch {
                cartViewModel.cartDraftOrderStateFlow.collectLatest {
                    when (it) {
                        is ApiState.Loading -> {
                            binding.relodGroub.visibility = View.VISIBLE
                        }

                        is ApiState.Success -> {
                            createListForAdapter(it.data as DraftResponse)
                            lastLineItems = it.data.draft_order.line_items
                            calculateTotal()
                            binding.relodGroub.visibility = View.GONE
                        }

                        is ApiState.Failure -> {
                            Log.w(TAG, "error:")
                        }
                    }
                }
            }

            binding.btnCheckout.setOnClickListener {
                if (lastLineItems.size > 1) {
                    binding.relodGroub.visibility = View.VISIBLE
                    val action = CartFragmentDirections.actionCartFragment2ToCheckoutFragment(
                        CheckArgument(lastLineItems)
                    )
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "add item first", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun modifyCartDraftOrder(lineItems: List<LineItem>) {
        val newSendLineItems = mutableListOf<SendLineItem>()
        for (lineItem in lineItems) {
            newSendLineItems.add(
                SendLineItem(lineItem.variant_id, lineItem.quantity, lineItem.properties)
            )
        }
        cartViewModel.modifyDraftOrder(
            cartViewModel.readStringFromSettingSP(Constants.CART_KEY).toLong(), SendDraftRequest(
                SendDraftOrder(
                    newSendLineItems, mAuth.currentUser!!.email!!, "cart"
                )
            )
        )
    }

    private fun createListForAdapter(draftResponse: DraftResponse) {
        val lineItems = draftResponse.draft_order.line_items
        val viewedLineItems = lineItems.filterIndexed { index, _ ->
            index > 0
        }
        cartAdapter.submitList(viewedLineItems)
    }

    private fun deleteWhenSwipe() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val lineItem = cartAdapter.currentList[position]
                val newList = lastLineItems.toMutableList()
                newList.removeAt(position + 1)
                lastLineItems = newList
                cartAdapter.submitList(lastLineItems.filterIndexed { index, _ ->
                    index > 0
                })

                Snackbar.make(binding.rvCartItems, "deleting Item.... ", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            newList.add(position + 1, lineItem)
                            cartAdapter.submitList(lastLineItems.filterIndexed { index, _ ->
                                index > 0
                            })
                        }
                        show()
                    }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rvCartItems)
    }

    private fun calculateTotal() {
        var totalPrice = 0.0
        for (lineItem in lastLineItems) {
            if (lastLineItems.indexOf(lineItem) != 0) {
                totalPrice += lineItem.price.toDouble() * lineItem.quantity
            }
        }

        binding.tvSumTotal.text = totalPrice.toString()
    }

    override fun onStop() {
        super.onStop()
        if (mAuth.currentUser != null) {
            modifyCartDraftOrder(lastLineItems)
            cartViewModel.resetState()
        }
    }
}