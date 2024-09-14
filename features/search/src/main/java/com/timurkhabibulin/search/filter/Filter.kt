package com.timurkhabibulin.search.filter

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.search.R

@Composable
internal fun Filter(
    modifier: Modifier = Modifier,
    filterHandler: FilterHandler,
    onCancelClick: () -> Unit,
    onApplyClick: (FilterUIState) -> Unit
) {
    val uiState by filterHandler.uiState.collectAsState()

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = stringResource(R.string.filters), style = MaterialTheme.typography.titleLarge)

        OrientationFilter(
            currentOrientation = uiState.orientation,
            onChangeOrientation = filterHandler::updateOrientation
        )
        ColorFilter(
            currentColor = uiState.color,
            onChangeColor = filterHandler::updateColor
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                30.dp, Alignment.End
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.clickable { onCancelClick() },
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.titleSmall
            )

            Button(
                modifier = Modifier
                    .width(214.dp)
                    .height(44.dp),
                onClick = { onApplyClick(uiState) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = stringResource(R.string.apply),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun OrientationFilter(
    currentOrientation: Orientation?,
    onChangeOrientation: (Orientation?) -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.orientation),
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = currentOrientation == null,
                onClick = { onChangeOrientation(null) },
                label = {
                    Text(
                        text = stringResource(R.string.none),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                shape = RoundedCornerShape(50.dp)
            )

            Orientation.values().forEach {
                FilterChip(
                    selected = currentOrientation == it,
                    onClick = { onChangeOrientation(it) },
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

@Composable
fun ColorFilter(
    currentColor: Color?,
    onChangeColor: (Color?) -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(id = R.string.color),
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = currentColor == null,
                onClick = { onChangeColor(null) },
                label = {
                    Text(
                        text = stringResource(R.string.none),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                shape = RoundedCornerShape(50.dp)
            )

            Color.values().forEach {
                FilterChip(
                    selected = currentColor == it,
                    onClick = { onChangeColor(it) },
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
            text = stringResource(id = R.string.filters),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
