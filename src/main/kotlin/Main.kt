// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            App()
        }
    }
}

@Composable
@Preview
private fun photographerCardPreview() {
    MaterialTheme {
        App()
    }
}

@Composable
private fun App() {
    //photographerCard()
    //layoutCodeLab()
    listItems()
}

@Composable
fun listItems() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100){
            imageListItem(it)
        }
    }
}

@Composable
private fun imageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource("Android_Robot.png"),
            contentDescription = "Android Robot",
            modifier = Modifier.size(50.dp))
        Spacer(Modifier.width(10.dp))
        Text("Item $index", style = MaterialTheme.typography.subtitle1)
    }
}


@Composable
private fun layoutCodeLab() {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Catalogue")
            },
                actions = {
                    IconButton(
                        onClick = {

                        }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    }
                })

        }) { innerPadding ->
        bodyContent(Modifier.padding(innerPadding).padding(8.dp))
    }
}

@Composable
private fun bodyContent(modifier: Modifier = Modifier) {
    Column(modifier = Modifier) {
        Text("Hello there")
        Text("My beautiful lady")
    }
}

@Composable
private fun photographerCard(modifier: Modifier = Modifier) {
    Row(modifier) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) { }
        Column(
            modifier = Modifier.padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            // LocalContentAlpha is defining opacity level of its children
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

//-------------------------
//not working
@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color.Red,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}

//-----------------------------
//------------
//works but weirdly
@Composable
fun ScrollBox(
    modifier: Modifier = Modifier,
    columnVerticalArrangement: Arrangement.Vertical = Arrangement.Top,
    columnHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    rowHorizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    rowVerticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    val horizontalScrollState = rememberScrollState(0)
    val verticalScrollState = rememberScrollState(0)

    Box(modifier) {
        Row(
            Modifier.horizontalScroll(horizontalScrollState),
            horizontalArrangement = rowHorizontalArrangement,
            verticalAlignment = rowVerticalAlignment
        ) {
            Column(Modifier.verticalScroll(verticalScrollState), columnVerticalArrangement, columnHorizontalAlignment) {
                content()
            }
        }
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(horizontalScrollState),
            modifier = Modifier.align(Alignment.BottomEnd),
            style = LocalScrollbarStyle.current.copy(unhoverColor = LocalScrollbarStyle.current.hoverColor.copy(alpha = 0.35f))
        )
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalScrollState),
            modifier = Modifier.align(Alignment.CenterEnd),
            style = LocalScrollbarStyle.current.copy(unhoverColor = LocalScrollbarStyle.current.hoverColor.copy(alpha = 0.35f))
        )
    }
}
//usage of this
//BoxWithConstraints {
//    val parentHeight: Dp = with(LocalDensity.current)  { constraints.maxHeight.toDp() }
//
//    Box(
//        Modifier.background(Color.Gray)
//            .sizeIn(
//                minHeight = 50.dp,
//                maxHeight = parentHeight * 0.5f
//            )
//    ) {
//        ScrollBox {
//            Column{
//                repeat(100){
//                    Text("$it")
//                }}
//        }
//    }
//}
//------------




