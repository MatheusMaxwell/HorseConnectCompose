package br.com.horseconnect.impl.domain.model

data class Farm(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val creation: List<String>,
    val proprietaryId: String
) {
    companion object {
        val mock = Farm(
            id = "1",
            name = "Haras Bela Vista",
            address = "Estrada Rural, km 5",
            city = "Itu",
            state = "SP",
            country = "Brasil",
            creation = listOf("2020"),
            proprietaryId = "1"
        )
    }
}