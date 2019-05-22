package pl.net.gwynder.multitenency.support.script.execution.services

enum class ScriptTransactionBehavior(
        val description: String
) {

    NO_TRANSACTION("No transaction"),
    ROLLBACK_ALWAYS("Rollback always"),
    ROLLBACK_ON_ERROR("Rollback on error, commit on success"),

}