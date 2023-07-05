package com.example.data.repository


import com.example.data.realtime_database.PhotoViews
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.PhotoViewsRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class PhotoViewsRepositoryImpl @Inject constructor(private val photoViewsReference: DatabaseReference) : PhotoViewsRepository {

    override fun addPhotoView(photoEntity: PhotoEntity) {
        val itemReference = photoViewsReference.child(photoEntity.id.toString())
        itemReference.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val existingValue = snapshot.getValue(PhotoViews::class.java)
                    itemReference.setValue(PhotoViews(existingValue?.totalViews?.plus(1) ?: 1, photoEntity.userID))
                }

                override fun onCancelled(error: DatabaseError) = itemReference.setValue(PhotoViews(1, photoEntity.userID)).let { }
            }
        )
    }

    override fun getUserTotalViews(userId: Int, callback: (Long) -> Unit) {
        photoViewsReference.orderByChild(PhotoViews::userId.name).equalTo(userId.toDouble()).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) =
                    callback(snapshot.children.sumOf { it.getValue(PhotoViews::class.java)?.totalViews ?: 0 })

                override fun onCancelled(error: DatabaseError) = callback(0L)

            }
        )
    }

    override fun getPhotoViews(photoId: Int, callback: (Long) -> Unit) {
        photoViewsReference.child(photoId.toString()).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) = callback(snapshot.getValue(PhotoViews::class.java)?.totalViews ?: 0L)

                override fun onCancelled(error: DatabaseError) = callback(0L)
            }
        )
    }
}