package br.com.horseconnect.impl.domain.model

data class Genealogy(
    val id: String,
    val father: String,
    val fatherGrandFather: String,
    val fatherGrandFatherGreatGrandFather: String,
    val fatherGrandFatherGreatGrandMother: String,
    val fatherGrandMother: String,
    val fatherGrandMotherGreatGrandFather: String,
    val fatherGrandMotherGreatGrandMother: String,
    val mother: String,
    val motherGrandFather: String,
    val motherGrandFatherGreatGrandFather: String,
    val motherGrandFatherGreatGrandMother: String,
    val motherGrandMother: String,
    val motherGrandMotherGreatGrandFather: String,
    val motherGrandMotherGreatGrandMother: String
)
