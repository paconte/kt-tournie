package com.paconte

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Paths

data class Config(
    val csv: CsvConfig,
    val padel: PadelConfig,
)

data class CsvConfig(
    val delimiter: String,
    @JsonProperty("padel_score_start") val padelScoreStart: Int,
)

data class PadelConfig(
    @JsonProperty("medal_order") val medalsOrder: List<String>,
    @JsonProperty("round_order") val roundsOrder: List<String>,
)

object ConfigLoader {
    // private val mapper = YAMLMapper().registerModule(KotlinModule())
    private val mapper = YAMLMapper().registerModule(KotlinModule.Builder().build())

    val config: Config

    init {
        val configFile = Paths.get("src/main/resources/config.yaml").toFile()
        config = mapper.readValue(configFile)
    }
}
