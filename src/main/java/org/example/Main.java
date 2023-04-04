package org.example;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.*;
import kotlinx.coroutines.future.FutureKt;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonKt;
import org.ton.api.liteclient.config.LiteClientConfigGlobal;
import org.ton.block.Account;
import org.ton.block.AccountInfo;
import org.ton.block.AddrStd;
import org.ton.lite.client.LiteClient;
import org.ton.lite.client.internal.FullAccountState;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        Json json = JsonKt.Json(Json.Default, (builder) -> {
            builder.setIgnoreUnknownKeys(true);
            return Unit.INSTANCE;
        });

        LiteClientConfigGlobal liteClientConfigGlobal = json.decodeFromString(
                LiteClientConfigGlobal.Companion.serializer(),
                Objects.requireNonNull(getJson())
        );

        CoroutineContext context = (CoroutineContext) Dispatchers.getDefault();
        try (LiteClient liteClient = new LiteClient(context, liteClientConfigGlobal)) {
            String address = "EQDhfNEHdK06MNRjGyx5h0Pao5NgqFTE8ug2SrZ14c6bJnJF";
            AddrStd addrStd = AddrStd.parse(address);

            Future<FullAccountState> future = callSuspend((c) -> liteClient.getAccountState(addrStd, c));
            FullAccountState fullAccountState = future.get();
            Account account = fullAccountState.account().getValue();

            if (account instanceof AccountInfo) {
                String balance = ((AccountInfo) account).storage().balance().coins().toString();
                System.out.println(balance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> Future<T> callSuspend(Function<Continuation<? super T>, Object> continuation) {
        Deferred<T> async = BuildersKt.async(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT,
                ((coroutineScope, continuation1) -> continuation.apply(continuation1))
        );
        return FutureKt.asCompletableFuture(async);
    }

    public static String getJson() {
        try {
            // URL, откуда необходимо скачать JSON
            String urlString = "https://ton.org/global-config.json";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}