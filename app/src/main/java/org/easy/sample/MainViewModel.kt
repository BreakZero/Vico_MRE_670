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

class MainViewModel: ViewModel() {
    private val marketPager = Pager(
        config = PagingConfig(pageSize = Int.MAX_VALUE, prefetchDistance = 2),
        pagingSourceFactory = {
            MarketDataPagingSource()
        }
    )

    val marketInfoUiState = marketPager.flow.distinctUntilChanged().cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), PagingData.empty())
}