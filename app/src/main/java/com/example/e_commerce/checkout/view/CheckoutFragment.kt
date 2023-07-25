package com.example.e_commerce.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.databinding.FragmentCheckoutBinding
import com.example.e_commerce.utility.Constants

class CheckoutFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.GONE
    }

    private lateinit var binding: FragmentCheckoutBinding
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
}