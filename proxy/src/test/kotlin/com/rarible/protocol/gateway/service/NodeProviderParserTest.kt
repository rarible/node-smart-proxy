package com.rarible.protocol.gateway.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI

class NodeProviderParserTest {
    @Test
    fun `fetch quiknode`() {
        val node = URI.create("https://empty-prettiest-putty.matic.quiknode.pro/0000000000000000000000000000000000000000")
        val tld = NodeProviderParser.parser(node)
        assertThat(tld).isEqualTo("quiknode")
    }

    @Test
    fun `fetch alchemy`() {
        val node = URI.create("https://eth-mainnet.g.alchemy.com/v2/00")
        val tld = NodeProviderParser.parser(node)
        assertThat(tld).isEqualTo("alchemy")
    }

    @Test
    fun `fetch mainnet-node`() {
        val node = URI.create("http://mainnet-node.ethereum-node:8545")
        val tld = NodeProviderParser.parser(node)
        assertThat(tld).isEqualTo("mainnet-node")
    }
}