package com.example.practiceapps.ui.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.practiceapps.R
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.databinding.FragmentUserRecordsBinding
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.ui.activity.MainActivity
import com.example.practiceapps.utils.CommonUtils
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.showToastMessage
import com.example.practiceapps.viewmodel.UserRecordsViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserRecordsFragment : Fragment() {

    private val screenName = UserRecordsFragment::class.java.simpleName
    private lateinit var binding: FragmentUserRecordsBinding
    private val viewModel: UserRecordsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRecordsBinding.inflate(layoutInflater, container, false)
        subscribeToObservables()
        return binding.root.apply {
            binding.composeView.setContent {
                MaterialTheme(
                    colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
                ) {
                    UserRecordListView(viewModel.userRecordsLiveData)
                }
            }
            callApi()
        }
    }

    /**
     * This function will set the livedata observables to listen to the change in loader value.
     * */
    private fun subscribeToObservables() = viewModel.loaderLiveData.observe(viewLifecycleOwner) {
        showLoader(it.shouldShow)
        if (it.responseStatus != ResponseStatus.NO_ISSUE) {
            showToastMessage(requireActivity(), it.responseStatus.toString())
        }
    }

    /**
     * This function will call the User List API.
     * */
    private fun callApi() {
        showLoader(true)
        if (CommonUtils.isConnected(requireActivity())) {
            viewModel.callUserRecordsApi(0, 20)
        } else {
            showLoader(false)
            showToastMessage(requireActivity(), getString(R.string.no_internet))
        }
    }

    /**
     * This function will call the User List API.
     *
     * @param isAscending Should the list be in ascending or descending order.
     * */
    private fun fetchOrderedListFromDB(isAscending: Boolean) {
        showLoader(true)
        if (CommonUtils.isConnected(requireActivity())) {
            viewModel.orderUserList(isAscending)
        } else {
            showLoader(false)
            showToastMessage(requireActivity(), getString(R.string.no_internet))
        }
    }

    /**
     * This function will either show or make the loader disappear.
     *
     * @param shouldShow Boolean parameter to make the loader visible or gone.
     * */
    private fun showLoader(shouldShow: Boolean) {
        binding.loader.loadingPanel.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }

    /**
     * This function will create the base view of LazyColumn (like recyclerview).
     *
     * @param userRecordsLiveData The SnapshotStateList of PublicApisListDetails
     * */
    @Composable
    private fun UserRecordListView(userRecordsLiveData: SnapshotStateList<UserRecordsListDetails>) {
        Scaffold(
            content = { padding ->
                Row {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        ReorderView()
                        LazyColumn {
                            items(items = userRecordsLiveData) { item ->
                                SingleUserRecordView(item)
                            }
                        }
                    }
                }
            }
        )
    }

    /**
     * This function will create the item view for the LazyColumn (like recyclerview).
     *
     * @param userRecordsListDetails The UserRecordsListDetails object
     * */
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun SingleUserRecordView(userRecordsListDetails: UserRecordsListDetails) {
        val context = LocalContext.current
        val latLang = LatLng(
            userRecordsListDetails.latitude ?: 0.0,
            userRecordsListDetails.longitude ?: 0.0
        )
        val settingResultRequest = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                callMapFragment(latLang)
            } else {
                LogUtils.e(screenName, "Denied")
            }
        }
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                LogUtils.e(screenName, "PERMISSION GRANTED")
                checkLocationSetting(
                    context = context,
                    onDisabled = { intentSenderRequest ->
                        LogUtils.e(screenName, "onDisabled")
                        settingResultRequest.launch(intentSenderRequest)
                    },
                    onEnabled = {
                        LogUtils.e(screenName, "onEnabled")
                        callMapFragment(latLang)
                    }
                )

            } else {
                LogUtils.e(screenName, "PERMISSION DENIED")
            }
        }
        Card(
            onClick = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
            modifier = Modifier
                .padding(8.dp, 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = 2.dp,
            backgroundColor = colorResource(R.color.blue_light_tint),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.CenterVertically)
                ) {
                    CustomRowTextView(
                        stringResource(R.string.name),
                        "${userRecordsListDetails.firstName} ${userRecordsListDetails.lastName}"
                    )
                    CustomRowTextView(
                        stringResource(R.string.gender_dob),
                        "${viewModel.genderShortForm(userRecordsListDetails.gender)}, " +
                                "${userRecordsListDetails.dateOfBirth}"
                    )
                    CustomRowTextView(
                        stringResource(R.string.email_phone),
                        "${userRecordsListDetails.email}, ${userRecordsListDetails.phone}"
                    )
                    CustomRowTextView(
                        stringResource(R.string.address),
                        "${userRecordsListDetails.street}, " +
                                "${userRecordsListDetails.city}, " +
                                "${userRecordsListDetails.state}, " +
                                "${userRecordsListDetails.country}, " +
                                "${userRecordsListDetails.zipcode}, " +
                                "${userRecordsListDetails.latitude} - " +
                                "${userRecordsListDetails.longitude}"
                    )
                    CustomRowTextView(stringResource(R.string.job), userRecordsListDetails.job)
                }
            }
        }
    }

    // call this function on button click
    private fun checkLocationSetting(
        context: Context,
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: () -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder().addLocationRequest(locationRequest.build())
        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        gpsSettingTask.addOnSuccessListener { onEnabled() }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()
                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // ignore here
                }
            }
        }

    }


    /**
     * This function will create custom attributes for Text.
     *
     * @param textTitle The text to display.
     * @param textValue The type of FontWeight we need for the text.
     * */
    @Composable
    private fun CustomRowTextView(textTitle: String?, textValue: String?) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Text(
                text = textTitle ?: stringResource(R.string.title),
                textAlign = TextAlign.Start,
                color = colorResource(R.color.black),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = textValue ?: stringResource(R.string.no_data),
                textAlign = TextAlign.Start,
                color = colorResource(R.color.black),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1.5f)
            )
        }
    }

    @Composable
    private fun ReorderView() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painterResource(R.drawable.ascending),
                contentDescription = "Ascend",
                modifier = Modifier.clickable { fetchOrderedListFromDB(true) }
            )
            Image(
                painterResource(R.drawable.descending),
                contentDescription = "Descend",
                modifier = Modifier.clickable { fetchOrderedListFromDB(false) }
            )
        }
    }

    private fun callMapFragment(latLang: LatLng) {
        LogUtils.e(
            screenName,
            "callMapFragment latitude ${latLang.latitude} longitude ${latLang.longitude}"
        )

        val latLangBundle = Bundle()
        latLangBundle.putDouble("latitude", latLang.latitude)
        latLangBundle.putDouble("longitude", latLang.longitude)

        (requireActivity() as MainActivity).setMapFragment(latLangBundle)
    }

    companion object {

        /**
         * This function will set provide an instance of the fragment.
         * */
        fun newInstance() = UserRecordsFragment()
    }

}