package br.com.olx.adview

data class AdviewModel(
    val images: List<String>,
    val price: String,
    val oldPrice: String,
    val title: String,
    val origListTime: String,
    val description: String,
    val sellerName: String,
    val phone: String
)