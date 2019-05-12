package pl.net.gwynder.multitenency.support.configuration.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import javax.persistence.*

@Entity
class ServerConfigurationGroup(
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "configurationId", nullable = false)
        var configuration: ServerConfiguration = ServerConfiguration(),
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parentId")
        var parent: ServerConfigurationGroup? = null,
        @Column(nullable = false)
        var name: String = "",
        @ElementCollection
        @CollectionTable(name = "DatabaseServerConfigurationGroupDatabase", joinColumns = [JoinColumn(name = "groupId")])
        @Column(name = "schema")
        var databases: MutableSet<String> = HashSet()
) : BaseEntity()