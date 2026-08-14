package br.com.horseconnect.impl.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pregnancy(
    val id: String,
    val date: String,
    val farmId: String,
    val femaleId: String,
    val femaleName: String,
    val maleId: String,
    val maleName: String,
    val receiverId: String,
    val sex: String,
    val status: String,
    val type: String
) {

    companion object {
        val mock = Pregnancy(
            id = "1",
            date = "2023-04-15T17:13:00Z",
            farmId = "farm1",
            femaleId = "female1",
            femaleName = "Femea",
            maleId = "male1",
            maleName = "Macho",
            receiverId = "receiver1",
            status = "Confirmed",
            type = "Natural",
            sex = "Female"
        )
    }
}
