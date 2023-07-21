package com.example.e_commerce.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentMapBinding
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var settingViewModel: SettingViewModel
    private var marker: Marker? = null
    override fun onStart() {
        super.onStart()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = SettingViewModelFactory(
            Repo.getInstance(
            ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
        ))

        settingViewModel = ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        googleMapHandler()

        binding.btnSaveLocation.setOnClickListener {
            binding.btnSaveLocation.visibility = View.GONE
            findNavController().popBackStack()
        }

    }

    private fun googleMapHandler() {
        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync { map ->
            map.setOnMapClickListener {
                marker?.remove()
                marker = map.addMarker(
                    MarkerOptions()
                        .position(it)
                )
            }
        }
    }

}