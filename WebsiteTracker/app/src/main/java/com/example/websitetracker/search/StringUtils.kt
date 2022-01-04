package com.example.websitetracker.search

fun String.addStartUrl() =
    if (this.startsWith(SearchFragment.START_URL)) this
    else SearchFragment.START_URL.plus(this)

fun String.removeWhitespace() = this.filter { !it.isWhitespace() }
