package com.example.data.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val storageReference: StorageReference
) {

    fun uploadAvatar(userID: Int, imageUri: Uri): Completable{
        return Completable.create { emitter ->
            storageReference.putFile(imageUri)
                .addOnSuccessListener { if(!emitter.isDisposed) emitter.onComplete() }
                .addOnFailureListener{ emitter.onError(it) }
        }
    }

}