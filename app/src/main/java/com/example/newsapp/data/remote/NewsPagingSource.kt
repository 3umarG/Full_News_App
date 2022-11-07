package com.example.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import okio.IOException
import retrofit2.HttpException

private const val STARTING_PAGE_NUMBER = 1

class NewsPagingSource(val api: NewsAPI) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: STARTING_PAGE_NUMBER

        return try {
            val response = api.getBreakingNewsPaging(
                page = page
            )

            val list = response.body()!!.articles
            LoadResult.Page(
                data = list,
                prevKey = if (page == STARTING_PAGE_NUMBER) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )

        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}