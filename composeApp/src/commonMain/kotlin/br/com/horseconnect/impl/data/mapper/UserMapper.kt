package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.domain.model.User
import dev.gitlive.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): User = User(
    id = uid,
    name = displayName ?: "",
    email = email ?: "",
    phone = phoneNumber ?: ""
)