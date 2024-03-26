package it.polito.wa2.g13.document_store.util

import java.util.*

fun <T> Optional<T>.nullable(): T? = orElse(null)