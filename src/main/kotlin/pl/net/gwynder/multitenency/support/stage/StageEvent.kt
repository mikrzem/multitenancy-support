package pl.net.gwynder.multitenency.support.stage

import javafx.stage.Stage
import org.springframework.context.ApplicationEvent

abstract class StageEvent(
        val stage: Stage
) : ApplicationEvent(stage)

class OpenMainStageEvent(
        stage: Stage
) : StageEvent(stage)