package com.paconte

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PadelGameTest {
    @Test
    fun testPadelGameFromString() {
        val path = Paths.get("src/test/resources/padel_games.csv")
        val input2 = path.toFile().readLines().first()
        val input = Files.readString(path).trim()

        val padelGame = PadelGame.fromString(input)

        assertEquals("GERMANY", padelGame.federation)
        assertEquals("GPS 1000 Cuxhaven - Herren", padelGame.tournamentName)
        assertEquals("GPS-1000", padelGame.tier)
        assertEquals("MO", padelGame.division)
        assertEquals("05.07.2020", padelGame.date)
        assertEquals("PoolA", padelGame.round)
        assertEquals("Gold", padelGame.medal)
        assertEquals("Ströhl-Richard-Messerschmidt-Jonas", padelGame.local.name())
        assertEquals("Viebrock-Lars-Hagen-Ralf", padelGame.visitor.name())
        assertEquals(listOf(6, 0, 6, 0), padelGame.score.score)
    }

    @Test
    fun testGER2015() {
        val path = Paths.get("src/test/resources/GER_tournaments_2015_utf8.csv")
        Files.lines(path).forEach {
            PadelGame.fromString(it)
        }
    }

    @Test
    fun testPadelGameFromStringInvalidInput() {
        val input = "Invalid;Input"
        assertFailsWith<IllegalArgumentException> {
            PadelGame.fromString(input)
        }
    }
}
