package pl.net.gwynder.multitenency.support.database.definition.query

interface QueryParser {

    fun parseQuery(multipleQueries: String, options: QueryParserOptions): List<ParsedQuery>

}