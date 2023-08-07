package com.example.practiceapps.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practiceapps.R
import com.example.practiceapps.database.model.PhotoDetails
import com.example.practiceapps.databinding.PhotoListDetailLayoutBinding
import javax.inject.Inject


class PhotoDetailsAdapter @Inject constructor(
    context: Context,
    photoDetailList: MutableList<PhotoDetails>
) :
    RecyclerView.Adapter<PhotoDetailsAdapter.PhotoDetailsViewHolder>() {

    private val mContext = context
    private var mPhotoDetailList: MutableList<PhotoDetails> = photoDetailList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDetailsViewHolder {
        val binding =
            PhotoListDetailLayoutBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        return PhotoDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int = mPhotoDetailList.size

    override fun onBindViewHolder(holder: PhotoDetailsViewHolder, position: Int) {
        val photoDetails = mPhotoDetailList[position]

        holder.binding.userIdTV.text = photoDetails.id.toString()
        holder.binding.titleTV.text = photoDetails.title
        holder.binding.descriptionTV.text = photoDetails.description
        Glide.with(mContext).load(photoDetails.url).placeholder(R.drawable.avatar)
            .timeout(6000)
            .error(R.drawable.avatar)
            .into(holder.binding.avatarIV)
    }

    /**
     * This function will redraw the adapter view with latest data.
     *
     * @param newPhotoDetailsList List of PhotoDetails object.
     * */
    fun refreshUserList(newPhotoDetailsList: List<PhotoDetails>) {
        mPhotoDetailList.clear()
        mPhotoDetailList.addAll(newPhotoDetailsList)
        notifyDataSetChanged()
    }

    inner class PhotoDetailsViewHolder(val binding: PhotoListDetailLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}