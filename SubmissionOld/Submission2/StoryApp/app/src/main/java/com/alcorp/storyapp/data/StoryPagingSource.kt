package com.alcorp.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alcorp.storyapp.api.ApiService
import com.alcorp.storyapp.helper.PrefData
import com.alcorp.storyapp.model.ListStoryResponse

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryResponse> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getListStory(PrefData.token, position, params.loadSize).listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}