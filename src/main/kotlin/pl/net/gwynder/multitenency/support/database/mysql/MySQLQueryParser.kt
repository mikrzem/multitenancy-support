package pl.net.gwynder.multitenency.support.database.mysql

import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParser
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParserOptions

class MySQLQueryParser : QueryParser {

    private val separator = "(;\r\n)|(;\n)"

    override fun parseQuery(multipleQueries: String, options: QueryParserOptions): List<ParsedQuery> {
        return multipleQueries.split(separator).map { query -> ParsedQuery(query) }
    }
}