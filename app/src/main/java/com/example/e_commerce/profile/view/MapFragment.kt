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
import com.example.e_commerce.model.pojo.address.Coordinate
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var coordinate: Coordinate
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
            )
        )

        settingViewModel =
            ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        googleMapHandler()

        binding.btnSaveLocation.setOnClickListener {
            if (marker != null) {
                binding.btnSaveLocation.visibility = View.GONE
                binding.prProgressMap.visibility = View.VISIBLE
                val action = MapFragmentDirections.actionMapFragmentToAddAddressFragment(coordinate)
                val navController = findNavController()
                navController.navigate(action)
            }

        }

    }

    private fun googleMapHandler() {
        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        supportMapFragment.getMapAsync { map ->
            val egypt = LatLng(26.8206, 30.8025)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(egypt, 6f))

            // Restrict the map boundaries to show only the area of Egypt
            val egyptBounds = LatLngBounds(
                LatLng(21.9999, 24.7000), // South west corner of Egypt
                LatLng(31.3333, 36.9000) // North east corner of Egypt
            )
            map.setLatLngBoundsForCameraTarget(egyptBounds)
            map.setMinZoomPreference(7F)

            supportMapFragment.getMapAsync { map_here ->
                map_here.setOnMapClickListener {
                    marker?.remove()
                    marker = map_here.addMarker(
                        MarkerOptions()
                            .position(it)
                    )
                    coordinate = Coordinate(it.latitude, it.longitude)
                }
            }
        }

    }
}