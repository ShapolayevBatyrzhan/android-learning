package com.example.trainjun.navigation

object Routes {
    const val MOVIE_LIST = "movie_list"
    const val MOVIE_DETAIL = "movie_detail"
    const val FAVORITES = "favorites"
    const val ARG_MOVIE_ID = "movieId"
    const val MOVIE_DETAIL_PATTERN = "$MOVIE_DETAIL/{$ARG_MOVIE_ID}"

    fun movieDetail(id: Int) = "$MOVIE_DETAIL/$id"
}