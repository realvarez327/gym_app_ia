package com.example.gymappia.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.content.res.Resources
import androidx.compose.ui.graphics.drawscope.DrawScope


class ProgressGraphic {
    @Composable
    fun DrawProgressGraphic(
        goalColors: List<Color>,
        modifier: Modifier = Modifier,

    ) {
        var degreeAmt = 0f
        Canvas(modifier = modifier.size(240.dp)) {
            goalColors.forEach { color ->
                //DrawSector(color = color, progressAmount = 2.0, rotationAmount = degreeAmt, canvasSize = 15f)
                degreeAmt += 45f
                drawSector(
                    drawScope =this,
                    progressAmount = 3.0,
                    rotationAmount = degreeAmt,
                    color = color
                )

            }
        }


    }



    fun drawSector(
        color: Color,
        progressAmount: Double,
        rotationAmount: Float = 0f,
        drawScope: DrawScope
    ) {

        with(drawScope){
            rotate(degrees = rotationAmount) {
                translate(top = size.height / 2) {
                    val path = Path()
                    path.moveTo(0.5F * size.width, y = 0f)
                    path.lineTo(0.35F * size.width, 0.15F * size.height)
                    path.lineTo(0.5F * size.width, 0.5F * size.height)
                    path.lineTo(0.65F * size.width, 0.15F * size.height)
                    drawPath(path = path, color = color, alpha = 0.5f)

                }
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DrawPreview() {
//        Canvas(modifier = Modifier.size(300.dp)) {
//            drawSector(
//                drawScope = this,
//                color = Color.Red,
//                progressAmount = 3.0,
//                rotationAmount = 0f,
//            )
//        }

        DrawProgressGraphic(
            goalColors = listOf(
                Color.Red,
                Color.Magenta,
                Color.Blue,
                Color.Cyan,
                Color.Yellow,
                Color.Green,
                Color.DarkGray,
                Color.Black
            )
        )

    }

}