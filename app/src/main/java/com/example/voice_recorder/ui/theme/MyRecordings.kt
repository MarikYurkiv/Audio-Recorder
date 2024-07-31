package com.example.voice_recorder.ui.theme

import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavController
import com.example.voice_recorder.MAIN_SCREEN
import com.example.voice_recorder.R
import com.example.voice_recorder.playback.AndroidAudioPlayer
import java.io.File

@Composable
fun MyRecordings(
    navController: NavController,
    player: AndroidAudioPlayer,
    audioFile: File?) {

    val white100: Color = colorResource(R.color.white100)

    val colorTrRecord = remember { mutableStateOf(Color.Transparent) }
    val colorGrRecord = remember { mutableStateOf(white100) }
    val colorTrMerecordings = remember { mutableStateOf(Color.White) }
    val colorGrMerecordings = remember { mutableStateOf(Color.Black) }
    val colorTrFullacess = remember { mutableStateOf(Color.Transparent) }
    val colorGrFullacess = remember { mutableStateOf(white100) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.red100))
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()) {
            Button(modifier = Modifier
                .padding(start = 15.dp, top = 30.dp)
                .size(80.dp, 36.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorGrRecord.value,
                    containerColor = colorTrRecord.value),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(
                    start = 9.dp,
                    top = 7.dp,
                    bottom = 5.dp,
                    end = 0.dp),
                onClick = {
                    navController.navigate(MAIN_SCREEN)
                    colorGrMerecordings.value = white100
                    colorTrMerecordings.value = Color.Transparent
                    colorGrRecord.value = Color.Black
                    colorTrRecord.value = Color.White
                }
            ) {
                Text(modifier = Modifier
                    .fillMaxSize(),
                    text = "Record"
                    ,fontSize = 18.sp,
                    fontFamily = FontFamily(Typeface.DEFAULT_BOLD)
                )
            }

            Button(modifier = Modifier
                .padding(start = 5.dp, top = 30.dp)
                .size(140.dp, 36.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorGrMerecordings.value,
                    containerColor = colorTrMerecordings.value),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(
                    start = 9.dp,
                    top = 7.dp,
                    bottom = 5.dp,
                    end = 0.dp),
                onClick = {}
            ) {
                Text(modifier = Modifier
                    .fillMaxSize(),
                    text = "My recordings"
                    ,fontSize = 18.sp,
                    fontFamily = FontFamily(Typeface.DEFAULT_BOLD)
                )
            }

            Button(modifier = Modifier
                .padding(start = 5.dp, top = 30.dp)
                .size(100.dp, 36.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorGrFullacess.value,
                    containerColor = colorTrFullacess.value),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(
                    start = 9.dp,
                    top = 7.dp,
                    bottom = 5.dp,
                    end = 0.dp),
                onClick = {}
            ) {
                Text(modifier = Modifier
                    .fillMaxSize(),
                    text = "Full acess"
                    ,fontSize = 18.sp,
                    fontFamily = FontFamily(Typeface.DEFAULT_BOLD)
                )
            }
        }


        Box(
            modifier = Modifier
                .padding(top = 40.dp, start = 24.dp, end = 24.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Button (onClick = {
                player.playFile(audioFile ?: return@Button) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(42.dp)
                    .background(Color.White)
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 3,
                            radius = size.minDimension / 2,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 10f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                        onDrawBehind {
                            drawPath(roundedPolygonPath, color = Color.Black)
                        }
                    }
            ) {}
        }
    }
}