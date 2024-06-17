package me.prashant.cleannews.util.core

interface Mapper<F, T> {
    fun convert(from: F): T
}
