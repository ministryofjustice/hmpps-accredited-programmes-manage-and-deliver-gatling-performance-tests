package uk.gov.justice.digital.hmpps.team.acp.helper

import java.util.concurrent.atomic.AtomicLong

object GroupCodeGenerator {
    private val runId = System.currentTimeMillis().toString(36)
    private val counter = AtomicLong(0)

    fun next(): String = "perf-test-$runId-${counter.incrementAndGet()}"
}
