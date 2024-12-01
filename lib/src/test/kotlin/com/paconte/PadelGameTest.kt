package com.paconte

import com.paconte.ConfigLoader.config
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PadelGameTest {
    @Test
    fun testCompareMedals() {
        assertEquals(0, medalComparator.compare("Gold", "Gold"))
        assertEquals(1, medalComparator.compare("Gold", "Silver"))
        assertEquals(-1, medalComparator.compare("Bronze", "Silver"))

        val medals = config.padel.medalsOrder
        for (i in medals.indices) {
            for (j in medals.indices) {
                val expected = i.compareTo(j)
                assertEquals(expected, medalComparator.compare(medals[i], medals[j]))
            }
        }
    }

    @Test
    fun testCompareRounds() {
        assertEquals(0, roundComparator.compare("KO1", "KO1"))
        assertEquals(-1, roundComparator.compare("KO1", "KO2"))
        assertEquals(1, roundComparator.compare("KO2", "POS3"))
        val rounds = config.padel.roundsOrder
        for (i in rounds.indices) {
            for (j in rounds.indices) {
                val expected = i.compareTo(j)
                assertEquals(expected, roundComparator.compare(rounds[i], rounds[j]))
            }
        }
    }

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
        val games = mutableListOf<PadelGame>()
        Files.lines(path).forEach {
            games.add(PadelGame.fromString(it))
        }
        assertEquals(7, games.size)

        val sortedGames = games.sortedDescending()

        // Create a temporary file
        val tempFile = Files.createTempFile("sorted_games", ".csv")

        // Write the sorted games to the temporary file
        Files.newBufferedWriter(tempFile).use { writer ->
            sortedGames.forEach { writer.write(it.toString() + System.lineSeparator()) }
        }

        // Read the original file and the temporary file
        val originalContent = Files.readAllLines(path)
        val tempFileContent = Files.readAllLines(tempFile)

        // Compare the contents of both files
        assertEquals(originalContent, tempFileContent)
    }

    @Test
    fun testGER2016() {
        val path = Paths.get("src/test/resources/GER_tournaments_2016_utf8_sorted.csv")
        // updateFile(path)
        checkYearFile(path)
    }

    @Test
    fun testPadelGameFromStringInvalidInput() {
        val input = "Invalid;Input"
        assertFailsWith<IllegalArgumentException> {
            PadelGame.fromString(input)
        }
    }

    private fun checkYearFile(path: Path) {
        val games = readGamesFromFile(path, PadelGame::fromString)
        val sortedGames = games.sortedDescending()
        val tempFile = Files.createTempFile("sorted_games", ".csv")
        writeGamesToFile(sortedGames, tempFile)
        compareTwoFiles(path, tempFile)
    }

    private fun compareTwoFiles(
        orig: Path,
        new: Path,
    ) {
        // Read the original file and the temporary file
        val originalContent = Files.readAllLines(orig)
        val tempFileContent = Files.readAllLines(new)

        // Compare the contents of both files. We compare line by line instead of the whole content for debugging purposes
        assertEquals(originalContent.size, tempFileContent.size)
        for (i in originalContent.indices) {
            assertEquals(originalContent[i], tempFileContent[i])
        }
        // assertEquals(originalContent, tempFileContent)
    }
}
