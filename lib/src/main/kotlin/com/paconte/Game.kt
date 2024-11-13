package com.paconte

val delimiter = ";" // TODO: make this configurable later

interface IGame {
    val federation: String
    val tournamentName: String
    val tier: String
    val division: String
    val dateStart: String
    val dateEnd: String
    val round: String
    val medal: String
    val local: ITeam
    val visitor: ITeam
    val score: Any
}

interface ITeam {
    fun name(): String
}

interface IScore {
    val score: List<Int>
}
