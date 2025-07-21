package com.example.gymappia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import com.example.gymappia.R
import com.example.gymappia.ui.theme.GymAppIATheme

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    onNextButtonClicked:()->Unit
){
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(R.string.first_time_welcome),
            textAlign = TextAlign.Center,
            modifier = modifier,
            style = MaterialTheme.typography.headlineMedium
        )
        Image(
            painterResource(R.drawable.transparent_jim),
            contentDescription = "jim",
            modifier = modifier

                .width(120.dp),
            contentScale = ContentScale.FillWidth
        )

        Button(
            onClick = { onNextButtonClicked() },
            modifier = modifier.padding(8.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                text = stringResource(R.string.ready),
                style = MaterialTheme.typography.labelMedium
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LandingScreenPreview(){
    GymAppIATheme {
        LandingScreen(modifier = Modifier, onNextButtonClicked = {})
    }
}