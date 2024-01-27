package com.example.myapplication.repository.model

data class CountryResponse(
    val name: Name,
    val capital: List<String>,
    val region: String,
    val population: Int,
    val landlocked: Boolean,
    val flags: Flag,
    val coatOfArms: Emblem
)

data class Name(
    val common: String,
    val official: String
)

data class Flag(
    val png: String
)

data class Emblem(
    val png: String
)
