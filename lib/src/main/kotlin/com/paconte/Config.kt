package com.paconte

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Paths

data class Config(
    val csv: CsvConfig,
)

data class CsvConfig(
    val delimiter: String,
    val padelScoreStart: Int,
)

object ConfigLoader {
    private val mapper = YAMLMapper().registerModule(KotlinModule())

    val config: Config

    init {
        val configFile = Paths.get("src/main/resources/config.yaml").toFile()
        config = mapper.readValue(configFile)
    }
}

