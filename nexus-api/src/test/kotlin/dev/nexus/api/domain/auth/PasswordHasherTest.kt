package dev.nexus.api.domain.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Pure unit test — no fake, no Docker. PasswordHasher has zero external
 * dependencies (bcrypt runs in-process), so it's the simplest thing to unit-test.
 */
class PasswordHasherTest {

    @Test
    fun `verify accepts the correct password`() {
        val hash = PasswordHasher.hash("hunter2pass")
        assertTrue(PasswordHasher.verify("hunter2pass", hash))
    }

    @Test
    fun `verify rejects a wrong password`() {
        val hash = PasswordHasher.hash("hunter2pass")
        assertFalse(PasswordHasher.verify("wrongpass", hash))
    }

    @Test
    fun `hashing is salted - same input gives different hashes`() {
        assertNotEquals(PasswordHasher.hash("samePassword"), PasswordHasher.hash("samePassword"))
    }

    @Test
    fun `hash is a 60-char bcrypt string`() {
        val hash = PasswordHasher.hash("anything")
        assertEquals(60, hash.length)
        assertTrue(hash.startsWith("\$2"), "bcrypt hashes start with \$2")
    }
}
