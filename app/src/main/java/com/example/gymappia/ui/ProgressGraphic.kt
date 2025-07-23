package com.example.gymappia.ui

import android.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class ProgressGraphic {
    @Composable
    fun DrawProgressGraphic(
        goalColors: List<Color>,
        modifier: Modifier = Modifier
    ) {

        Canvas(modifier = modifier.size(300.dp)) {


        }
    }

    @Composable
    fun DrawSector(
        color: Color,
        progressAmount: Double,
        modifier: Modifier = Modifier,
        rotationAmount: Float = 0f
    ) {
        //first draw triangle
        //then work up to kite shape as per specs
        // then draw only outline
        // then draw outline and fill
        //then finally draw outline and fill up to progressAmount
        Canvas(modifier = modifier.size(300.dp)) {
            val path = Path()
            path.moveTo(size.width / 4f, size.height / 20f)
            path.lineTo(size.width / 4f + 100f, size.height / 20f + 100f)
            path.lineTo(size.width / 4f, size.height * 0.45f)
            path.lineTo(size.width / 4f - 100f, size.height / 20f + 100f)

//            translate(left = -100f, top = 400f) {
            rotate(degrees = 90f - rotationAmount) {

                translate(top = 400f, left = 200f) {
                    drawPath(path = path, color = color, alpha = 0.5f)
                }

            }
//            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DrawSectorPreview() {
        DrawSector(color = Color.Red, progressAmount = 2.0, rotationAmount = 0f)
        DrawSector(color = Color.Blue, progressAmount = 2.0, rotationAmount = 45f)
        DrawSector(color = Color.Cyan, progressAmount = 2.0, rotationAmount = 90f)
        DrawSector(color = Color.Green, progressAmount = 2.0, rotationAmount = 135f)
        DrawSector(color = Color.Magenta, progressAmount = 2.0, rotationAmount = 180f)
        DrawSector(color = Color.Black, progressAmount = 2.0, rotationAmount = 225f)
        DrawSector(color = Color.DarkGray, progressAmount = 2.0, rotationAmount = 270f)
        DrawSector(color = Color.Yellow, progressAmount = 2.0, rotationAmount = 315f)

    }

}