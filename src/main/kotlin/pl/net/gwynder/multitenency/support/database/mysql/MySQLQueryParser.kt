package pl.net.gwynder.multitenency.support.database.mysql

import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParser
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParserOptions
import java.util.regex.Pattern

class MySQLQueryParser : QueryParser {

    private val separator = Pattern.compile("(;\r\n)|(;\n)")

    override fun parseQuery(multipleQueries: String, options: QueryParserOptions): List<ParsedQuery> {
        if (multipleQueries.trim() == "") {
            return listOf()
        }
        return multipleQueries.split(separator)
                .map { query -> query.trim() }
                .filter { query -> query != "" }
                .mapIndexed { index, query -> ParsedQuery(index, query) }
    }
}