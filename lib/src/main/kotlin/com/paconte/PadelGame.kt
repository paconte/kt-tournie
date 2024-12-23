package com.paconte

import com.paconte.ConfigLoader.config

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

    override fun toString(): String = score.joinToString(config.csv.delimiter)

    companion object {
        fun fromString(input: String): PadelScore {
            val setScores = input.split(config.csv.delimiter).map { it.toInt() }
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
    override fun toString(): String {
        val d = config.csv.delimiter
        return "$lastNameP1$d$firstNameP1$d$lastNameP2$d$firstNameP2"
    }

    companion object {
        fun fromString(input: String): PadelTeam {
            val parts = input.split(config.csv.delimiter)
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

val roundComparator =
    Comparator<String> { round1, round2 ->
        val roundOrder = config.padel.roundsOrder
        roundOrder.indexOf(round1).compareTo(roundOrder.indexOf(round2))
    }

val medalComparator =
    Comparator<String> { medal1, medal2 ->
        val medalsOrder = config.padel.medalsOrder
        medalsOrder.indexOf(medal1).compareTo(medalsOrder.indexOf(medal2))
    }

// GERMANY;GPS 1000 Cuxhaven - Herren;GPS-1000;MO;05.07.2020;;;PoolA;Gold;4;;;Ströhl;Richard;Messerschmidt;Jonas;Viebrock;Lars;Hagen;Ralf;2;6;0;6;0;;;;;;
class PadelGame(
    override val federation: String,
    override val tournamentName: String,
    override val tier: String,
    override val division: String,
    override val date: String,
    override val round: String,
    override val medal: String,
    override val local: PadelTeam,
    override val visitor: PadelTeam,
    override val score: PadelScore,
) : IGame<PadelGame> {
    override fun compareTo(other: PadelGame): Int =
        compareValuesBy(
            this,
            other,
            { it.federation },
            { it.tournamentName },
            { it.tier },
            { it.division },
            { medalComparator.compare(it.medal, other.medal) },
            { roundComparator.compare(it.round, other.round) },
        )

    override fun fromString(input: String): PadelGame = fromString(input)

    override fun toString(): String {
        val d = config.csv.delimiter
        return "$federation$d$tournamentName$d$tier$d$division$d$date$d$round$d$medal$d$local$d$visitor$d$score"
    }

    companion object {
        fun fromString(input: String): PadelGame {
            val parts = input.split(config.csv.delimiter)
            require(parts.size > 16) { "Padel games have at least 17 elements" }
            val localString = "${parts[7]};${parts[8]};${parts[9]};${parts[10]}"
            val visitorString = "${parts[11]};${parts[12]};${parts[13]};${parts[14]}"
            var scoreString = ""
            for (i in config.csv.padelScoreStart until parts.size) {
                require(parts[i].isNotEmpty()) { "Padel games have no empty fields" }
                scoreString += parts[i] + ";"
            }
            scoreString = scoreString.dropLast(1)
            return PadelGame(
                // federation
                parts[0],
                // tournamentName
                parts[1],
                // tier
                parts[2],
                // division
                parts[3],
                // date
                parts[4],
                // round
                parts[5],
                // medal
                parts[6],
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
