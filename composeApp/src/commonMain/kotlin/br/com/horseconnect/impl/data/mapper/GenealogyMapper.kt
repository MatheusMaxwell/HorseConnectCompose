package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.GenealogyData
import br.com.horseconnect.impl.domain.model.Genealogy

fun GenealogyData.toDomain(): Genealogy {
    return Genealogy(
        id = id,
        father = father,
        fatherGrandFather = fatherGrandFather,
        fatherGrandFatherGreatGrandFather = fatherGrandFatherGreatGrandFather,
        fatherGrandFatherGreatGrandMother = fatherGrandFatherGreatGrandMother,
        fatherGrandMother = fatherGrandMother,
        fatherGrandMotherGreatGrandFather = fatherGrandMotherGreatGrandFather,
        fatherGrandMotherGreatGrandMother = fatherGrandMotherGreatGrandMother,
        mother = mother,
        motherGrandFather = motherGrandFather,
        motherGrandFatherGreatGrandFather = motherGrandFatherGreatGrandFather,
        motherGrandFatherGreatGrandMother = motherGrandFatherGreatGrandMother,
        motherGrandMother = motherGrandMother,
        motherGrandMotherGreatGrandFather = motherGrandMotherGreatGrandFather,
        motherGrandMotherGreatGrandMother = motherGrandMotherGreatGrandMother
    )
}