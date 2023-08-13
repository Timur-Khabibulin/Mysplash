package com.timurkhabibulin.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timurkhabibulin.ui.theme.MysplashTheme

@Preview
@Composable
internal fun FilterPreview() {
    MysplashTheme {
        Filter(Modifier, {}, {})
    }
}

@Composable
internal fun Filter(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Filters", style = MaterialTheme.typography.titleLarge)

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
                onClick = { onApplyClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "Apply", style = MaterialTheme.typography.titleSmall)
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