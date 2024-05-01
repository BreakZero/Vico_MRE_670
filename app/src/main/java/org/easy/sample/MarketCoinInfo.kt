package org.easy.sample

import kotlin.random.Random

data class MarketCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val price: List<Double>
)

fun buildMarketCoin(id: String): MarketCoin {
    return MarketCoin(
        id = id,
        symbol = "BTC_$id",
        name = "Bitcoin_$id",
        image = "https://assets.coingecko.com/coins/images/11795/large/Untitled_design-removebg-preview.png?1696511671",
        currentPrice = 66666.0,
        priceChangePercentage24h = 0.005,
        price = List(168) { Random.nextDouble(40000.0 , 70000.0) }
    )
}