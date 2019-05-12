package pl.net.gwynder.multitenency.support.stage.main

import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration

interface MainNavigation {

    fun showAllConfigurations()

    fun showNewConfiguration()

    fun showEditConfiguration(configuration: ServerConfiguration)

    fun showGroupEditTree(configuration: ServerConfiguration)

}