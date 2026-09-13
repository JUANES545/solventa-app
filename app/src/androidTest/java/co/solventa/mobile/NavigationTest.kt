package co.solventa.mobile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.solventa.mobile.data.FakeAuthRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun welcomeNavigatesToLogin() {
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.sign_in)).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.email)).assertIsDisplayed()
        composeRule.onNodeWithText(FakeAuthRepository.DEMO_EMAIL).assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_user_login)).assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.demo_credentials)).assertDoesNotExist()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.simulation_notice)).assertDoesNotExist()
    }

    @Test
    fun emptyLoginShowsCorrectiveMessage() {
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.sign_in)).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.sign_in)).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.required_fields_error)).assertIsDisplayed()
    }

    @Test
    fun testUserLoginNavigatesHome() {
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.sign_in)).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_user_login)).performClick()
        val homeGreeting = composeRule.activity.getString(R.string.hello_user)
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(homeGreeting).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(homeGreeting).assertExists().assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.nav_notifications)).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.notifications)).assertIsDisplayed()
    }
}
