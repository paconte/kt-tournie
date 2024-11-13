package com.paconte

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PadelGameTest {
    @Test
    fun testPadelGameFromString() {
        val input =
            @Suppress("ktlint:standard:max-line-length")
            // "GERMANY;GPS 1000 Cuxhaven - Herren;GPS-1000;MO;05.07.2020;;;PoolA;Gold;Ströhl;Richard;Messerschmidt;Jonas;Viebrock;Lars;Hagen;Ralf;2;6;0;6;0"
            "GERMANY;GPS 1000 Cuxhaven - Herren;GPS-1000;MO;05.07.2020;;PoolA;Gold;Ströhl;Richard;Messerschmidt;Jonas;Viebrock;Lars;Hagen;Ralf;6;0;6;0"

        val padelGame = PadelGame.fromString(input)

        assertEquals("GERMANY", padelGame.federation)
        assertEquals("GPS 1000 Cuxhaven - Herren", padelGame.tournamentName)
        assertEquals("GPS-1000", padelGame.tier)
        assertEquals("MO", padelGame.division)
        assertEquals("05.07.2020", padelGame.dateStart)
        assertEquals("", padelGame.dateEnd)
        assertEquals("PoolA", padelGame.round)
        assertEquals("Gold", padelGame.medal)
        assertEquals("Ströhl-Richard-Messerschmidt-Jonas", padelGame.local.name())
        assertEquals("Viebrock-Lars-Hagen-Ralf", padelGame.visitor.name())
        assertEquals(listOf(6, 0, 6, 0), padelGame.score.score)
    }

    @Test
    fun testPadelGameFromStringInvalidInput() {
        val input = "Invalid;Input"
        assertFailsWith<IllegalArgumentException> {
            PadelGame.fromString(input)
        }
    }
}
