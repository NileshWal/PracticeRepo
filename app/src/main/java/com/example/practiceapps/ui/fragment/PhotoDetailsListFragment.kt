package com.example.practiceapps.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapps.R
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.databinding.FragmentPhotoListBinding
import com.example.practiceapps.network.NetworkInstance
import com.example.practiceapps.repository.PhotoDetailsListRepository
import com.example.practiceapps.ui.adapter.PhotoDetailsAdapter
import com.example.practiceapps.ui.viewmodel.PhotoDetailsListViewModel
import com.example.practiceapps.utils.CommonUtils
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.LinkedList
import java.util.Stack


class PhotoDetailsListFragment : Fragment() {

    private val screenName = PhotoDetailsListFragment::class.java.simpleName
    private lateinit var binding: FragmentPhotoListBinding
    private val viewModel: PhotoDetailsListViewModel by viewModels {
        PhotoDetailsListViewModel.UserListViewModelFactory(
            PhotoDetailsListRepository(
                NetworkInstance.getInstance(NetworkInstance.IMAGE_LIST_BASE_URL),
                AppDatabase.getInstance(requireActivity())
            )
        )
    }
    private lateinit var adapter: PhotoDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoListBinding.inflate(layoutInflater)
        setupAdapter()
        setupLivedata()
        callApi(requireActivity())
        setupViewCallbacks()
        return binding.root
    }

    /**
     * This function will set adapter on the RecyclerView to list the users from API.
     * */
    private fun setupAdapter() {
        val initialList = viewModel.userListLiveData.value?.let { Collections.unmodifiableList(it) }
            ?: mutableListOf()
        adapter = PhotoDetailsAdapter(requireActivity(), initialList)
        binding.userListRV.setHasFixedSize(true)
        binding.userListRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.userListRV.adapter = adapter


        val animals = LinkedList<String>()
        animals.add("Dog")
        animals.add("Cat")
        animals.add("Cow")
        LogUtils.e(screenName, "LinkedList: $animals")

        val stackEx = Stack<Int>()
        stackEx.push(1)
        stackEx.push(4)
        stackEx.push(46)

    }

    /**
     * This function will call the User List API.
     *
     * @param context Context is needed to check if the device has internet for the API call.
     * */
    private fun callApi(context: Context) {
        showLoader(true)
        if (CommonUtils.isConnected(context)) {
            viewModel.callUsersApi(0, 40)
        } else {
            showLoader(false)
            CommonUtils.showToastMessage(context, getString(R.string.no_internet))
        }
    }

    /**
     * This function will set the livedata observables to listen to the change in API response
     * values and loader value.
     * */
    private fun setupLivedata() {
        viewModel.userListLiveData.observe(this) {
            adapter.refreshUserList(it)
        }
        viewModel.loaderLiveData.observe(this) {
            showLoader(it.shouldShow)
            if (it.responseStatus != ResponseStatus.NO_ISSUE) {
                CommonUtils.showToastMessage(requireActivity(), it.responseStatus.toString())
            }
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
     * This function will set callbacks to the views.
     * */
    private fun setupViewCallbacks() {
        binding.userListRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                /*if (!recyclerView.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    //Make the existing list in descending order when scrolled full down
                    LogUtils.e(screenName, "scroll down")
                    sortUserList(false)
                } else if (!recyclerView.canScrollVertically(-1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    //Make the existing list in ascending order when scrolled full up
                    LogUtils.e(screenName, "scroll up")
                    sortUserList(true)
                }*/
            }
        })
    }

    /**
     * This function will make the DB call to sort the user list in ascending or descending order.
     *
     * @param isAscending Whether to sort the user list in ascending or descending order.
     * */
    private fun sortUserList(isAscending: Boolean) {
        showLoader(true)
        if (isAscending) {
            viewModel.makeUserListAscending()
        } else {
            viewModel.makeUserListDescending()
        }
        CoroutineScope(Dispatchers.IO).launch {
            coroutineContext
        }
    }

    companion object {

        /**
         * This function will set provide an instance of the fragment.
         * */
        fun newInstance() =
            PhotoDetailsListFragment()
    }
}
