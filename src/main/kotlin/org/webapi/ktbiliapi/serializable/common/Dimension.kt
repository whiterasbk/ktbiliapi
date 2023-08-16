package org.webapi.ktbiliapi.serializable.common

import kotlinx.serialization.Serializable

@Serializable
data class Dimension(
    val height: Int,
    val rotate: Int,
    val width: Int
)