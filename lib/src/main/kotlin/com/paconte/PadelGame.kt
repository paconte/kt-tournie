package com.paconte

interface IPadelTeam : ITeam {
    val lastNameP1: String
    val firstNameP1: String
    val lastNameP2: String
    val firstNameP2: String

    override fun name(): String = "$lastNameP1-$firstNameP1-$lastNameP2-$firstNameP2"
}

class PadelScore(
    override val score: List<Int>,
) : IScore {
    init {
        require(score.size > 1) { "Padel games have at least one set" }
        require(score.size % 2 == 0) { "Padel games have an even number of sets" }
    }

    companion object {
        fun fromString(input: String): PadelScore {
            val setScores = input.split(delimiter).map { it.toInt() }
            return PadelScore(setScores)
        }
    }
}

class PadelTeam(
    override val lastNameP1: String,
    override val firstNameP1: String,
    override val lastNameP2: String,
    override val firstNameP2: String,
) : IPadelTeam {
    companion object {
        fun fromString(input: String): PadelTeam {
            val parts = input.split(delimiter)
            require(parts.size == 4) { "Padel teams have exactly 4 elements" }
            return PadelTeam(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
            )
        }
    }
}

// GERMANY;GPS 1000 Cuxhaven - Herren;GPS-1000;MO;05.07.2020;;;PoolA;Gold;4;;;Ströhl;Richard;Messerschmidt;Jonas;Viebrock;Lars;Hagen;Ralf;2;6;0;6;0;;;;;;
class PadelGame(
    override val federation: String,
    override val tournamentName: String,
    override val tier: String,
    override val division: String,
    override val dateStart: String,
    override val dateEnd: String,
    override val round: String,
    override val medal: String,
    override val local: PadelTeam,
    override val visitor: PadelTeam,
    override val score: PadelScore,
) : IGame {
    companion object {
        fun fromString(input: String): PadelGame {
            val parts = input.split(delimiter)
            require(parts.size > 17) { "Padel games have at least 18 elements" }
            val localString = "${parts[8]};${parts[9]};${parts[10]};${parts[11]}"
            val visitorString = "${parts[12]};${parts[13]};${parts[14]};${parts[15]}"
            val scoreString = "${parts[16]};${parts[17]};${parts[18]};${parts[19]}"
            return PadelGame(
                // federation
                parts[0],
                // tournamentName
                parts[1],
                // tier
                parts[2],
                // division
                parts[3],
                // dateStart
                parts[4],
                // dateEnd
                parts[5],
                // round
                parts[6],
                // medal
                parts[7],
                // local
                PadelTeam.fromString(localString),
                // visitor
                PadelTeam.fromString(visitorString),
                // score
                PadelScore.fromString(scoreString),
            )
        }
    }
}
