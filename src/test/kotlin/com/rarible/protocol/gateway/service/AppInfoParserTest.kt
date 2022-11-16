package com.rarible.protocol.gateway.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class AppInfoParserTest {
    private companion object {
        @JvmStatic
        fun paths(): Stream<String> = Stream.of(
            "/ethereum/nft",
            "/ethereum/nft/"
        )
    }

    @ParameterizedTest
    @MethodSource("paths")
    fun `parse - ok`(path: String) {
        val info = AppInfoParser.extractApp(path)
        assertThat(info?.blockchain).isEqualTo("ethereum")
        assertThat(info?.app).isEqualTo("nft")
    }

    @Test
    fun `parse - fail`() {
        val info = AppInfoParser.extractApp("/ethereum")
        assertThat(info).isNull()
    }
}