package com.bojanvilic.shelves.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SubjectResponseDto(
    val works: List<SubjectWorkDto>? = null,
)

@Serializable
data class SubjectWorkDto(
    val key: String? = null,
    val title: String? = null,
    val authors: List<SubjectAuthorDto>? = null,
    @SerialName("cover_id") val coverId: Long? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
)

@Serializable
data class SubjectAuthorDto(
    val name: String? = null,
)

@Serializable
data class SearchResponseDto(
    val docs: List<SearchDocDto>? = null,
)

@Serializable
data class SearchDocDto(
    val key: String? = null,
    val title: String? = null,
    @SerialName("author_name") val authorName: List<String>? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("cover_i") val coverI: Long? = null,
)

@Serializable
data class WorkDto(
    val key: String? = null,
    val title: String? = null,
    val description: JsonElement? = null,
    val authors: List<WorkAuthorRefDto>? = null,
    val covers: List<Long>? = null,
    @SerialName("first_publish_date") val firstPublishDate: String? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
)

@Serializable
data class WorkAuthorRefDto(
    val author: WorkAuthorKeyDto? = null,
)

@Serializable
data class WorkAuthorKeyDto(
    val key: String? = null,
)

@Serializable
data class AuthorDto(
    val key: String? = null,
    val name: String? = null,
)
