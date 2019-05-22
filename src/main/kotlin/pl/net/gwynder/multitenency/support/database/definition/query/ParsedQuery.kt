package pl.net.gwynder.multitenency.support.database.definition.query

data class ParsedQuery(
        val index: Int,
        val query: String
) {
    val display: String
        get() = "${index.toString().padStart(4, '0')}: $query"
}