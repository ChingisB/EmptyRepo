package com.example.data.mapper

interface Mapper<in Input, out Output> {

    fun convert(input: Input): Output
}