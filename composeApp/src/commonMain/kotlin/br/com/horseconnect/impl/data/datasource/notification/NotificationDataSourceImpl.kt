package br.com.horseconnect.impl.data.datasource.notification

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.data.model.NotificationData
import br.com.horseconnect.impl.domain.model.Notification
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.LocalCacheSettings
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.firestoreSettings

private const val NOTIFICATIONS_COLLECTION = "notifications"
private const val USER_ID_FIELD = "userId"

class NotificationDataSourceImpl: NotificationDataSource {

    private val db = Firebase.firestore

    init {
        db.settings = firestoreSettings {
            cacheSettings = LocalCacheSettings.Persistent.newBuilder().build()
        }
    }

    override suspend fun getNotifications(userId: String): ResponseResult<List<Notification>> {
        return try {
            val snapshot = db
                .collection(NOTIFICATIONS_COLLECTION)
                .where { USER_ID_FIELD equalTo userId }
                .get()

            val notifications = snapshot.documents.map {
                val data = it.data(strategy = NotificationData.serializer())
                data.toDomain(it.id)
            }
            ResponseResult.Success(notifications)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun addNotification(notification: NotificationData): ResponseResult<Unit> {
        return try {
            db.collection(NOTIFICATIONS_COLLECTION).add(notification)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun deleteNotification(notificationId: String): ResponseResult<Unit> {
        return try {
            db.collection(NOTIFICATIONS_COLLECTION).document(notificationId).delete()
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun deleteNotifications(notificationIds: List<String>): ResponseResult<Unit> {
        return try {
            notificationIds.forEach { id ->
                db.collection(NOTIFICATIONS_COLLECTION).document(id).delete()
            }
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}
