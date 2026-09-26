package com.bojanvilic.shelves.data.api

import com.bojanvilic.shelves.data.dto.AuthorDto
import com.bojanvilic.shelves.data.dto.SearchResponseDto
import com.bojanvilic.shelves.data.dto.SubjectResponseDto
import com.bojanvilic.shelves.data.dto.WorkDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("subjects/{subject}.json")
    suspend fun getSubject(
        @Path("subject") subject: String,
        @Query("limit") limit: Int = 10,
    ): SubjectResponseDto

    @GET("search.json")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i",
    ): SearchResponseDto

    @GET("works/{workId}.json")
    suspend fun getWork(
        @Path("workId") workId: String,
    ): WorkDto

    @GET("authors/{authorId}.json")
    suspend fun getAuthor(
        @Path("authorId") authorId: String,
    ): AuthorDto
}
