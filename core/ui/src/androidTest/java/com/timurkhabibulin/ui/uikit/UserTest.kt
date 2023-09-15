package com.timurkhabibulin.ui.uikit

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.theme.MysplashTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class UserTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val photo = Photo.Default

    @Test
    fun userPreviewCardTest() {
        var user: User? = null

        composeTestRule.setContent {
            MysplashTheme {
                UserPreviewCard(
                    user = photo.user,
                    onUserClick = { user = it },
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.shouldHaveUserNameAndBeClickable()
        composeTestRule.onNodeWithText("@${photo.user.username}").assertIsDisplayed()
        composeTestRule.onNodeWithTag("userView").performClick()

        Assert.assertEquals(photo.user, user)
    }

    @Test
    fun userViewHorizontalTest() {
        var user: User? = null

        composeTestRule.setContent {
            MysplashTheme {
                UserViewHorizontal(
                    photo = photo,
                    onUserClick = { user = it }
                )
            }
        }

        composeTestRule.shouldHaveUserNameAndBeClickable()
        composeTestRule.onNodeWithTag("userView").performClick()

        Assert.assertEquals(photo.user, user)
    }

    @Test
    fun userViewVerticalTest() {
        var user: User? = null

        composeTestRule.setContent {
            MysplashTheme {
                UserViewVertical(
                    photo = photo,
                    onUserClick = { user = it }
                )
            }
        }

        composeTestRule.shouldHaveUserNameAndBeClickable()
        composeTestRule.onNodeWithTag("userView").performClick()

        Assert.assertEquals(photo.user, user)
    }

    private fun ComposeContentTestRule.shouldHaveUserNameAndBeClickable() {
        onNodeWithText(photo.user.name).assertIsDisplayed()
        onNodeWithContentDescription("Profile image").assertIsDisplayed()
        onNodeWithTag("userView").assertHasClickAction()
    }
}