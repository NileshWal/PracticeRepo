package com.example.practiceapps.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.practiceapps.R
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.databinding.FragmentUserRecordsBinding
import com.example.practiceapps.utils.CommonUtils
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.viewmodel.UserRecordsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserRecordsFragment : Fragment() {

    private lateinit var binding: FragmentUserRecordsBinding
    private val viewModel: UserRecordsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRecordsBinding.inflate(layoutInflater, container, false)
        setUpLiveData()
        return binding.root.apply {
            binding.composeView.setContent {
                MaterialTheme(
                    colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
                ) {
                    PublicApiComposeView(viewModel.userRecordsLiveData)
                }
            }
            callApi(requireActivity())
        }
    }

    /**
     * This function will set the livedata observables to listen to the change in loader value.
     * */
    private fun setUpLiveData() = viewModel.loaderLiveData.observe(this) {
        showLoader(it.shouldShow)
        if (it.responseStatus != ResponseStatus.NO_ISSUE) {
            CommonUtils.showToastMessage(requireActivity(), it.responseStatus.toString())
        }
    }

    /**
     * This function will call the User List API.
     *
     * @param context Context is needed to check if the device has internet for the API call.
     * */
    private fun callApi(context: Context) {
        showLoader(true)
        if (CommonUtils.isConnected(context)) {
            viewModel.callUserRecordsApi(0, 20)
        } else {
            showLoader(false)
            CommonUtils.showToastMessage(context, getString(R.string.no_internet))
        }
    }

    /**
     * This function will call the User List API.
     *
     * @param context Context is needed to check if the device has internet for the API call.
     * */
    private fun fetchOrderedListFromDB(context: Context, isAscending: Boolean) {
        showLoader(true)
        if (CommonUtils.isConnected(context)) {
            if (isAscending) {
                viewModel.makeUserListAscending()
            } else {
                viewModel.makeUserListDescending()
            }
        } else {
            showLoader(false)
            CommonUtils.showToastMessage(context, getString(R.string.no_internet))
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
     * @param apiEntriesLiveData The SnapshotStateList of PublicApisListDetails
     * */
    @Composable
    private fun PublicApiComposeView(apiEntriesLiveData: SnapshotStateList<UserRecordsListDetails>) {
        Scaffold(
            content = {
                Row {
                    Column(
                        modifier = Modifier
                            .padding(6.dp, 8.dp, 6.dp, 8.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        ReorderView()
                        LazyColumn {
                            items(items = apiEntriesLiveData) { item ->
                                PublicApisListItem(item)
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
     * @param userRecordsListDetails The PublicApisListDetails object
     * */
    @Composable
    private fun PublicApisListItem(userRecordsListDetails: UserRecordsListDetails) {
        Card(
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
                        "Name: ",
                        userRecordsListDetails.firstName.toString()
                                + " "
                                + userRecordsListDetails.lastName.toString()
                    )

                    CustomRowTextView("Gender: ", userRecordsListDetails.gender.toString())
                    CustomRowTextView("DoB: ", userRecordsListDetails.dateOfBirth.toString())
                    CustomRowTextView(
                        "Email & Phone: ",
                        userRecordsListDetails.email.toString()
                                + ", "
                                + userRecordsListDetails.phone.toString()
                    )
                    CustomRowTextView(
                        "Address: ",
                        userRecordsListDetails.street.toString()
                                + ", "
                                + userRecordsListDetails.city.toString()
                                + ", "
                                + userRecordsListDetails.state.toString()
                                + ", "
                                + userRecordsListDetails.country.toString()
                                + ", "
                                + userRecordsListDetails.zipcode.toString()
                                + ", "
                                + userRecordsListDetails.latitude.toString()
                                + " - "
                                + userRecordsListDetails.longitude.toString()
                    )
                    CustomRowTextView("Job: ", userRecordsListDetails.job.toString())
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
    private fun CustomRowTextView(textTitle: String, textValue: String) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Text(
                text = textTitle,
                textAlign = TextAlign.Start,
                color = colorResource(R.color.black),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = textValue,
                textAlign = TextAlign.Start,
                color = colorResource(R.color.black),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.clickable { fetchOrderedListFromDB(requireActivity(), true) }
            )

            Image(
                painterResource(R.drawable.descending),
                contentDescription = "Descend",
                modifier = Modifier.clickable { fetchOrderedListFromDB(requireActivity(), false) }
            )
        }
    }

    companion object {

        /**
         * This function will set provide an instance of the fragment.
         * */
        fun newInstance() =
            UserRecordsFragment()
    }

}