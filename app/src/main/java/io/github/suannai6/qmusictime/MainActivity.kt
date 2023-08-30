package io.github.suannai6.qmusictime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import io.github.suannai6.qmusictime.ui.theme.QMusicTimeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QMusicTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf("") }
    val context = LocalContext.current
    var qq by rememberSaveable { mutableStateOf("") }
    var qq1 by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = qq,
            onValueChange = { qq = it },
            label = { Text("QQ账号") },
            singleLine = true,
            leadingIcon = { Image(painterResource(id = R.drawable.qq), contentDescription = "QQ账号") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(
            visible = qq.isNotBlank(), // 一个布尔值，表示视图是否可见
            enter = fadeIn() + expandVertically(), // 进入动画，包括淡入和垂直展开
            exit = fadeOut() + shrinkVertically() // 退出动画，只有淡出
        ) {
            // 这里放置要展开的视图内容
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {}
                ) {
                    Text(text = "加入反馈群")
                }
                Button(
                    onClick = {
                        isButtonClicked = true // 设置按钮已点击的标志
                        qq1 = qq // 设置 qq1 的新值
                    },
                    enabled = qq.isNotBlank()
                ) {
                    Text(text = "刷取时长")
                }
                Button(
                    onClick = {},
                    enabled = false
                ) {
                    Text(text = "微信刷时长(制作ing)")
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(onDismissRequest = { /*TODO*/ }) {
            Surface(
                modifier = Modifier,
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    CircularProgressIndicator() // 显示圆形进度条
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text =  "                    正在刷取时间...                    ")
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

    // 使用LaunchedEffect来监听isButtonClicked的变化
    if (isButtonClicked) {
        LaunchedEffect(qq1) {
            if (qq1.isNotBlank()) {
                try {
                    showDialog.value = true
                    val response = fetchDataInBackground("https://decode.xiaojieapi.com/api/qqmusic.php?qq=${qq}&time=300")
                    result = response
                    // 在点击按钮后显示 Toast
                    showDialog.value = false
                    Toast.makeText(context, "已发送请求，每日上限300分钟，刷取一次即可(软件制作：葫芦侠@酸奶)", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "${result}", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    // 处理错误
                }
            }
        }
    }
}

@Throws(IOException::class)
suspend fun fetchDataInBackground(url: String): String = withContext(Dispatchers.IO) {
    try {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return@withContext response.body?.string() ?: ""
    } catch (e: IOException) {
        e.printStackTrace()
        // 处理错误
        return@withContext ""
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QMusicTimeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting()
        }
    }
}