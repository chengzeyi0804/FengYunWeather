package com.tomato.lib.net.parser

interface ResultParser<T> {
    fun parse(json: String): T
}