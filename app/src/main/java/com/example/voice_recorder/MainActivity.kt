package com.example.voice_recorder

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voice_recorder.playback.AndroidAudioPlayer
import com.example.voice_recorder.record.AndroidAudioRecorder
import com.example.voice_recorder.record.AudioRecorder
import com.example.voice_recorder.ui.theme.MyRecordings
import com.example.voice_recorder.ui.theme.Voice_RecorderTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.min

const val MAIN_SCREEN = "main_screen"
const val MY_RECORDINGS = "my_regordings"

class MainActivity : ComponentActivity() {

    val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        setContent {

            Voice_RecorderTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController
                    ,startDestination = MAIN_SCREEN
                ) {
                    composable(MAIN_SCREEN) {
                        MainScreen(navController = navController)
                    }
                    composable(MY_RECORDINGS) {
                        MyRecordings(navController = navController
                            ,player = player
                            ,audioFile = audioFile)
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavController) {
        val red200: Color = colorResource(R.color.red200)
        val white100: Color = colorResource(R.color.white100)

        val hours = remember { mutableStateOf(0) }
        val minutes = remember { mutableStateOf(0) }
        val seconds = remember { mutableStateOf(0) }
        val click = remember { mutableStateOf("Start") }
        val isRunning = remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val shadowVisible = remember { mutableStateOf(false) }

        var timerJob: Job? = null

        val colorTrRecord = remember { mutableStateOf(Color.White) }
        val colorGrRecord = remember { mutableStateOf(Color.Black) }
        val colorTrMerecordings = remember { mutableStateOf(Color.Transparent) }
        val colorGrMerecordings = remember { mutableStateOf(white100) }
        val colorTrFullacess = remember { mutableStateOf(Color.Transparent) }
        val colorGrFullacess = remember { mutableStateOf(white100) }

        fun startTimer() {
            timerJob = coroutineScope.launch {
                isRunning.value = true
                while (isRunning.value) {
                    delay(1000)
                    if (isRunning.value) {
                        seconds.value += 1
                        if (seconds.value >= 60) {
                            seconds.value = 0
                            minutes.value += 1
                        }
                        if (minutes.value >= 60) {
                            minutes.value = 0
                            hours.value += 1
                        }
                    }
                }
            }
        }

        fun stopTimer() {
            isRunning.value = false
            timerJob?.cancel()
            timerJob = null
        }

        fun record() {
            File(cacheDir, "audio.mp3").also {
                recorder.start(it)
                audioFile = it
            }
        }

        fun recordStop() {
            recorder.stop()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.red100))
        ) {
            Column (verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color.White)
                    ) {
                    }

                    Text(modifier = Modifier
                        .padding(start = 20.dp)
                        ,text = "Record it"
                        ,color = Color.White
                        ,fontSize = 32.sp)
                }

                Row (modifier = Modifier
                    .fillMaxWidth()) {
                    Button(modifier = Modifier
                        .padding(start = 15.dp, top = 35.dp)
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
                        onClick = {}
                    ) {
                        Text(modifier = Modifier
                            .fillMaxSize(),
                            text = "Record"
                            ,fontSize = 18.sp,
                            fontFamily = FontFamily(Typeface.DEFAULT_BOLD)
                        )
                    }

                    Button(modifier = Modifier
                        .padding(start = 5.dp, top = 35.dp)
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
                        onClick = {
                            navController.navigate(MY_RECORDINGS)
                            colorGrMerecordings.value = Color.Black
                            colorTrMerecordings.value = Color.White
                            colorGrRecord.value = white100
                            colorTrRecord.value = Color.Transparent
                        }
                    ) {
                        Text(modifier = Modifier
                            .fillMaxSize(),
                            text = "My recordings"
                            ,fontSize = 18.sp,
                            fontFamily = FontFamily(Typeface.DEFAULT_BOLD)
                        )
                    }

                    Button(modifier = Modifier
                        .padding(start = 5.dp, top = 35.dp)
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
            }
        }

        Box(contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(top = 165.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(color = Color.White)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .background(color = Color.White)
                        .fillMaxHeight(0.09f)
                        .fillMaxWidth(0.36f)
                        .border(
                            width = 1.dp, color = Color.Gray,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = String.format("%02d:%02d:%02d", hours.value, minutes.value, seconds.value),
                        color = Color.Red,
                        fontSize = 20.sp
                    )
                }

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            if (shadowVisible.value) {
                                val shadowRadius = -65.dp.toPx()
                                val shadowOffset = 0.dp.toPx()

                                drawCircle(
                                    color = red200,
                                    radius = (size.minDimension / 2) + shadowRadius,
                                    center = center,
                                    alpha = 0.5f
                                )
                            }
                        }
                ) {
                    Button(modifier = Modifier
                        .size(128.dp)
                        .border(5.dp,
                            color = colorResource(R.color.red100)
                            ,RoundedCornerShape(64.dp)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(R.color.red100),
                            containerColor = Color.White
                        ),
                        onClick = {
                            if (isRunning.value) {
                                recordStop()
                                hours.value = 0
                                minutes.value = 0
                                seconds.value = 0
                                stopTimer()
                                click.value = "Start"
                                shadowVisible.value = false
                            } else {
                                record()
                                startTimer()
                                isRunning.value = true
                                click.value = "Stop"
                                shadowVisible.value = true
                            }
                        }) {
                        Text(text = "${click.value}"
                            ,fontSize = 20.sp)
                    }
                }
            }
        }
    }
}