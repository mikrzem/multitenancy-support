package pl.net.gwynder.multitenency.support.script.execution.services

import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptResult
import pl.net.gwynder.multitenency.support.utils.database.BaseEntityRepository

interface ScriptResultRepository : BaseEntityRepository<ScriptResult> {
}