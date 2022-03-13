package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import java.util.*

class RepresentativeFragment : Fragment() {

    private val viewModel: RepresentativeViewModel by inject()

    private lateinit var binding: FragmentRepresentativeBinding

    private lateinit var enteredAddress: Address

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            representativeViewModel = viewModel

            rvMyRepresentatives.adapter = RepresentativeListAdapter()

//            viewModel.representativeAddress.observe(viewLifecycleOwner, Observer { address ->
//                address.line1 = etAddressLine1.text.toString()
//                address.line2 = etAddressLine2.text.toString()
//                address.city = etCity.text.toString()
//                address.zip = etZipcode.text.toString()
//                address.state = spinnerState.selectedItem.toString()
//            })

            btnSearch.setOnClickListener {
                hideKeyboard()
                composeAddress()
                viewModel.getRepresentativesFromEnteredAddress(enteredAddress)
            }

            btnLocation.setOnClickListener {
                hideKeyboard()
                checkLocationPermissionsAndHandleRepresentativeAddress()
            }
        }

        return binding.root
    }

//    private fun keepInfoOnDeviceRotation() {
//        with(binding) {
//            viewModel.address.observe(viewLifecycleOwner, Observer { address ->
//                viewModel.setAddressLine1(etAddressLine1.text.toString())
//
//                address.line1 = etAddressLine1.text.toString()
//                address.line2 = etAddressLine2.text.toString()
//                address.city = etCity.text.toString()
//                address.zip = etZipcode.text.toString()
//                address.state = spinnerState.selectedItem.toString()
//            })
//        }
//    }

    private fun snackBarInfoComposer(field: String) {
        Snackbar.make(requireView(), "The field $field must be filled.", Snackbar.LENGTH_LONG).show()
    }

    private fun composeAddress() {
        val line1 = binding.etAddressLine1.text.toString()
        val line2 = if (binding.etAddressLine2.text.isEmpty()) null else binding.etAddressLine2.text.toString()
        val city = binding.etCity.text.toString()
        val state = binding.spinnerState.selectedItem.toString()
        val zipCode = binding.etZipcode.text.toString()

        if (line1.isEmpty()) snackBarInfoComposer(line1)
        if (city.isEmpty()) snackBarInfoComposer(city)
        if (zipCode.isEmpty()) snackBarInfoComposer(zipCode)

        enteredAddress = Address(line1, line2, city, state, zipCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //DONE: Handle location permission result to get location on permission granted
        when (requestCode) {
            REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                }
            }
        }
    }

    private fun checkLocationPermissionsAndHandleRepresentativeAddress(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                getLocation()
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
            }
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //DONE: Check if permission is already granted and return (true = granted, false = denied/other)
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //DONE: Get location from LocationServices
        val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        client.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    val lastAddress = geoCodeLocation(it)
                    viewModel.setAddress(lastAddress)

                    val selectedState = resources.getStringArray(R.array.states).indexOf(lastAddress.state)
                    binding.spinnerState.setSelection(selectedState)

                    viewModel.getRepresentativesFromEnteredAddress(lastAddress)
                }
            }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())

        val coordinates = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        val addressFromCoordinates = coordinates.first()

        if (addressFromCoordinates.thoroughfare == null || addressFromCoordinates.postalCode == null) {
            AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
                .setTitle("Location not found")
                .setMessage("Your location seems to be invalid. Please, try typing your desired address above.")
                .setPositiveButton("OK", null)
                .show()

            return Address("", null, "", "", "")
        }

        return coordinates
            .map { address ->
                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    companion object {
        private const val REQUEST_ACCESS_FINE_LOCATION = 123
    }
}
