package pl.net.gwynder.multitenency.support.script.execution.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ScriptRun(
        @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = [CascadeType.ALL])
        @JoinColumn(name = "runId")
        var targets: MutableSet<ScriptTarget> = HashSet(),
        @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = [CascadeType.ALL])
        @JoinColumn(name = "runId")
        var queries: MutableSet<ScriptQuery> = HashSet(),
        @Column(nullable = false)
        var startTime: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false)
        var endTime: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false)
        var finished: Boolean = false,
        @Column(nullable = false)
        var success: Boolean = false
) : BaseEntity()