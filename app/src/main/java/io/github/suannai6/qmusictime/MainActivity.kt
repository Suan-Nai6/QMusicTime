/*
 * 项目昵称：QMusicTime
 * 项目作者：酸奶
 * 模块昵称：QMusicTime.app.main
 * 最后修改时间：2023/8/30 下午8:51
 * Copyright© (c) 2017-2023 酸奶 https://github.com/Suan-Nai6
 */

package io.github.suannai6.qmusictime

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import io.github.suannai6.qmusictime.ui.theme.QMusicTimeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.selects.selectUnbiased
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

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
    var showDialog = remember { mutableStateOf(false) }
    var showDialog1 = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = qq,
            onValueChange = { qq = it },
            label = { Text("QQ账号") },
            keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number),
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
                    onClick = { showDialog1.value = true }
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
    if (showDialog1.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                showDialog1.value = false
            },
            title = {
                Text(text = "加入反馈群")
            },
            text = {
                SelectionContainer {
                    Text(text = "该软件只在葫芦侠发布并且完全免费，如果你在其他地方看到的请加入反馈群举报\n当然，你也可以在里面给我们说说开发什么新软件\n反馈群群号：1135205594")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Toast.makeText(context,"技术原因，请长按复制",Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("复制群号到剪切板")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog1.value = false
                    }
                ) {
                    Text("关闭")
                }
            }
        )
    }

    // 使用LaunchedEffect来监听isButtonClicked的变化
    if (isButtonClicked) {
        LaunchedEffect(Unit) {
            if (qq1.isNotBlank()) {
                try {
                    showDialog.value = true
                    val response = fetchDataInBackground("https://decode.xiaojieapi.com/api/qqmusic.php?qq=${qq}&time=300")
                    result = response
                    // 在点击按钮后显示 Toast
                    showDialog.value = false
                    Toast.makeText(context, "已发送请求，刷取一次即可(软件制作：葫芦侠@酸奶)", Toast.LENGTH_SHORT).show()
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