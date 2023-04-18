package com.example.basicpaging.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.lang.Integer.max
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
private val firstArticleCreatedTime = LocalDateTime.now()

private const val LOAD_DELAY_MILLIS = 3_000L

const val STARTING_KEY = 0

class ArticlePagingSource: PagingSource<Int, Article>() {

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + params.loadSize)

        if (start != STARTING_KEY) delay(LOAD_DELAY_MILLIS)
        return LoadResult.Page(
            data = range.map { number ->
                Article(
                    // Generate consecutive increasing numbers as the article id
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    created = firstArticleCreatedTime.minusDays(number.toLong())
                )
            },

            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }
}