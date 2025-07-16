package com.example.gymappia.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gymappia.R

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = "greeting goes here", //todo add the custom greeting thing
            modifier = modifier.padding(bottom = 16.dp)
        )
        Image(
            painter = painterResource(R.drawable.transparent_jim),
            contentDescription = "jim"
        )
        Button(
            onClick = {},
            modifier = modifier,
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                text = stringResource(R.string.ready)
            )
        }

    }
}