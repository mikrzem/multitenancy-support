package pl.net.gwynder.multitenency.support.stage.main

import javafx.scene.Scene
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.stage.OpenMainStageEvent
import pl.net.gwynder.multitenency.support.utils.FxmlBuilder

@Component
class MainStageListener(
        private val fxml: FxmlBuilder,
        @Value("classpath:fxml/main.fxml")
        private val mainUiResource: Resource
) : ApplicationListener<OpenMainStageEvent> {

    override fun onApplicationEvent(event: OpenMainStageEvent) {
        val stage = event.stage
        val scene = fxml.scene(mainUiResource)
        stage.scene = scene
        stage.show()
    }

}