package com.malkinfo.editingrecyclerview.parsing

import com.malkinfo.editingrecyclerview.model.ClassLink
import java.net.URI

class parsingurl {
    fun fromUrl(urlString: String, id: Int): ClassLink {
        val cleanUrlString = urlString.replace("Applink://", "")
        val url = URI(cleanUrlString)
        val app_name = "LinkManager"
        val UrlUUiD = urlString
        val uuid = UrlUUiD.substringAfter("://").substringBefore("@")
        val protocol = url.scheme
        val ares = url.host
        val port = url.port
        val query = url.rawQuery.split("&")
        val security = query.firstOrNull { it.startsWith("security=") }?.substringAfter("security=")
        val sni = query.firstOrNull { it.startsWith("sni=") }?.substringAfter("sni=")
        val public_key = query.firstOrNull { it.startsWith("pbk=") }?.substringAfter("pbk=")
        val browser = query.firstOrNull { it.startsWith("fp=") }?.substringAfter("fp=")
        val type = query.firstOrNull { it.startsWith("type=") }?.substringAfter("type=")
        val flow = query.firstOrNull { it.startsWith("flow=") }?.substringAfter("flow=")
        val country = url.fragment ?: ""
        return ClassLink(id,protocol, app_name, uuid, ares, port, security ?: "", sni ?: "", public_key
            ?: "", browser ?: "", type ?: "", flow ?: "", country)
    }
}