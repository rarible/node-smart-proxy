package com.rarible.protocol.gateway.model

sealed class NodeEndpoints {
    abstract val main: MainNode?

    //TODO AY Not really sure, but can be here N reserved nodes?
    abstract val reserve: ReserveNode?

    fun getMainIfEnabled(): Node? {
        return main?.ifEnabled()
    }

    fun getReserveIfEnabled(): Node? {
        return reserve?.ifEnabled()
    }

    private fun Node.ifEnabled(): Node? = takeIf { node -> node.enabled }
}

class GlobalNodeEndpoints(
    override val main: MainNode,
    override val reserve: ReserveNode? = null
) : NodeEndpoints()

class AppNodeEndpoints(
    val name: App,
    val failbackEnabled: Boolean = false,
    override val main: MainNode? = null,
    override val reserve: ReserveNode? = null
) : NodeEndpoints()
