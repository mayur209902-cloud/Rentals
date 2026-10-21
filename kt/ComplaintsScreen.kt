package com.example.rentals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ComplaintsScreen(
    modifier: Modifier = Modifier,
    onNewComplaintClick: () -> Unit = {},
    onMyComplaintsClick: () -> Unit = {},
    onPendingClick: () -> Unit = {},
    onResolvedClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Text(
                text = "Complaints",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 18.dp),
            )

            ComplaintsContainer(
                modifier = Modifier.fillMaxWidth(),
                onNewComplaintClick = onNewComplaintClick,
                onMyComplaintsClick = onMyComplaintsClick,
                onPendingClick = onPendingClick,
                onResolvedClick = onResolvedClick,
            )
        }
    }
}

@Composable
fun ComplaintsContainer(
    modifier: Modifier = Modifier,
    onNewComplaintClick: () -> Unit = {},
    onMyComplaintsClick: () -> Unit = {},
    onPendingClick: () -> Unit = {},
    onResolvedClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141416))
            .padding(15.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ComplaintButton(
                    text = "New Complaint",
                    modifier = Modifier.weight(1f),
                    onClick = onNewComplaintClick,
                )
                ComplaintButton(
                    text = "My Complaints",
                    modifier = Modifier.weight(1f),
                    onClick = onMyComplaintsClick,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ComplaintButton(
                    text = "Pending",
                    modifier = Modifier.weight(1f),
                    onClick = onPendingClick,
                )
                ComplaintButton(
                    text = "Resolved",
                    modifier = Modifier.weight(1f),
                    onClick = onResolvedClick,
                )
            }
        }
    }
}

@Composable
fun ComplaintButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E22))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ComplaintsScreenPreview() {
    ComplaintsScreen()
}
