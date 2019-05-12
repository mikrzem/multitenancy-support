package pl.net.gwynder.multitenency.support.configuration.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class ServerConfiguration(
        @Column(nullable = false)
        var name: String = "",
        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var serverType: ServerType = ServerType.MYSQL,
        @Column(nullable = false)
        var serverUsername: String = "",
        @Column(nullable = false)
        var serverPassword: String = "",
        @Column(nullable = false)
        var serverUrl: String = ""
) : BaseEntity()