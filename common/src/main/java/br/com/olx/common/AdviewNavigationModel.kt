package br.com.olx.common

import java.io.Serializable

data class AdviewNavigationModel(
    val images: List<String>,
    val price: String,
    val oldPrice: String,
    val title: String,
    val origListTime: String,
    val description: String,
    val sellerName: String,
    val phone: String
): Serializable