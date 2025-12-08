package com.example.gymappia.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.gymappia.model.Progress
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.tan


class ProgressGraphic {
    @Composable
    fun DrawProgressGraphic(
        goalColors: List<Color>,
        modifier: Modifier = Modifier,

    ) {
        var degreeAmt = 0f
        Canvas(modifier = modifier.size(480.dp)) {
            val halfTriangleCenterAngle: Float= (((2*PI)/goalColors.size)/2).toFloat()
            var progressAmt = 0.1f
            goalColors.forEach { color ->

                //DrawSector(color = color, progressAmount = 2.0, rotationAmount = degreeAmt, canvasSize = 15f)
                degreeAmt += 45f
                drawSector(
                    drawScope =this,
                    progressAmount = progressAmt,
                    rotationAmount = degreeAmt,
                    color = color,
                    triangleCenterAngleHalf = halfTriangleCenterAngle
                )
                progressAmt*=-1.05f

            }
        }




    }

    @Composable
    fun RealDrawProgressGraphic(
        progresses:List<Progress>,
        modifier: Modifier = Modifier,

        ) {
        var degreeAmt = 0f
        Canvas(modifier = modifier.size(480.dp)) {
            val halfTriangleCenterAngle: Float= (((2*PI)/progresses.size)/2).toFloat()
            var progressAmt = 0.1f
            progresses.forEach { progressGiven ->

                //DrawSector(color = color, progressAmount = 2.0, rotationAmount = degreeAmt, canvasSize = 15f)
                degreeAmt += 45f
                realDrawSector(
                    drawScope =this,
                    progress = progressGiven,
                    rotationAmount = degreeAmt,
                    triangleCenterAngleHalf = halfTriangleCenterAngle
                )
                progressAmt*=-1.05f

            }
        }




    }



    fun drawSector(
        color: Color,
        progressAmount: Float,
        rotationAmount: Float = 0f,
        drawScope: DrawScope,
        triangleCenterAngleHalf: Float
    ) {

        with(drawScope){
           rotate(degrees = rotationAmount){
               val path = Path()
               val size = drawScope.size
               val height =(size.width*0.9F)*progressAmount;
               val distanceFromCenterLine = height* tan(triangleCenterAngleHalf)
               path.moveTo(size.width*0.5f, size.height*0.5f)
               path.lineTo(size.width*0.5f + height, ((size.height*0.5f)-distanceFromCenterLine))
               path.lineTo(size.width*0.5f + height, ((size.height*0.5f)+distanceFromCenterLine))
               path.lineTo(size.width*0.5f, size.height*0.5f)
               path.close()
               drawPath(path = path, color =color, alpha = 0.5f)
           }
        }

    }

    fun realDrawSector(
        progress: Progress,
        rotationAmount: Float = 0f,
        drawScope: DrawScope,
        triangleCenterAngleHalf: Float
    ) {

        with(drawScope){
            rotate(degrees = rotationAmount){
                val path = Path()
                val size = drawScope.size
                val height =(size.width*0.9F)*progress.progressAmt;
                val distanceFromCenterLine = height* tan(triangleCenterAngleHalf)
                path.moveTo(size.width*0.5f, size.height*0.5f)
                path.lineTo(size.width*0.5f + height, ((size.height*0.5f)-distanceFromCenterLine))
                path.lineTo(size.width*0.5f + height, ((size.height*0.5f)+distanceFromCenterLine))
                path.lineTo(size.width*0.5f, size.height*0.5f)
                path.close()
                drawPath(path = path, color =progress.color, alpha = 0.5f)
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
//                progressAmount = 0.7f,
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