package pl.net.gwynder.multitenency.support.script.execution.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class ScriptQuery(
        @Column(nullable = false)
        @Lob
        var query: String = ""
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ScriptQuery

        if (query != other.query) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + query.hashCode()
        return result
    }

}