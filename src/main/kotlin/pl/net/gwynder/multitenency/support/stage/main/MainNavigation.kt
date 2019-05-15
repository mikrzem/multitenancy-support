package pl.net.gwynder.multitenency.support.stage.main

import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfigurationGroup

interface MainNavigation {

    fun showAllConfigurations()

    fun showNewConfiguration()

    fun showEditConfiguration(configuration: ServerConfiguration)

    fun showGroupEditTree(configuration: ServerConfiguration)

    fun showGroupEdit(configuration: ServerConfiguration, group: ServerConfigurationGroup)

    fun showScriptExecution()

    fun goBack()

}