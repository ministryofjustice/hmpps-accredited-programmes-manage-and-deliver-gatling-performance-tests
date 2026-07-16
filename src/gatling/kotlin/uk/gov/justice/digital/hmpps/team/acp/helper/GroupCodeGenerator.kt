package uk.gov.justice.digital.hmpps.team.acp.helper

import java.util.concurrent.atomic.AtomicLong

object GroupCodeGenerator {

    private val counter = AtomicLong(0)

    fun next(): String {
        return "perf-test-group-code-${counter.incrementAndGet()}"
    }
}