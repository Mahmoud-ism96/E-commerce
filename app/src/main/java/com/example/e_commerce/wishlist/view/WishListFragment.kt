package com.example.e_commerce.wishlist.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.MainActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentWishListBinding
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.response.LineItem
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftOrder
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.draftorder.send.SendLineItem
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants.WISHLIST_KEY
import com.example.e_commerce.utility.Functions
import com.example.e_commerce.wishlist.viewmodel.WishListViewModel
import com.example.e_commerce.wishlist.viewmodel.WishListViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WishListFragment : Fragment() {

    private lateinit var binding: FragmentWishListBinding
    private lateinit var _viewModelFactory: WishListViewModelFactory
    private lateinit var _viewModel: WishListViewModel

    private lateinit var wishlistID: String
    private lateinit var lastLineItems: List<LineItem>
    private lateinit var newSendLineItems: MutableList<SendLineItem>

    private lateinit var wishlistAdapter: WishListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)

        _viewModelFactory = WishListViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )
        _viewModel = ViewModelProvider(
            requireActivity(), _viewModelFactory
        )[WishListViewModel::class.java]

        wishlistAdapter = WishListAdapter {
            val action = WishListFragmentDirections.actionWishListFragmentToProductDetailsFragment(
                it
            )
            findNavController().navigate(action)
        }
        deleteWhenSwipe()

        binding.rvWishlist.apply {
            adapter = wishlistAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        if (FirebaseAuth.getInstance().currentUser != null) {

            binding.wishListFragmentSignInFirst.visibility = View.GONE

            if (Functions.checkConnectivity(requireContext())) {
                wishlistID = _viewModel.readStringFromSettingSP(WISHLIST_KEY)
                _viewModel.getDraftOrderByDraftId(wishlistID.toLong())
            } else {
                (requireActivity() as HomeActivity).noConnectionGroup.visibility = View.VISIBLE
                (requireActivity() as HomeActivity).retryButton.setOnClickListener {
                    if (Functions.checkConnectivity(requireContext())) {
                        wishlistID = _viewModel.readStringFromSettingSP(WISHLIST_KEY)
                        _viewModel.getDraftOrderByDraftId(wishlistID.toLong())
                        (requireActivity() as HomeActivity).noConnectionGroup.visibility = View.GONE
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.couldn_t_retrieve_data),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            lifecycleScope.launch {
                _viewModel.wishlistDraftOrderFlow.collectLatest {
                    when (it) {
                        is ApiState.Loading -> {
                            Log.w("TAG", "loading:")
                        }

                        is ApiState.Success -> {
                            val draftResponse: DraftResponse = it.data as DraftResponse
                            createListForAdapter(draftResponse)
                        }

                        is ApiState.Failure -> {
                            Log.w("TAG", "error:")
                        }
                    }
                }
            }
        } else {
            binding.wishListFragmentSignInFirst.visibility = View.VISIBLE
            binding.btnSignInFirst.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return binding.root
    }

    private fun createListForAdapter(draftResponse: DraftResponse) {
        lastLineItems = draftResponse.draft_order.line_items
        val viewedLineItems = lastLineItems.filterIndexed { index, _ ->
            index > 0
        }
        wishlistAdapter.submitList(viewedLineItems)
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
                val lineItem = wishlistAdapter.currentList[position]
                deleteItemFromCart(lineItem)

                Snackbar.make(
                    binding.rvWishlist,
                    getString(R.string.product_removed_from_wishlist),
                    Snackbar.LENGTH_LONG
                ).apply {
                        setAction("Undo") {
                            val newList = lastLineItems.toMutableList()
                            newList.add(position + 1, lineItem)
                            newSendLineItems.add(
                                SendLineItem(
                                    lineItem.variant_id, lineItem.quantity, lineItem.properties
                                )
                            )
                            wishlistAdapter.submitList(newList.filterIndexed { index, _ ->
                                index > 0
                            })
                        }
                        show()
                    }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rvWishlist)
    }

    private fun deleteItemFromCart(lineItem: LineItem) {

        val updatedItems = lastLineItems.filter { it.id != lineItem.id }
        lastLineItems = updatedItems

        if (lastLineItems.isNotEmpty()) {
            val viewedLineItems = lastLineItems.filterIndexed { index, _ ->
                index > 0
            }
            wishlistAdapter.submitList(viewedLineItems)
        }

        newSendLineItems = mutableListOf<SendLineItem>()
        for (item in lastLineItems) {
            newSendLineItems.add(
                SendLineItem(item.variant_id, item.quantity, item.properties)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewModel.resetState()
    }

    override fun onStop() {
        super.onStop()
        if (::_viewModel.isInitialized) if (::newSendLineItems.isInitialized) _viewModel.removeLineItem(
            wishlistID.toLong(), SendDraftRequest(
                SendDraftOrder(
                    newSendLineItems, FirebaseAuth.getInstance().currentUser!!.email!!, WISHLIST_KEY
                )
            )
        )
    }
}