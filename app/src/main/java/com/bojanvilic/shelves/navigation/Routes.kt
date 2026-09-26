package com.bojanvilic.shelves.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DiscoverRoute

@Serializable
data object SearchRoute

@Serializable
data class BookDetailsRoute(val workId: String)
