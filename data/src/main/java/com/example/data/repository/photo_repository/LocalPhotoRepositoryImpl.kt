package com.example.data.repository.photo_repository

import com.example.data.Constants
import com.example.data.SingleThreadScheduler
import com.example.data.database.model.ImageObject
import com.example.data.database.model.PhotoObject
import com.example.data.mapper.image.ImageEntityToObjectMapper
import com.example.data.mapper.image.ImageObjectToEntityMapper
import com.example.data.mapper.photo.PhotoEntityToObjectMapper
import com.example.data.mapper.photo.PhotoObjectToEntityMapper
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.domain.entity.PhotoEntity
import com.example.domain.repository.photo_repository.LocalPhotoRepository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.kotlin.delete
import io.realm.kotlin.where
import javax.inject.Inject

class LocalPhotoRepositoryImpl @Inject constructor(
    private val realm: Realm,
    @SingleThreadScheduler private val singleThreadScheduler: Scheduler
) : LocalPhotoRepository {

    private val photoObjectToEntityMapper = PhotoObjectToEntityMapper(ImageObjectToEntityMapper())
    private val photoEntityToObjectMapper = PhotoEntityToObjectMapper(ImageEntityToObjectMapper())

    override fun getPhotos(new: Boolean, popular: Boolean, query: String, page: Int): Single<PaginatedPhotosEntity> =
        Single.create {emitter ->
            val dbQuery = realm.where<PhotoObject>()
                .equalTo(PhotoObject::isNew.name, new)
                .equalTo(PhotoObject::popular.name, popular)
                .contains(PhotoObject::name.name, query)
            if(dbQuery.count() == 0L){ emitter.onError(Error("No items to show")) }
            else{ emitter.onSuccess(transformDBQueryToEntity(dbQuery, page)) }
        }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())

    override fun getUserPhotos(userId: Int, page: Int): Single<PaginatedPhotosEntity> =
        Single.fromCallable { transformDBQueryToEntity(realm.where<PhotoObject>().equalTo(PhotoObject::userID.name, userId), page) }
            .subscribeOn(singleThreadScheduler)
            .observeOn(AndroidSchedulers.mainThread())

    override fun getSavedPhotos(page: Int): Single<PaginatedPhotosEntity> =
        Single.fromCallable { transformDBQueryToEntity(realm.where<PhotoObject>().equalTo(PhotoObject::isSaved.name, true), page) }
            .subscribeOn(singleThreadScheduler)
            .observeOn(AndroidSchedulers.mainThread())

    override fun getById(id: Int): Single<PhotoEntity> =
        Single.fromCallable {
            realm.where<PhotoObject>().equalTo(PhotoObject::id.name, id).findFirst()?.let { photoObjectToEntityMapper.convert(it) }
        }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())

    override fun delete(id: Int): Completable {
        TODO("Not yet implemented")
    }

    override fun savePhoto(photoEntity: PhotoEntity): Completable =
        Completable.fromAction {
            val photoObject = photoEntityToObjectMapper.convert(photoEntity)
            photoObject.isSaved = true
            realm.executeTransaction { r -> r.insertOrUpdate(photoObject) }
        }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())


    override fun removePhoto(photoEntity: PhotoEntity): Completable =
        Completable.fromAction {
            val photoObject = photoEntityToObjectMapper.convert(photoEntity)
            photoObject.isSaved = false
            realm.executeTransaction { r -> r.insertOrUpdate(photoObject) }
        }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())

    override fun insertPhoto(photoEntity: PhotoEntity): Completable {
        return Completable.fromAction {
            val photoObject = photoEntityToObjectMapper.convert(photoEntity)
            realm.executeTransaction { r ->
                try {
                    r.insert(photoObject)
                } catch (e: Exception) { }
            }
        }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())
    }

    override fun clearLocalPhotos(): Completable {
        return Completable.fromAction {
            realm.executeTransaction { r ->
                r.delete<PhotoObject>()
                r.delete<ImageObject>()
            }
        }.subscribeOn(singleThreadScheduler).observeOn(AndroidSchedulers.mainThread())
    }


    private fun transformDBQueryToEntity(dbQuery: RealmQuery<PhotoObject>, page: Int): PaginatedPhotosEntity {
        return PaginatedPhotosEntity(
            countOfPages = (dbQuery.count() / Constants.ITEMS_PER_PAGE).toInt() + 1,
            data = paginateList(dbQuery.findAll(), page).map(photoObjectToEntityMapper::convert).toMutableList(),
            itemsPerPage = Constants.ITEMS_PER_PAGE,
            totalItems = dbQuery.count().toInt()
        )
    }

    private fun <T> paginateList(list: List<T>, page: Int): List<T> =
        list.subList(
            Integer.min((Constants.ITEMS_PER_PAGE * (page - 1)).toInt(), list.size),
            Integer.min(Constants.ITEMS_PER_PAGE.toInt() * page, list.size)
        )

}