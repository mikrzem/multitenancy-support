package pl.net.gwynder.multitenency.support.utils.base

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseComponent {

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

}