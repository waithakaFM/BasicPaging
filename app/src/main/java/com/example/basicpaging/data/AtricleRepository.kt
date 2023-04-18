package com.example.basicpaging.data


/**
 * Repository class that mimics fetching [Article] instances from an asynchronous source.
 */

class ArticleRepository {
//    /**
//     * Exposed singular stream of [Article] instances
//     */
//    @RequiresApi(Build.VERSION_CODES.O)
//    val articleStream: Flow<List<Article>> = flowOf(
//        (0..500).map { number ->
//            Article(
//                id = number,
//                title = "Article $number",
//                description = "This describes the article $number",
//                created = firstArticleCreatedTime.minusDays(number.toLong())
//            )
//        }
//    )

    fun articlePagingSource() = ArticlePagingSource()
}