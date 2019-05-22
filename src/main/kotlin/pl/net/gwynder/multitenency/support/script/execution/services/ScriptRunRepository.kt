package pl.net.gwynder.multitenency.support.script.execution.services

import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptRun
import pl.net.gwynder.multitenency.support.utils.database.BaseEntityRepository

interface ScriptRunRepository : BaseEntityRepository<ScriptRun> {
}