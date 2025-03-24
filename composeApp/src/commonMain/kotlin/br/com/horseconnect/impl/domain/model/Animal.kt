package br.com.horseconnect.impl.domain.model

import br.com.horseconnect.impl.data.model.OwnerData
import kotlinx.serialization.Serializable

@Serializable
data class Animal(
    val id: String,
    val birthDate: String,
    val coat: String,
    val farmId: String,
    val imageId: String,
    val imageUrl: String,
    val isLive: Boolean,
    val name: String,
    val race: String,
    val sex: String,
    val types: List<AnimalType>,
    val ownerOf: Double,
    val otherOwners: List<Owner>
) {
    companion object {
        val mock = Animal(
            id = "1",
            birthDate = "2022-01-01",
            coat = "Alazã",
            farmId = "1",
            imageId = "1",
            imageUrl = "",
            isLive = true,
            name = "Monarca de Alcatéia",
            race = "Mangalarga Marchador",
            sex = "Macho",
            types = listOf(AnimalType.STALLION, AnimalType.DONOR),
            ownerOf = 0.5,
            otherOwners = listOf(
                Owner("João", 0.5)
            )
        )
    }
}

@Serializable
data class Owner(
    val name: String,
    val ownerOf: Double
)

enum class AnimalType(val value: String) {
    STALLION("Garanhão"),
    DONOR("Doadora"),
    MOTHER("Matriz"),
    FOAL("Potro"),
    COMPETITORS("Competidor"),
    GELDING("Castrado"),
    RECEIVER("Receptora");

    companion object {
        fun enumOf(value: String): AnimalType {
            return AnimalType.entries.first { it.value == value }
        }
    }
}