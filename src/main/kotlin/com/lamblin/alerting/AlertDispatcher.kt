package com.lamblin.alerting


/**
 * Defines an alert dispatching mechanism.
 */
interface AlertDispatcher {

    /**
     * Dispatches an alert.
     *
     * @param alert the alert to dispatch.
     */
    fun dispatchAlert(alert: LambdaAlert)

    companion object {

        /** Creates a [SlackAlertDispatcher] instance. */
        fun slackAlertDispatcher(slackDispatchingLambda: String): AlertDispatcher {
            return SlackAlertDispatcher(slackDispatchingLambda)
        }
    }

}
