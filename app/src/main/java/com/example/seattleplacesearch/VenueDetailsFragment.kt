package com.example.seattleplacesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
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
        fun newInstance(venue: Venue) = VenueDetailsFragment().apply {
            arguments = bundleOf(VENUE to venue)
        }
    }

    private lateinit var venue: Venue

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
        venue = (arguments?.get(VENUE) as? Venue)!!
        if (venue != null) {
            detailsName.text = venue.name
            detailsCategory.text = venue.category
            detailsAddress.text = venue.location.address
            detailsDistance.text =
                String.format(resources.getString(R.string.distanceToCityCenter), venue.distanceToCenter)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            val seattle = LatLng(MapInfoHelper.SEATTLE_LATITUDE, MapInfoHelper.SEATTLE_LONGITUDE)
            val venueLocation = LatLng(
                venue.location.lat,
                venue.location.lng
            )
            googleMap.addMarker(MarkerOptions().position(seattle))
            googleMap.addMarker(MarkerOptions().position(venueLocation))

            val builder = LatLngBounds.Builder()
            builder.include(seattle)
            builder.include(venueLocation)

            val bounds = builder.build()

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.getDimensionPixelOffset(R.dimen.map_padding)
                )
            )
            googleMap.uiSettings.setAllGesturesEnabled(false)
            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.setOnMarkerClickListener(OnMarkerClickListener { true })
        }
    }
}