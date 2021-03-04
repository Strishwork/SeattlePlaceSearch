package com.example.seattleplacesearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.seattleplacesearch.MapInfoHelper
import com.example.seattleplacesearch.R
import com.example.seattleplacesearch.VenueViewState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.details_fragment.*


@AndroidEntryPoint
class VenueDetailsFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val VENUE = "venue"
        fun newInstance(venueViewState: VenueViewState) = VenueDetailsFragment().apply {
            arguments = bundleOf(VENUE to venueViewState)
        }
    }

    private lateinit var venueViewState: VenueViewState
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.detailsMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        venueViewState = ((arguments?.get(VENUE) as? VenueViewState)!!)
        detailsName.text = venueViewState.name
        detailsCategory.text = venueViewState.category
        detailsAddress.text = venueViewState.location.address
        detailsDistance.text =
            String.format(
                resources.getString(R.string.distanceToCityCenter),
                venueViewState.distanceToCenter
            )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap

            setMapMarkersAndRecenter()

            disableMapInteractions()
        }
    }

    private fun setMapMarkersAndRecenter() {
        val seattle = LatLng(MapInfoHelper.SEATTLE_LATITUDE, MapInfoHelper.SEATTLE_LONGITUDE)
        val venueLocation = LatLng(
            venueViewState.location.lat,
            venueViewState.location.lng
        )
        map.addMarker(MarkerOptions().position(seattle))
        map.addMarker(MarkerOptions().position(venueLocation))

        val builder = LatLngBounds.Builder()
        builder.include(seattle)
        builder.include(venueLocation)

        val bounds = builder.build()

        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.getDimensionPixelOffset(R.dimen.map_padding)
            )
        )
    }

    private fun disableMapInteractions() {
        map.uiSettings.setAllGesturesEnabled(false)
        map.uiSettings.isMapToolbarEnabled = false
        map.setOnMarkerClickListener { true }
    }
}