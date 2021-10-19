package com.example.loginsemana6.data.model

import android.location.Location
import android.location.LocationManager
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class Pedido(email: String, direccion: Location) {
    val distancia: Float
    val envio: Int
    val monto: Int

    init {
        val locationCentral = Location(LocationManager.NETWORK_PROVIDER)
        locationCentral.latitude = -33.3877
        locationCentral.longitude = -70.6771
        distancia = locationCentral.distanceTo(direccion) / 1000
        monto = getMonto(email)

        envio = if (distancia < 20) {
            if(monto >= 50000) {
                0
            } else {
                if (monto >= 25000) {
                    (distancia * 150).roundToInt()
                } else {
                    (distancia * 300).roundToInt()
                }
            }
        } else {
            -1
        }
    }

    private fun getMonto(email: String): Int {
        return when(email) {
            "test@yopmail.com" -> 36490
            "test2@yopmail.com" -> 23290
            "test3@yopmail.com" -> 67800
            else -> {
                0
            }
        }
    }
}