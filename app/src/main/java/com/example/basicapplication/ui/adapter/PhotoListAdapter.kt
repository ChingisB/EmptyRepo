package com.example.basicapplication.ui.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.basicapplication.R
import com.example.basicapplication.databinding.PhotoViewHolderBinding
import com.example.data.api.Config
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.PhotoEntity
import com.example.util.BaseListAdapter


class PhotoListAdapter(private var onItemClickListener: (PhotoEntity) -> Unit):
    BaseListAdapter<PaginatedPhotosEntity, PhotoListAdapter.PhotoViewHolder>() {

    private val differ = AsyncListDiffer(this, DifferCallback())

    inner class DifferCallback: DiffUtil.ItemCallback<PhotoEntity>(){
        override fun areItemsTheSame(oldItem: PhotoEntity, newItem: PhotoEntity): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PhotoEntity, newItem: PhotoEntity): Boolean = oldItem == newItem

    }

    inner class PhotoViewHolder(private val view: View) : ViewHolder(view) {
        private val binding = PhotoViewHolderBinding.bind(view)
        fun bind(photo: PhotoEntity) {
            binding.apply {
                root.setOnClickListener { onItemClickListener.invoke(photo) }
                Glide
                    .with(view.context)
                    .load(Config.MEDIA_URL + photo.image.name)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_view_holder, parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) = holder.bind(differ.currentList[position])

    @SuppressLint("NotifyDataSetChanged")
    override fun submitData(data: PaginatedPhotosEntity) {
        differ.submitList(data.data.toList())
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun clear() {
        differ.submitList(emptyList())
        notifyDataSetChanged()
    }

}