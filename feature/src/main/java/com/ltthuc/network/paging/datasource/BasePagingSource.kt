package com.ltthuc.network.paging.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ltthuc.network.response.PagedResponse
import retrofit2.Response

abstract class BasePagingSource<Item : Any> : PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val pageNumber = params.key ?: 1
        return try {
            val response = getApiService(getParams(params))
            val pagedResponse = response.body()
            val data = pagedResponse?.results

            var nextPageNumber: Int? = null
            if (pageNumber!= pagedResponse?.totalPages) {

                nextPageNumber = pageNumber + 1
            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    open fun getFirstPage(): Int = 1

    abstract suspend fun getApiService( hashMap: HashMap<String, String> = HashMap()): Response<PagedResponse<Item>>
    abstract fun getParams(params: LoadParams<Int>):HashMap<String,String>



}

