package com.example.basicapplication.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basicapplication.R
import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.databinding.PhotoViewHolderBinding
import com.example.basicapplication.model.retrofit_model.Photo

class PhotoListAdapter(
    private var photoList: List<Photo>,
    private var onItemClickListener: (Photo) -> Unit
) :
    RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {


    inner class PhotoViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding = PhotoViewHolderBinding.bind(view)
        fun bind(photo: Photo) {
            binding.apply {
                root.setOnClickListener { onItemClickListener.invoke(photo) }
                Glide.with(view.context)
                    .load(Config.mediaUrl + photo.image?.name)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_view_holder, parent, false)

        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(photos: List<Photo>) {
        this.photoList = this.photoList.plus(photos)
        Log.e("values for adapter", photos.toString())
        notifyDataSetChanged()
    }
}