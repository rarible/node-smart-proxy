package com.rarible.protocol.gateway.service.decoder

import com.rarible.protocol.gateway.model.NodeProxyResponse
import org.brotli.dec.BrotliInputStream
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.io.InputStream
import java.util.zip.GZIPInputStream

@Component
class NodeResponseDecoder {
    fun decode(response: NodeProxyResponse): InputStream {
        val bodyInputStream = response.body.inputStream()
        return when (response.headers.getFirst(HttpHeaders.CONTENT_ENCODING)) {
            "gzip" -> GZIPInputStream(bodyInputStream)
            "br" -> BrotliInputStream(bodyInputStream)
            else -> bodyInputStream
        }
    }
}