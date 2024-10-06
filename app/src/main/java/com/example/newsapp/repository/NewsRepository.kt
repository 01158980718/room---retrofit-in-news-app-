package com.example.newsapp.repository


import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getHeadlines(countryCode, pageNumber)
    }

    suspend fun searchForNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
    }

    suspend fun upsert(article: Article) {
        db.getArticleDao().upSert(article)
    }

     fun getFavouritesNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) {
        db.getArticleDao().deleteArticle(article)
    }
}
