package com.nilesh.practiceapps.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.nilesh.practiceapps.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {

    private val screenName = MapsFragment::class.java.simpleName
    private lateinit var binding: FragmentMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)

        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0

        return binding.root.apply {
            binding.composeMapView.setContent {
                val marker = LatLng(latitude, longitude)
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    /*cameraPositionState = cameraState,*/
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                        mapType = MapType.HYBRID,
                        isTrafficEnabled = true
                    )
                ) {
                    Marker(
                        state = MarkerState(position = marker),
                        title = "MyPosition",
                        snippet = "This is a description of this Marker",
                        draggable = true
                    )
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        /**
         * This function will set provide an instance of the fragment.
         * */
        fun newInstance() = MapsFragment()
    }
}