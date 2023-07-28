package com.example.practiceapps.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practiceapps.database.model.UserListDetails
import com.example.practiceapps.databinding.UserListDetailLayoutBinding


class UserListAdapter(context: Context, userList: MutableList<UserListDetails>) :
    RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    private val mContext = context
    private var mUserList: MutableList<UserListDetails> = userList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding =
            UserListDetailLayoutBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        return UserListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mUserList.size
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val userDetail = mUserList[position]

        holder.binding.firstNameTV.text = userDetail.firstName
        holder.binding.lastNameTV.text = userDetail.lastName
        holder.binding.emailTV.text = userDetail.email
    }

    /**
     * This function will redraw the adapter view with latest data.
     *
     * @param newUserDetailsList List of UserListDetails object.
     * */
    fun refreshUserList(newUserDetailsList: List<UserListDetails>) {
        mUserList.clear()
        mUserList.addAll(newUserDetailsList)
        notifyDataSetChanged()
    }

    inner class UserListViewHolder(val binding: UserListDetailLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}