package com.lamblin.core.model

import java.io.Serializable
import java.util.Comparator

/**
 * Defines the main HandlerMethod comparator, used when controller paths are sorted on request handling
 * before the right handler method is picked.
 */
class HandlerMethodComparator : Comparator<HandlerMethod>, Serializable {

    /**
     * Compares two handler methods following the rules
     * - shorter paths take precedence
     *  If method paths have the same length
     *  -the method with i.e less path params takes precedence
     * - the method with more mandatory query param takes precedence
     */
    override fun compare(o1: HandlerMethod, o2: HandlerMethod): Int {
        val o1PathSections = o1.path.split("/")
        val o2PathSections = o2.path.split("/")

        return when {
            o1PathSections.size != o2PathSections.size -> o1PathSections.size - o2PathSections.size
            comparePathParams(o1PathSections, o2PathSections) != 0 -> comparePathParams(o1PathSections,
                                                                                        o2PathSections)
            else -> compareMandatoryQueryParams(o1, o2)
        }
    }

    // The method with less path params will take precedence
    private fun comparePathParams(pathEntries: List<String>, comparedPathEntries: List<String>): Int {
        val pathContainsPathParams = pathEntries.any {
            HandlerMethod.isPathParamSection(it)
        }
        val comparedPathContainsPathParams = comparedPathEntries.any {
            HandlerMethod.isPathParamSection(it)
        }

        return when {
            pathContainsPathParams && !comparedPathContainsPathParams -> 1
            !pathContainsPathParams && comparedPathContainsPathParams -> -1
            else -> 0
        }
    }

    // The method with more mandatory query params will take precedence
    private fun compareMandatoryQueryParams(o1: HandlerMethod, o2: HandlerMethod): Int {
        return (o2.paramNameToParam.values.stream()
                .filter { it.required }
                .count()
                - o1.paramNameToParam.values
                .filter { it.required }
                .count())
                .toInt()
    }
}
