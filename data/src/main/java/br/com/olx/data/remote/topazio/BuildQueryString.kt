package br.com.olx.data.remote.topazio

fun buildQueryString(queryParams: String): String {
    return "{\"query\":\"query getAdsFromQueryString(\$queryString: String!) {    " +
            "ads(queryString: \$queryString) {      " +
            "pagination      " +
            "ads {        " +
            "listId        " +
            "rankId        " +
            "lastBumpAgeSecs        " +
            "isFavorited        " +
            "subject        " +
            "origListTime        " +
            "priceValue        " +
            "oldPrice        " +
            "professionalAd        " +
            "featured        " +
            "location {          " +
            "neighbourhood          " +
            "municipality          " +
            "uf        }        " +
            "images {          " +
            "baseUrl          " +
            "path        }        " +
            "properties {          " +
            "name          " +
            "value        }        " +
            "thumbnail {          " +
            "baseUrl          " +
            "path        }        " +
            "user {          " +
            "name          " +
            "accountId        }        " +
            "phone {          " +
            "phone          " +
            "phoneHidden          " +
            "phoneVerified        }        " +
            "adReply      }    }  }  \"" +
            ",\"variables\":{\"queryString\":\"$queryParams\"}" +
            ",\"operationName\":\"getAdsFromQueryString\"}"
}