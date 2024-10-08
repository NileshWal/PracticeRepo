package com.nilesh.practiceapps.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nilesh.practiceapps.R
import com.nilesh.practiceapps.database.model.UserRecordsListDetails
import com.nilesh.practiceapps.databinding.FragmentMapsBinding
import com.nilesh.practiceapps.utils.CommonUtils.USER_RECORDS_LIST_DETAILS

class MapsFragment : Fragment() {

    private val screenName = MapsFragment::class.java.simpleName
    private lateinit var binding: FragmentMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)

        MapsInitializer.initialize(requireActivity(), MapsInitializer.Renderer.LATEST) {}
        val userRecordsListDetails =
            arguments?.getParcelable<UserRecordsListDetails>(USER_RECORDS_LIST_DETAILS)
        val marker = LatLng(
            userRecordsListDetails?.latitude ?: 0.0,
            userRecordsListDetails?.longitude ?: 0.0
        )
        return binding.root.apply {
            binding.composeMapView.setContent {

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(marker, 5f)
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                        mapType = MapType.HYBRID,
                        isTrafficEnabled = true
                    )
                ) {
                    Marker(
                        state = MarkerState(position = marker),
                        title = userRecordsListDetails?.let { "${it.firstName} ${it.lastName}, \n${it.latitude} ${it.longitude}" }
                            ?: context.getString(R.string.john_doe),
                        snippet = userRecordsListDetails?.let { "${it.city}, ${it.country}" }
                            ?: context.getString(R.string.this_is_a_description_of_this_marker),
                        draggable = true
                    )
                }
            }
        }
    }

    companion object {

        /**
         * This function will set provide an instance of the fragment.
         * */
        fun newInstance(bundle: Bundle): MapsFragment {
            val fragment = MapsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}