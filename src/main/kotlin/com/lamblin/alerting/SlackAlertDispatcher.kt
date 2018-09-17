package com.lamblin.alerting

import com.amazonaws.services.lambda.AWSLambdaClientBuilder
import com.amazonaws.services.lambda.model.InvocationType
import com.amazonaws.services.lambda.model.InvokeRequest
import com.lamblin.core.OBJECT_MAPPER
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(SlackAlertDispatcher::class.java)

class SlackAlertDispatcher(private val slackDispatchingLambda: String) : AlertDispatcher {

    override fun dispatchAlert(alert: LambdaAlert) {
        LOGGER.info("Sending lambda alert")

        val request = InvokeRequest().apply {
            withFunctionName(slackDispatchingLambda)
            withPayload(OBJECT_MAPPER.writeValueAsString(alert))
            withInvocationType(InvocationType.Event)
        }

        val client = AWSLambdaClientBuilder.defaultClient()
        client.invoke(request)
    }
}
