package com.example.organizationservice.events.source

import com.example.organizationservice.events.model.OrganizationChangeModel
import com.example.organizationservice.utils.ActionEnum
import com.example.organizationservice.utils.UserContext
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SimpleSource(
    private val streamBridge: StreamBridge,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Async
    fun publishOrganizationChange(action: ActionEnum, organizationId: String) {
        logger.info("Sending Kafka Message $action for Organization Id : $organizationId")

        val organization = OrganizationChangeModel(
            OrganizationChangeModel::class.java.typeName,
            action.toString(),
            organizationId,
            UserContext.getCorrelationId(),
        )

        streamBridge.send("output-out-0", MessageBuilder.withPayload(organization).build())
    }
}
