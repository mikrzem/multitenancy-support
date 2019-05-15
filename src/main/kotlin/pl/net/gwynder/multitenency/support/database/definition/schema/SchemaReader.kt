package pl.net.gwynder.multitenency.support.database.definition.schema

interface SchemaReader {

    fun selectDatabases(): List<String>

}