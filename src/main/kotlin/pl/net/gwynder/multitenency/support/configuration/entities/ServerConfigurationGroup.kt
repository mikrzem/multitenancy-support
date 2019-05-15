package pl.net.gwynder.multitenency.support.configuration.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import javax.persistence.*

@Entity
class ServerConfigurationGroup(
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "configurationId", nullable = false)
        var configuration: ServerConfiguration = ServerConfiguration(),
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "parentId")
        var parent: ServerConfigurationGroup? = null,
        @Column(nullable = false)
        var name: String = "",
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "DatabaseServerConfigurationGroupDatabase", joinColumns = [JoinColumn(name = "groupId")])
        @Column(name = "schema")
        var databases: MutableSet<String> = HashSet()
) : BaseEntity() {

    fun display(): String {
        val parent = this.parent
        return if (parent == null) {
            name
        } else {
            "${parent.display()} / name"
        }
    }

}