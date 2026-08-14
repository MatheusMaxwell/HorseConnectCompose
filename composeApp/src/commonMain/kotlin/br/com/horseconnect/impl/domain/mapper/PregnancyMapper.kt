package br.com.horseconnect.impl.domain.mapper

import br.com.horseconnect.impl.data.model.PregnancyData
import br.com.horseconnect.impl.domain.model.Pregnancy

fun Pregnancy.toData(): PregnancyData {
    return PregnancyData(
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