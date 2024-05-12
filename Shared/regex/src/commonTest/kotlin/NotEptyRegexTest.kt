import shared.regex.NotEmptyPattern
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NotEmptyRegexTest {
    @Test
    fun `not empty success`() {
        val clues = listOf(
            "asdnl",
            "1",
        )
        val regex = NotEmptyPattern.toRegex()
        val matches = clues.fold(true) { acc, clue ->
            acc && clue.matches(regex)
        }
        
        assertTrue(matches)
    }

    @Test
    fun `not empty error`() {
        val clues = listOf(
            "",
        )
        val regex = NotEmptyPattern.toRegex()
        val matches = clues.fold(false) { acc, clue ->
            acc || clue.matches(regex)
        }
        
        assertFalse(matches)
    }
}