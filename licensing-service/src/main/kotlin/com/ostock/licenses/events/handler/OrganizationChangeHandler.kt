package com.ostock.licenses.events.handler

import com.ostock.licenses.events.model.OrganizationChangeModel
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class OrganizationChangeHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun inboundOrgChanges(): Consumer<OrganizationChangeModel> = Consumer { organization ->

        logger.debug("Received a message of type ${organization.type}")

        when (organization.action) {
            "GET" -> logger.debug("Received a GET event from the organization service for organization id ${organization.organizationId}")
            "CREATED" -> logger.debug("Received a CREATED event from the organization service for organization id ${organization.organizationId}")
            "UPDATED" -> logger.debug("Received a UPDATED event from the organization service for organization id ${organization.organizationId}")
            "DELETED" -> logger.debug("Received a DELETED event from the organization service for organization id ${organization.organizationId}")
            else -> logger.error("Received a UNKNOWN event from the organization service of type ${organization.type}")
        }
    }
}
