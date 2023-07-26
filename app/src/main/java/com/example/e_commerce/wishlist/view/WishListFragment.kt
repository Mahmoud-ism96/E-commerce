package com.example.e_commerce.wishlist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.e_commerce.databinding.FragmentWishListBinding
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.wishlist.viewmodel.WishListViewModel
import com.example.e_commerce.wishlist.viewmodel.WishListViewModelFactory

class WishListFragment : Fragment() {

    private lateinit var binding: FragmentWishListBinding
    private lateinit var _viewModelFactory: WishListViewModelFactory
    private lateinit var _viewModel: WishListViewModel

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

//        wishlistRecycleAdapter = CartAdapter(onPlusClick = { _, quantity ->
//            val newQuantity = quantity + 1
//            // cartViewModel.updateQuantityForItem(id, newQuantity)
//        }, onMinusClick = { _, quantity ->
//            if (quantity > 1) {
//                val newQuantity = quantity - 1
//                //   cartViewModel.updateQuantityForItem(id, newQuantity)
//            } else {
//                // cartViewModel.deleteItemFromCart(id)
//            }
//        }, onItemClick = {
//            val action = CartFragmentDirections.actionCartFragment2ToProductDetailsFragment(it)
//            findNavController().navigate(action)
//        })
//
//        binding.rvWishlist.apply {
//            adapter = wishlistRecycleAdapter
//            layoutManager = LinearLayoutManager(requireContext()).apply {
//                orientation = RecyclerView.VERTICAL
//            }
//        }
//
//        if (FirebaseAuth.getInstance().currentUser != null) {
//
//            val wishlistID = _viewModel.readStringFromSettingSP(WISHLIST_KEY)
//
//            _viewModel.getDraftOrderByDraftId(wishlistID.toLong())
//
//            lifecycleScope.launch {
//                _viewModel.wishlistDraftOrderFlow.collectLatest {
//                    when (it) {
//                        is ApiState.Loading -> {
//                            Log.w(TAG, "loading:")
//                        }
//
//                        is ApiState.Success -> {
//                            val draftResponse: DraftResponse = it.data as DraftResponse
//                            wishlistRecycleAdapter.submitList(draftResponse.draft_order.line_items)
//                        }
//
//                        is ApiState.Failure -> {
//                            Log.w(TAG, "error:")
//                        }
//                    }
//                }
//            }
//        }

        return binding.root
    }

}