package pl.net.gwynder.multitenency.support.utils

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.util.Callback
import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class FxmlBuilder(
        private val context: ApplicationContext
) {

    private fun loader(resource: Resource, controllerFactory: Callback<Class<*>, Any>?): FXMLLoader {
        val loader = FXMLLoader(resource.url)
        loader.controllerFactory = controllerFactory
                ?: Callback<Class<*>, Any> { controller -> context.getBean(controller) }
        return loader
    }

    fun component(resource: Resource, controllerFactory: Callback<Class<*>, Any>? = null): Parent {
        return loader(resource, controllerFactory).load()
    }

    fun componentWithController(resource: Resource, controller: Any): Parent {
        return component(resource, Callback { controller })
    }

    fun scene(resource: Resource): Scene {
        return Scene(
                component(resource)
        )
    }

}