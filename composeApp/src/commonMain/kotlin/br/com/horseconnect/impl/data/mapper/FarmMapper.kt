package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.FarmData
import br.com.horseconnect.impl.domain.model.Farm
import dev.gitlive.firebase.firestore.DocumentSnapshot

fun FarmData.toDomain(): Farm {
    return Farm(
        id = id,
        name = name,
        address = address,
        city = city,
        state = state,
        country = country,
        creation = creation,
        imageLogoUrl = imageLogoUrl,
        primaryColor = primaryColor,
        proprietaryId = proprietaryId
    )
}