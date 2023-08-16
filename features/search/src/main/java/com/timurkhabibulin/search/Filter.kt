package com.timurkhabibulin.search

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.ui.theme.MysplashTheme

@Preview
@Composable
internal fun FilterPreview() {
    MysplashTheme {

        Filter(
            Modifier,
            Color.BLACK,
            null,
            {},
            { _, _ -> })
    }
}

//TODO(): кнопка сброса
@Composable
internal fun Filter(
    modifier: Modifier = Modifier,
    defaultColor: Color?,
    defaultOrientation: Orientation?,
    onCancelClick: () -> Unit,
    onApplyClick: (Color?, Orientation?) -> Unit
) {

    val color: MutableState<Color?> = remember {
        mutableStateOf(defaultColor)
    }
    val orientation: MutableState<Orientation?> = remember {
        mutableStateOf(defaultOrientation)
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Filters", style = MaterialTheme.typography.titleLarge)

        OrientationFilter(currentOrientation = orientation)
        ColorFilter(currentColor = color)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                30.dp, Alignment.End
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.clickable { onCancelClick() },
                text = "Cancel",
                style = MaterialTheme.typography.titleSmall
            )

            Button(
                modifier = Modifier
                    .width(214.dp)
                    .height(44.dp),
                onClick = { onApplyClick(color.value, orientation.value) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "Apply", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrientationFilter(
    currentOrientation: MutableState<Orientation?>
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Color", style = MaterialTheme.typography.titleSmall)

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = currentOrientation.value == null,
                onClick = { currentOrientation.value = null },
                label = {
                    Text(text = "None", style = MaterialTheme.typography.bodyMedium)
                },
                shape = RoundedCornerShape(50.dp)
            )

            Orientation.values().forEach {
                FilterChip(
                    selected = currentOrientation.value == it,
                    onClick = { currentOrientation.value = it },
                    label = {
                        Text(
                            text = it.orientationName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = RoundedCornerShape(50.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorFilter(
    currentColor: MutableState<Color?>
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Color", style = MaterialTheme.typography.titleSmall)

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = currentColor.value == null,
                onClick = { currentColor.value = null },
                label = {
                    Text(text = "None", style = MaterialTheme.typography.bodyMedium)
                },
                shape = RoundedCornerShape(50.dp)
            )

            Color.values().forEach {
                FilterChip(
                    selected = currentColor.value == it,
                    onClick = { currentColor.value = it },
                    label = {
                        Text(
                            text = it.colorName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = RoundedCornerShape(50.dp),
                    leadingIcon = {
                        Canvas(modifier = Modifier.padding(horizontal = 10.dp), onDraw = {
                            drawCircle(
                                androidx.compose.ui.graphics.Color(it.colorValue),
                                7.dp.toPx()
                            )
                        })
                    }
                )
            }
        }
    }
}

@Composable
internal fun FilterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(size = 50.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(size = 50.dp)
            )
            .padding(horizontal = 15.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.settings_04),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}