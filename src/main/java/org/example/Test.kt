package org.example

import io.ktor.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.crypto.base64
import org.ton.lite.client.AccountState
import org.ton.lite.client.LiteClient
import java.util.concurrent.CompletableFuture

suspend fun Test() {
   var json = Json {
        ignoreUnknownKeys = true
    }
    val liteClient = Any() as LiteClient
    val account = liteClient.getAccount("") ?: error("address not found")
    val balance = account.info.storage.balance


    val transactions = liteClient.getLastTransactions("", account.lastTransactionLt, account.lastTransactionHash)
    transactions.forEach {
        println("")
    }
}

fun teleport(location: Location) {

}

class Location(
    val x: Int,
    val y: Int,
    val z: Int
)