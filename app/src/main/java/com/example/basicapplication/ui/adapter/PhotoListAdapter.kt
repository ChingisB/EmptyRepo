package com.example.basicapplication.ui.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basicapplication.R
import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.databinding.PhotoViewHolderBinding
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.util.BaseListAdapter

class PhotoListAdapter(private var onItemClickListener: (Photo) -> Unit): BaseListAdapter<Photo, PhotoListAdapter.PhotoViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class PhotoViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding = PhotoViewHolderBinding.bind(view)
        fun bind(photo: Photo) {
            binding.apply {
                root.setOnClickListener { onItemClickListener.invoke(photo) }
                Glide.with(view.context).load(Config.MEDIA_URL + photo.image?.name).into(image)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(data: List<Photo>) {
        differ.submitList(differ.currentList.plus(data))
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun clearList() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_view_holder, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

}