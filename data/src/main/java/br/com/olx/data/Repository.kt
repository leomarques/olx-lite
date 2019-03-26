package br.com.olx.data

interface Repository {
    fun search(): AdSearchResult
}