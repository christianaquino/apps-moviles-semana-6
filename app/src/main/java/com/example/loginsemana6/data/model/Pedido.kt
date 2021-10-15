package com.example.loginsemana6.data.model

import android.location.Location
import android.location.LocationManager

class Pedido {
    val distancia: Float
    val envio: Float
    constructor(monto: Int, direccion: Location) {
        val locationCentral = Location(LocationManager.NETWORK_PROVIDER)
        locationCentral.latitude = -33.3877
        locationCentral.longitude = -70.6771
        distancia = locationCentral.distanceTo(direccion) / 1000

        envio = if (distancia < 20000) {
            if(monto >= 50000) {
                0.0f
            } else {
                if (monto >= 25000) {
                    distancia * 150
                } else {
                    distancia * 300
                }
            }
        } else {
            -1f
        }
    }
}