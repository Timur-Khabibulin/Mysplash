package com.timurkhabibulin.ui.uikit

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.timurkhabibulin.domain.entities.Topic
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class TopicCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val topic = Topic.Default

    @Test
    fun topicCardTest() {
        var clickedTopic: Topic? = null

        composeTestRule.setContent {
            TopicCard(topic = topic, onTopicClick = {
                clickedTopic = it
            })
        }

        composeTestRule.apply {
            onNodeWithContentDescription("topicImage").assertIsDisplayed()
            onNodeWithContentDescription("arrow").assertIsDisplayed()
            onNodeWithText(topic.title).assertIsDisplayed()
            onNodeWithTag("topicPreview")
                .assertHasClickAction()
                .performClick()
        }

        Assert.assertEquals(topic, clickedTopic)
    }

}