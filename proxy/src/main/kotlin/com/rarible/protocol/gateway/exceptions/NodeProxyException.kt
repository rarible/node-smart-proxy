package com.rarible.protocol.gateway.exceptions

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import org.springframework.http.HttpStatus

sealed class NodeProxyException(
    message: String,
    val status: HttpStatus
) : Exception(message)

class AppNotFoundApiException(blockchain: Blockchain, app: App) : NodeProxyException(
    message = getNotFoundMessage(blockchain, app),
    status = HttpStatus.NOT_FOUND
)

private fun getNotFoundMessage(blockchain: Blockchain, app: App): String {
    return "$blockchain node with app $app not found"
}