package pl.net.gwynder.multitenency.support.script.execution.entities

data class ScriptProgress(
        val total: Int,
        val successful: Int,
        val skipped: Int,
        val failed: Int
) {

    val finished: Int
        get() = successful + skipped + failed

}