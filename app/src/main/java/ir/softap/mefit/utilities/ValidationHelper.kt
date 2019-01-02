package ir.softap.mefit.utilities

import dagger.Reusable
import java.util.regex.Pattern

object ValidationHelper {

    /**
     * Check's email format
     * @param emailAddress - valid example : email@example.com
     * @return - if valid return's true / if invalid return's false
     */
    @Reusable
    @JvmStatic
    fun isValidEmail(emailAddress: String) = Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(emailAddress).matches()

}