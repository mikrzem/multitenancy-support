package pl.net.gwynder.multitenency.support.script.execution.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import javax.persistence.*

@Entity
class ScriptTarget(
        @Column(nullable = false)
        var name: String = "",
        @Column(nullable = false, length = 2000)
        var url: String = "",
        @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = [CascadeType.ALL])
        @JoinColumn(name = "targetId")
        var databases: MutableSet<ScriptTargetDatabase> = HashSet()
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ScriptTarget

        if (name != other.name) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }

}