package org.webapi.ktbiliapi.serializable

import kotlin.properties.Delegates

abstract class CommonResponseBody {
    lateinit var iMessage: String
    var iCode by Delegates.notNull<Int>()
}