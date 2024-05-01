package org.easy.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import org.easy.sample.network.ApiController

class MainViewModel: ViewModel() {

    private val apiController = ApiController()

    private val marketPager = Pager(
        config = PagingConfig(pageSize = Int.MAX_VALUE, prefetchDistance = 2),
        pagingSourceFactory = {
            MarketDataPagingSource(apiController)
        }
    )

    val marketInfoUiState = marketPager.flow.distinctUntilChanged().cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), PagingData.empty())
}