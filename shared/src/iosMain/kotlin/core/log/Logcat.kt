package core.log

import platform.Foundation.NSLog

/**
 * Logcat utility class for iOS platform.
 * Provides methods for logging different types of messages.
 */
actual class Logcat {
    /**
     * Companion object containing actual logging functions.
     */
    actual companion object {
        /**
         * Logs an error message with the specified tag and message.
         * @param tag The tag for identifying the source of the log message.
         * @param msg The message to be logged.
         */
        actual fun e(
            tag: String,
            msg: String,
        ) {
            NSLog("     Error           $tag        $msg")
        }

        /**
         * Logs a warning message with the specified tag and message.
         * @param tag The tag for identifying the source of the log message.
         * @param msg The message to be logged.
         */
        actual fun w(
            tag: String,
            msg: String,
        ) {
            NSLog("     Warning         $tag        $msg")
        }

        /**
         * Logs a verbose message with the specified tag and message.
         * @param tag The tag for identifying the source of the log message.
         * @param msg The message to be logged.
         */
        actual fun v(
            tag: String,
            msg: String,
        ) {
            NSLog("     Verbose         $tag        $msg")
        }

        /**
         * Logs an informational message with the specified tag and message.
         * @param tag The tag for identifying the source of the log message.
         * @param msg The message to be logged.
         */
        actual fun i(
            tag: String,
            msg: String,
        ) {
            NSLog("     Info            $tag        $msg")
        }

        /**
         * Logs a debug message with the specified tag and message.
         * @param tag The tag for identifying the source of the log message.
         * @param msg The message to be logged.
         */
        actual fun d(
            tag: String,
            msg: String,
        ) {
            NSLog("     Debug           $tag        $msg")
        }
    }
}
