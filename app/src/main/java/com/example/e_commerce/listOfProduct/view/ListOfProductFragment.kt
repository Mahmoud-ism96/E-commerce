package com.example.e_commerce.listOfProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentListOfProductBinding

class ListOfProductFragment : Fragment() {

    private lateinit var binding:FragmentListOfProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentListOfProductBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

}