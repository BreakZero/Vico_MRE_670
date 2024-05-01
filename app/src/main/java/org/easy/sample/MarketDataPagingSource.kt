package org.easy.sample

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.easy.sample.network.ApiController

private const val PAGE_SIZE = 20

class MarketDataPagingSource(private val apiController: ApiController) :
    PagingSource<Int, MarketCoin>() {


    override fun getRefreshKey(state: PagingState<Int, MarketCoin>): Int? {
        return state.anchorPosition ?: 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketCoin> {
        return try {
            val currentPage = params.key ?: 1
            val marketCoins = apiController.getMarketData(
                page = currentPage,
                numCoinsPerPage = PAGE_SIZE
            )
//            val marketCoins = ((currentPage * PAGE_SIZE) until (currentPage + 1) * PAGE_SIZE).map {
//                println("=== $it")
//                buildMarketCoin(it.toString())
//            }
            LoadResult.Page(
                data = marketCoins,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (marketCoins.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

