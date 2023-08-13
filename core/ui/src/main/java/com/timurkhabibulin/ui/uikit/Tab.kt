package com.timurkhabibulin.ui.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

@Composable
fun TabIndicator(tabPosition: TabPosition) {
    Box(
        modifier = Modifier
            .tabIndicatorOffset(tabPosition)
            .padding(horizontal = 5.dp)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(100)
            )
            .zIndex(-1f),
    )
}

@Composable
fun Tab(
    text: String,
    selected: Boolean,
    onClick: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    Tab(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
                RoundedCornerShape(100)
            ),
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        selected = selected,
        onClick = {
            scope.launch {
                onClick()
            }
        }
    )
}