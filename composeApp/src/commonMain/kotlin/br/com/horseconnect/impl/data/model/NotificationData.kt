package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val userId: String,
    val type: String,
    val title: String,
    val message: String,
    val createdAt: String,
    val read: Boolean,
    val relatedEntityId: String? = null,
    val relatedEntityType: String? = null,
    val dueDate: String? = null
)
