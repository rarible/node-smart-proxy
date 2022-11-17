package com.rarible.protocol.gateway.model

sealed class NodeEndpoints {
    abstract val main: MainNode?
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
    override val main: MainNode? = null,
    override val reserve: ReserveNode? = null
) : NodeEndpoints()
