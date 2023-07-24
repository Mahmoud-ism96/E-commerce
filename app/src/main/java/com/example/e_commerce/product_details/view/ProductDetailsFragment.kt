package com.example.e_commerce.product_details.view

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.Review
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.product_details.viewmodel.ProductDetailsViewModel
import com.example.e_commerce.product_details.viewmodel.ProductDetailsViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var _viewModelFactory: ProductDetailsViewModelFactory
    private lateinit var _viewModel: ProductDetailsViewModel

    private lateinit var cartItem: CartItem

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var selectedVariantID: String

    val review1 = Review(
        reviewImage = "https://example.com/review1.jpg",
        reviewerName = "John Doe",
        rating = 4.5f,
        reviewDetails = "This product is amazing!",
        reviewDate = "2023-07-20"
    )

    val review2 = Review(
        reviewImage = null,
        reviewerName = "Jane Smith",
        rating = 3.0f,
        reviewDetails = "Good product, but could be better.",
        reviewDate = "2023-07-18"
    )

    val reviewList = listOf(review1, review2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)


        _viewModelFactory = ProductDetailsViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )
        _viewModel = ViewModelProvider(
            requireActivity(), _viewModelFactory
        )[ProductDetailsViewModel::class.java]

        val productID = ProductDetailsFragmentArgs.fromBundle(requireArguments()).productId

        _viewModel.getProductDetails(productID)

        binding.layoutDesc.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.cvDesc.setOnClickListener {
            expandCardView(binding.layoutDesc, binding.tvDescDetails, binding.ibDesc)
        }

        binding.layoutReview.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.cvReview.setOnClickListener {
            expandCardView(binding.layoutReview, binding.rvReview, binding.ibReview)
        }

        reviewAdapter = ReviewAdapter()
        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        sizeAdapter = SizeAdapter() {
            Log.i("TAG", "onCreateView: $it")
            selectedVariantID = it.toString()
        }
        binding.rvVariants.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }

        imageAdapter = ImageAdapter()
        binding.rvDetailsImages.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }
        binding.rvIndicator.attachToRecyclerView(binding.rvDetailsImages)

        lifecycleScope.launch {
            _viewModel.productDetailsState.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        binding.groupDetailsLoading.visibility = View.VISIBLE
                        binding.groupProductDetailsView.visibility = View.GONE
                    }

                    is ApiState.Success -> {
                        val productDetails: ProductDetailsResponse =
                            it.data as ProductDetailsResponse

                        val title = productDetails.product.title
                        val vendor = productDetails.product.vendor
                        val price = productDetails.product.variants[0].price
                        val inventoryQuantity =
                            productDetails.product.variants[0].inventory_quantity
                        val image = productDetails.product.image.src

                        val desc = productDetails.product.body_html

                        val totalRating = reviewList.map { it.rating }.sum()
                        val averageRating = totalRating / reviewList.size

                        val variants = productDetails.product.variants

                        val images = productDetails.product.images

                        imageAdapter.submitList(images)
                        reviewAdapter.submitList(reviewList)

                        if (productDetails.product.variants.isNotEmpty()) {
                            if (productDetails.product.variants[0].option1 != "OS") {
                                binding.tvSizeTitle.visibility = View.VISIBLE
                                binding.rvVariants.visibility = View.VISIBLE

                                sizeAdapter.submitList(variants)
                            } else {
                                selectedVariantID = variants[0].id.toString()
                            }
                        }



                        cartItem = CartItem(productID, title, price, inventoryQuantity, image)
                        binding.apply {
                            tvDetailVendorName.text = vendor
                            tvDetailProductName.text = title
                            tvDetailPrice.text = price
                            tvDescDetails.text = desc
                            tvProductDetailRating.text = averageRating.toString()
                        }

                        binding.groupDetailsLoading.visibility = View.GONE
                        binding.groupProductDetailsView.visibility = View.VISIBLE
                    }

                    is ApiState.Failure -> {
                        binding.groupDetailsLoading.visibility = View.GONE
                        binding.groupProductDetailsView.visibility = View.GONE

                        Toast.makeText(
                            requireContext(), getString(R.string.error_loading_product_data), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnDetailAddToCart.setOnClickListener {
            if (::selectedVariantID.isInitialized) {
                if (::cartItem.isInitialized) _viewModel.addToCart(cartItem)
                Toast.makeText(
                    requireContext(), getString(R.string.item_added_to_cart), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_a_size_to_progress),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun expandCardView(
        layout: ConstraintLayout, view: View, imageButton: ImageButton
    ) {
        TransitionManager.beginDelayedTransition(layout, AutoTransition())
        if (layout == binding.layoutDesc) {
            TransitionManager.beginDelayedTransition(binding.cvReview, AutoTransition())
        }
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
            imageButton.setImageResource(R.drawable.round_expand_less_black_24)
        } else {
            view.visibility = View.GONE
            imageButton.setImageResource(R.drawable.round_keyboard_arrow_down_black_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewModel.resetState()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as HomeActivity).bottomNavigationBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as HomeActivity).bottomNavigationBar.visibility = View.VISIBLE
    }

}