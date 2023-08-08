package com.timurkhabibulin.ui.uikit

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun CollapsibleLayout(
    modifier: Modifier = Modifier,
    collapsingHeader: @Composable (Modifier) -> Unit,
    canCollapse: State<Boolean> = mutableStateOf(true),
    content: @Composable (Modifier) -> Unit,
    collapsingHeaderDefaultHeight: Dp? = null
) {
    val density = LocalDensity.current

    var isHeaderSizeMeasured by rememberSaveable { mutableStateOf(collapsingHeaderDefaultHeight != null) }

    val collapsing by rememberSaveable {
        canCollapse
    }

    var headerDefaultHeight by rememberSaveable {
        mutableFloatStateOf(
            with(density) {
                collapsingHeaderDefaultHeight?.toPx()
            } ?: 0f)
    }

    var headerHeight by rememberSaveable { mutableFloatStateOf(headerDefaultHeight) }

    val onOffsetChange: (Float) -> Float = { delta ->
        if (collapsing) {
            val oldState = headerHeight
            val newState = (headerHeight + delta).coerceIn(0f, headerDefaultHeight)
            headerHeight = newState
            newState - oldState
        } else 0f
    }

    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val vertical = available.y
                val consumed = onOffsetChange(vertical)
                return Offset(x = 0f, y = consumed)
            }
        }
    }

    val headerModifier = if (collapsingHeaderDefaultHeight == null) {
        Modifier.onGloballyPositioned {
            if (!isHeaderSizeMeasured) {
                headerDefaultHeight = it.size.height.toFloat()
                headerHeight = headerDefaultHeight
                isHeaderSizeMeasured = true
            }
        }
    } else Modifier

    Column(
        modifier
            .fillMaxSize()
            .nestedScroll(connection = nestedScrollConnection, dispatcher = nestedScrollDispatcher)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    // here's regular drag. Let's be good citizens and ask parents first if they
                    // want to pre consume (it's a nested scroll contract)
                    val parentsConsumed = nestedScrollDispatcher.dispatchPreScroll(
                        available = Offset(x = 0f, y = delta),
                        source = NestedScrollSource.Drag
                    )
                    // adjust what's available to us since might have consumed smth
                    val adjustedAvailable = delta - parentsConsumed.y
                    // we consume
                    val consumed = onOffsetChange(adjustedAvailable)
                    // dispatch as a post scroll what's left after pre-scroll and our consumption
                    val totalConsumed = Offset(x = 0f, y = consumed) + parentsConsumed
                    val left = adjustedAvailable - consumed
                    nestedScrollDispatcher.dispatchPostScroll(
                        consumed = totalConsumed,
                        available = Offset(x = 0f, y = left),
                        source = NestedScrollSource.Drag
                    )
                }
            )
    ) {
        val headerHeightDp = with(density) { headerHeight.toDp() }

        collapsingHeader(
            headerModifier
                .then(
                    if (isHeaderSizeMeasured) Modifier.height(headerHeightDp)
                    else Modifier
                )
        )
        content(Modifier)
    }
}