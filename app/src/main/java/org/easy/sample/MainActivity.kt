package org.easy.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.easy.sample.ui.theme.Vico_670_MRETheme
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart as rememberCartesianChart1

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Vico_670_MRETheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val pagingItems = mainViewModel.marketInfoUiState.collectAsLazyPagingItems()
                    DefaultPagingLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        pagingItems = pagingItems,
                        itemKey = { index -> pagingItems[index]!!.id },
                        itemContainer = {
                            MarketCoinItem(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                marketCoin = it
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun MarketCoinItem(
    modifier: Modifier = Modifier,
    marketCoin: MarketCoin
) {
    val modelProducer = remember {
        CartesianChartModelProducer.build()
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            modelProducer.tryRunTransaction {
                lineSeries { series(marketCoin.price) }
            }
        }
    }
    Card(modifier = modifier, onClick = { /*TODO*/ }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = marketCoin.name)
                Text(text = marketCoin.symbol.uppercase())
            }
            Spacer(modifier = Modifier.weight(1.0f))
            val graphColor = if (marketCoin.priceChangePercentage24h > 0.0) {
                MaterialTheme.colorScheme.error
            } else MaterialTheme.colorScheme.tertiary
            CartesianChartHost(
                modifier = Modifier
                    .height(48.dp)
                    .aspectRatio(2.0f),
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(listOf(rememberLineSpec(DynamicShader.color(graphColor)))),
                    startAxis = rememberStartAxis(
                        label = null,
                        axis = null,
                        tick = null,
                        guideline = null
                    ),
                    bottomAxis = rememberBottomAxis(
                        label = null,
                        axis = null,
                        tick = null,
                        guideline = null
                    )
                ),
                modelProducer = modelProducer,
                horizontalLayout = HorizontalLayout.fullWidth()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = marketCoin.currentPrice.toString())
                Text(text = "${marketCoin.priceChangePercentage24h} %", color = graphColor)
            }
        }
    }
}

@Preview
@Composable
private fun MarketCoinItem_Preview() {
    MarketCoinItem(
        marketCoin = buildMarketCoin("1")
    )
}

