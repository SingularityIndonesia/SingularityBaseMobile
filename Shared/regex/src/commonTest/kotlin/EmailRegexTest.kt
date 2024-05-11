import com.singularity.regex.EmailPattern
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmailRegexTest {
    @Test
    fun `test email success`() {
        val clues = listOf(
            "some.email@google.com",
        )
        val regex = EmailPattern.toRegex()
        val matches = clues.fold(true) { acc, clue ->
            acc && clue.matches(regex)
        }
        
        assertTrue(matches)
    }

    @Test
    fun `test email error`() {
        val clues = listOf(
            "some.email@domain.",
            "askdnal",
            "121232@skkss.sk_",
            "121/232@domain.com"
        )
        val regex = EmailPattern.toRegex()
        val matches = clues.fold(false) { acc, clue ->
            acc || clue.matches(regex)
        }
        
        assertFalse(matches)
    }
}