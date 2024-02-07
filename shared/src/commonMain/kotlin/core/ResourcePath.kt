package core

import core.ResourcePath.Drawable
import core.ResourcePath.Drawable.BookmarkIcon
import core.ResourcePath.Drawable.HomeIcon
import core.ResourcePath.Drawable.Person
import core.ResourcePath.Drawable.SearchIcon
import core.ResourcePath.Font
import core.ResourcePath.Lottie
import core.ResourcePath.String


/**
 * A utility class that holds constants for resource paths including drawables, lotties, fonts, and strings.
 * This class also provides methods to generate content descriptions for drawable resources.
 *
 * @property Drawable Object holding drawable resource paths.
 * @property Lottie Object holding lottie animation resource paths.
 * @property Font Object holding font resource paths.
 * @property [ResourcePath.String] Object holding string resource values.
 *
 * @see Drawable
 * @see Lottie
 * @see Font
 * @see String
 *
 * @author Prashant Singh
 */
class ResourcePath {

    /**
     * Object holding drawable resource paths.
     */
    object Drawable {
        const val composeMultiPlatform = "drawable/compose_multiplatform.xml"
        const val splashPattern = "drawable/splash_pattern.xml"
        const val logo = "drawable/main_logo.png"
        const val welcomeFirst = "drawable/welcome_first.xml"
        const val welcomeSecond = "drawable/welcome_second.xml"
        const val iconBack = "drawable/icon_back.xml"
        const val iconGoogle = "drawable/icon_google.xml"
        const val iconFacebook = "drawable/icon_facebook.xml"
        const val iconLock = "drawable/icon_lock.xml"
        const val iconVisibility = "drawable/icon_visibility.xml"
        const val iconVisibilityOff = "drawable/icon_visibility_off.xml"
        const val iconProfile = "drawable/icon_profile.xml"
        const val iconMail = "drawable/icon_mail.xml"
        const val loginLogo = "drawable/login_logo.png"
        const val iconMessage = "drawable/icon_message.xml"
        const val iconResetOptionMail = "drawable/icon_reset_option_email.xml"
        const val iconSuccess = "drawable/icon_success.xml"
        const val iconBgSecond = "drawable/icon_otp_bg.xml"
        const val iconPaypal = "drawable/icon_paypal.xml"
        const val iconPayoneer = "drawable/icon_payoneer.xml"
        const val iconVisa = "drawable/icon_visa.xml"
        const val iconCamera = "drawable/icon_camera.xml"
        const val iconGallery = "drawable/icon_gallery.xml"
        const val iconClose = "drawable/icon_close.xml"
        const val iconPin = "drawable/icon_pin.xml"
        const val HomeIcon = "drawable/home.xml"
        const val Person = "drawable/person.xml"
        const val SearchIcon = "drawable/search.xml"
        const val BookmarkIcon = "drawable/bookmark.xml"
        const val BookmarkBorderIcon = "drawable/bookmark_border.xml"
        const val Close = "drawable/close.xml"
        const val LIKED = "drawable/favorite_like.xml"
        const val LIKED_BORDER = "drawable/favorite_border.xml"
        const val TOTAL_VIEWED = "drawable/icon_eye.xml"
        const val LINKEDIN = "drawable/icon_linkedin.xml"
        const val YOUTUBE = "drawable/icon_youtube.xml"
        const val INSTAGRAM = "drawable/icon_instagram.xml"
        const val GITHUB = "drawable/icon_github.xml"


        /**
         * Generates a content description from the drawable resource name.
         */
        val kotlin.String.contentDescription: kotlin.String
            get() = convertStringToTitleCase(this)

        private fun convertStringToTitleCase(input: kotlin.String): kotlin.String {
            val words =
                input.split("_", ".").map {
                    it.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase() else char.toString()
                    }
                }
            return words.joinToString(" ")
        }
    }

    /**
     * Object holding lottie animation resource paths.
     */
    object Lottie {
        // https://www.debugdesk.in/lotties/loading.json
        const val LOADING = "lottie/loading.json"
        const val NO_DATA = "lottie/no_data.json"
        const val NOT_FOUND = "lottie/not_found.json"
        const val WENT_WRONG = "lottie/went_wrong.json"
        const val SEARCH = "lottie/search.json"
    }


    /**
     * Object holding font resource paths.
     */
    object Font {
        const val VigaRegularName = "VigaRegular"
        const val VigaRegularFont = "font/viga_regular.ttf"
    }

    /**
     * Object holding string resource values.
     */
    object String {
        const val forgetYourPassword = "Forgot Your Password?"
        const val facebook = "Facebook"
        const val google = "Google"
        const val continueWith = "Or continue with"
        const val password = "Password"
        const val confirmPassword = "Confirm Password"
        const val appName = "Debug Desk"
        const val appSlogan = "Deliver Favorite Food"
        const val welcomeHeadingFirst = "Find your Comfort Food here"
        const val welcomeLabelFirst =
            "Here You Can find a chef or dish for every taste and color. Enjoy!"
        const val welcomeHeadingSecond = "Food Ninja is Where Your Comfort Food Lives"
        const val welcomeLabelSecond = "Enjoy a fast and smooth food delivery at\nyour doorstep"
        const val next = "Next"
        const val login = "Login"
        const val createAccount = "Create an Account"
        const val createAccountButton = "Create an account!"
        const val alreadyHaveAnAccount = "Already have an account!"
        const val loginToAccount = "Login To Your Account"
        const val signUpFree = "Sign Up For Free"
        const val name = "Name"
        const val firstName = "First Name"
        const val lastname = "LastName"
        const val number = "Number"
        const val emailSpecialPricing = "Email Me About Special Pricing"
        const val email = "Email"
        const val keepMeSignIn = "Email"

        // Forgot password Screen
        const val forgotPassword = "Forgot Password?"
        const val passwordResetOption =
            "Select which contact details should we\nuse to reset your password"

        // OTP verification
        const val enterFourCode = "Enter 4-digit\nVerification code"
        const val resendCodeIn = "Code send to +919282045**** . This code will\nexpired in 01:30"
        const val otp = "0000"

        // ResetPassword Screen
        const val resetPassword = "Reset your password\nhere"
        const val resetPasswordInstruction =
            "Select which contact details should we\nuse to reset your password"

        // Congrats Screen
        const val congrats = "Congrats!"
        const val resetSuccessful = "Password reset successfully"

        // SignUpProcess

        const val bioHeading = "Fill in your bio to get\nstarted"
        const val bioSubTitle = "This data will be displayed in your account\nprofile for security"

        const val paymentHeading = "Payment Method"
        const val paymentSubTitle =
            "This data will be displayed in your account\nprofile for security"

        const val uploadPhotoHeading = "Upload Your Photo\nProfile"
        const val uploadPhotoSubTitle =
            "This data will be displayed in your account\nprofile for security"

        const val previewPhotoHeading = "Preview Your Photo\nProfile"
        const val previewPhotoSubTitle =
            "This data will be displayed in your account\nprofile for security"

        const val locationHeading = "Set Your Location "
        const val locationSubTitle =
            "This data will be displayed in your account\nprofile for security"

        const val takePhoto = "Take Photo"
        const val fromGallery = "From Gallery"

        const val yourLocation = "Your Location"
        const val setLocation = "Set Location"
    }
}


/**
 * Enum class representing the bottom navigation tabs with associated drawable resource paths.
 *
 * @property text Text associated with the bottom navigation tab.
 *
 */
enum class BottomBarTab(val text: kotlin.String) {
    Home(HomeIcon),
    Search(SearchIcon),
    BookMark(BookmarkIcon),
    Account(Person),
}
