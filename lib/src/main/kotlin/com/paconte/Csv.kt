package com.paconte

import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

private val logger = LoggerFactory.getLogger("CsvLogger")

fun removeTrailingDelimiter(
    line: String,
    delimiter: Char = ';',
): String = line.trimEnd(delimiter)

fun removeDelimitersAtPositions(
    line: String,
    delimiter: Char = ';',
    positions: List<Int> = listOf(5, 6, 9, 10, 11, 20),
): String {
    val parts = line.split(delimiter).toMutableList()
    positions.sortedDescending().forEach { pos ->
        if (pos < parts.size) {
            parts.removeAt(pos)
        }
    }
    return parts.joinToString(delimiter.toString())
}

fun updateLine(line: String): String = removeDelimitersAtPositions(removeTrailingDelimiter(line))

/**
 * Rewrites the file at the given path by updating each line in place.
 * It removes the trailing delimiters and the extra delimiters after the date. Example:
 * From:
 * GERMANY;Essener Open 2016;GPS-500;MO;31.01.2016;;;PoolA;Gold;3;;;Mordhorst;Andre;Herbert;Justus;Jiménez Monroy;Francisco;Mateu;Miguel;2;6;1;6;2;;;;;;
 * To:
 * GERMANY;Essener Open 2016;GPS-500;MO;31.01.2016;PoolA;Gold;3;Mordhorst;Andre;Herbert;Justus;Jiménez Monroy;Francisco;Mateu;Miguel;2;6;1;6;2
 */
fun updateFile(path: Path) {
    val lines = Files.readAllLines(path)
    val updatedLines = lines.map { updateLine(it) }
    Files.write(path, updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
}

fun <T : IGame<T>> readGamesFromFile(
    path: Path,
    fromString: (String) -> T,
): List<T> {
    val games = mutableListOf<T>()
    Files.lines(path).forEach {
        try {
            games.add(fromString(it))
        } catch (e: Exception) {
            logger.error("Error reading game from file: $it", e)
        }
    }
    return games
}

fun <T> writeGamesToFile(
    games: List<T>,
    path: Path,
) {
    Files.newBufferedWriter(path).use { writer ->
        games.forEach { writer.write(it.toString() + System.lineSeparator()) }
    }
}
