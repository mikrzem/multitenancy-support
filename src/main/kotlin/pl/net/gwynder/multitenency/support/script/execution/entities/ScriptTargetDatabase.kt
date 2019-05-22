package pl.net.gwynder.multitenency.support.script.execution.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class ScriptTargetDatabase(
        @Column(nullable = false)
        var database: String = ""
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ScriptTargetDatabase

        if (database != other.database) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + database.hashCode()
        return result
    }

}