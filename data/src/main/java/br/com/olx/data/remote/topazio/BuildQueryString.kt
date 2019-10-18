package br.com.olx.data.remote.topazio

fun buildQueryString(queryParams: String): String {
    return "{\"query\":\"query getAdsFromQueryString(\$queryString: String!) {    " +
            "ads(queryString: \$queryString) {      " +
            "pagination      " +
            "ads {        " +
            "listId        " +
            "body        " +
            "subject        " +
            "origListTime        " +
            "priceValue        " +
            "oldPrice        " +
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
            "label          " +
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
            "      }    }  }  \"" +
            ",\"variables\":{\"queryString\":\"$queryParams\"}" +
            ",\"operationName\":\"getAdsFromQueryString\"}"
}