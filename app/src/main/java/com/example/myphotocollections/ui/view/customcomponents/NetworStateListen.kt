package com.example.myphotocollections.ui.view.customcomponents

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun observeConnectivityState(): State<Boolean> {
    val context = LocalContext.current
    val connectivityManager = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val isConnected = remember { mutableStateOf(false) }

    DisposableEffect(connectivityManager) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected.value = true
            }

            override fun onLost(network: Network) {
                isConnected.value = false
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    return isConnected
}