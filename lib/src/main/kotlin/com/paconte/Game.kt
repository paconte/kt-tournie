package com.paconte

interface IGame<T> : Comparable<T> {
    val federation: String
    val tournamentName: String
    val tier: String
    val division: String
    val date: String
    val round: String
    val medal: String
    val local: ITeam
    val visitor: ITeam
    val score: Any

    fun fromString(input: String): T
}

interface ITeam {
    fun name(): String
}

interface IScore {
    val score: List<Int>
}
