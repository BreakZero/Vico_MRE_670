package org.easy.sample.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import org.easy.sample.MarketCoin

class ApiController {
    private val httpClient: HttpClient
        get() = HttpClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(
                    kotlinx.serialization.json.Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        allowStructuredMapKeys = true
                    }
                )
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.coingecko.com"
                    path("api/v3/")
                    parameters.append("x_cg_demo_api_key", "CG-7r3BXakbsCLXSKZWVTx22dWy")
                }
                header("Content-Type", "application/json")
            }
        }

    suspend fun getMarketData(
        page: Int,
        numCoinsPerPage: Int,
    ): List<MarketCoin> {
        return try {
            httpClient.get("coins/markets") {
                parameter("vs_currency", "usd")
                parameter("page", page)
                parameter("per_page", numCoinsPerPage)
                parameter("order", "market_cap_desc")
                parameter("sparkline", true)
            }.body<List<CoinGeckoMarketsDto>>().map {
                MarketCoin(
                    id = it.id,
                    symbol = it.symbol,
                    name = it.name,
                    image = it.image,
                    currentPrice = it.currentPrice,
                    priceChangePercentage24h = it.priceChangePercentage24h,
                    price = it.sparklineIn7d?.price ?: emptyList()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}