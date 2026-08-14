package br.com.horseconnect.impl.domain.mapper

import br.com.horseconnect.impl.data.model.GenealogyData
import br.com.horseconnect.impl.domain.model.Genealogy

fun Genealogy.toData(): GenealogyData {
    return GenealogyData(
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
