package com.example.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AvatarRepository @Inject constructor(private val storage: FirebaseStorage) {

    fun uploadAvatar(userId: Int, imageUri: Uri): Completable =
        Completable.create { emitter ->
            storage.getReference(storageReference(userId)).putFile(imageUri)
                .addOnSuccessListener { if (!emitter.isDisposed) emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }

    fun getUserAvatar(userId: Int): Single<Uri> =
        Single.create { emitter ->
            storage.getReference(storageReference(userId)).downloadUrl
                .addOnSuccessListener { if (!emitter.isDisposed) emitter.onSuccess(it) }
                .addOnFailureListener { emitter.onError(it) }
        }


    private fun storageReference(userId: Int) = "/$FOLDER_NAME/$userId.$FILE_EXTENSION"

    private companion object {
        const val FILE_EXTENSION = "PNG"
        const val FOLDER_NAME = "avatars"
    }

}