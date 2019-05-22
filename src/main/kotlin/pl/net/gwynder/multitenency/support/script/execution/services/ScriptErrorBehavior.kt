package pl.net.gwynder.multitenency.support.script.execution.services

enum class ScriptErrorBehavior(
        val description: String
) {

    CONTINUE("Continue"),
    STOP_ALL("Stop all execution"),
    STOP_DATABASE("Stop for current database"),

}