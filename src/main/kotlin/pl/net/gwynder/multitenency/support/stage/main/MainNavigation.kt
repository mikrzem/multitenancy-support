package pl.net.gwynder.multitenency.support.stage.main

import pl.net.gwynder.multitenency.support.configuration.entities.DatabaseServerConfiguration

interface MainNavigation {

    fun showAllConfigurations()

    fun showNewConfiguration()

    fun showEditConfiguration(configuration: DatabaseServerConfiguration)

}