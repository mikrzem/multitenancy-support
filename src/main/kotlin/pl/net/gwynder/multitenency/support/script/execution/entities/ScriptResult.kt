package pl.net.gwynder.multitenency.support.script.execution.entities

import pl.net.gwynder.multitenency.support.utils.database.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ScriptResult(
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "runId", nullable = false)
        var run: ScriptRun = ScriptRun(),
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "targetId", nullable = false)
        var target: ScriptTargetDatabase = ScriptTargetDatabase(),
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "queryId", nullable = false)
        var query: ScriptQuery = ScriptQuery(),
        @Column(nullable = false)
        var startTime: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false)
        var endTime: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = false)
        var success: Boolean = false,
        @Column(nullable = false)
        var affectedRows: Int = 0
) : BaseEntity()