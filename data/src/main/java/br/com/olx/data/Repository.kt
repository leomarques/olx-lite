package br.com.olx.data

interface Repository {
    fun search(keyword: String): AdSearchResult
}
