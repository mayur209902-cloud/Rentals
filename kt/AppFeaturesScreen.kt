package com.example.rentals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentals.R

data class AppFeatureItem(
    val iconResId: Int,
    val title: String,
    val subtitle: String,
)

@Composable
fun AppFeaturesScreen(
    modifier: Modifier = Modifier,
) {
    val features = listOf(
        AppFeatureItem(
            iconResId = R.drawable.ic_home_feat,
            title = stringResource(id = R.string.feature_home_dashboard_title),
            subtitle = stringResource(id = R.string.feature_home_dashboard_subtitle),
        ),
        AppFeatureItem(
            iconResId = R.drawable.ic_document_feat,
            title = stringResource(id = R.string.feature_rental_details_title),
            subtitle = stringResource(id = R.string.feature_rental_details_subtitle),
        ),
        AppFeatureItem(
            iconResId = R.drawable.ic_wallet_feat,
            title = stringResource(id = R.string.feature_payments_history_title),
            subtitle = stringResource(id = R.string.feature_payments_history_subtitle),
        ),
        AppFeatureItem(
            iconResId = R.drawable.ic_calendar_feat,
            title = stringResource(id = R.string.feature_calendar_reminders_title),
            subtitle = stringResource(id = R.string.feature_calendar_reminders_subtitle),
        ),
        AppFeatureItem(
            iconResId = R.drawable.ic_profile_feat,
            title = stringResource(id = R.string.feature_profile_settings_title),
            subtitle = stringResource(id = R.string.feature_profile_settings_subtitle),
        ),
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.features_header),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(features) { (iconResId, title, subtitle) ->
                    FeatureCard(
                        iconResId = iconResId,
                        title = title,
                        subtitle = subtitle,
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureCard(
    iconResId: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Color(0xFFAAAAAA),
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AppFeaturesScreenPreview() {
    AppFeaturesScreen()
}
