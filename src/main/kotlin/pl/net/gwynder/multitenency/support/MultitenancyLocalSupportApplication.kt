package pl.net.gwynder.multitenency.support

import org.springframework.boot.autoconfigure.SpringBootApplication
import javafx.application.Application
import javafx.application.HostServices
import javafx.stage.Stage
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.GenericApplicationContext
import pl.net.gwynder.multitenency.support.stage.OpenMainStageEvent
import java.util.function.Supplier

@SpringBootApplication
class MultitenancyLocalSupportApplication : Application() {

    private var context: ConfigurableApplicationContext? = null

    override fun init() {
        super.init()

        context = SpringApplicationBuilder()
                .sources(MultitenancyLocalSupportApplication::class.java)
                .initializers(ApplicationContextInitializer<GenericApplicationContext> { applicationContext ->
                    applicationContext.registerBean(Application::class.java, Supplier<Application> { this })
                    applicationContext.registerBean(Parameters::class.java, Supplier<Parameters> { this.parameters })
                    applicationContext.registerBean(HostServices::class.java, Supplier<HostServices> { this.hostServices })
                })
                .run(*parameters.raw.toTypedArray())
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage != null) {
            primaryStage.isMaximized = true
            primaryStage.title = "Multitenancy support"
            context?.publishEvent(OpenMainStageEvent(primaryStage))
        }
    }

    override fun stop() {
        context?.stop()
        super.stop()
    }

}

fun main(args: Array<String>) {
    Application.launch(MultitenancyLocalSupportApplication::class.java)
}
