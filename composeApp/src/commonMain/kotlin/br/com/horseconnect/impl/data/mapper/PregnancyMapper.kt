package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.PregnancyData
import br.com.horseconnect.impl.domain.model.Pregnancy

fun PregnancyData.toDomain(id: String): Pregnancy {
    return Pregnancy(
        id = id,
        date = date,
        farmId = farmId,
        femaleId = femaleId,
        femaleName = femaleName,
        maleId = maleId,
        maleName = maleName,
        receiverId = receiverId,
        sex = sex,
        status = status,
        type = type
    )
}
