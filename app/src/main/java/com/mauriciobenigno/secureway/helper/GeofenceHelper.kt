package com.mauriciobenigno.secureway.helper

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import com.mauriciobenigno.secureway.services.GeofenceBroadcastReceiver


/*
class GeofenceHelper(context: Context) {

    fun buildGeofencingRequest(geofences: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(0)
            .addGeofences(geofences)
            .build()
    }

    val geofencePendingIntent: PendingIntent by lazy {
        //val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        val intent = Intent(context, GeofenceTransitionsJobIntentService::class.java)
        //PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        PendingIntent.getBroadcast(
            context,
            0,
            StartJobIntentServiceReceiver.getIntent(context, intent, GeofenceTransitionsJobIntentService.JOB_ID),
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @SuppressLint("MissingPermission")
    fun configurarCercaMacro(context: Context, userLat: Double, userLong: Double, geofencingClient: GeofencingClient) {

        // Cria a região macro com 20km de raio
        val parentGeoFence = GeoCerca("macro", userLat, userLong, 20000.0)

        // Cria a cerca virtual para a região macro
        val geofence = buildGeoFence(parentGeoFence, GeoFenceType.MACRO)

        // Adiciona a cerca virtual
        if (geofence != null) {
            geofencingClient.addGeofences(buildGeofencingRequest(listOf(geofence)), geofencePendingIntent)
                .addOnSuccessListener {
                    Log.v("geocerca", "certo macro")
                }
                .addOnFailureListener {
                    Log.v("geocerca", "erro macro")
                }
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    fun configurarCercaMicro(context: Context, geofenceModelList: List<GeoCerca>, currentLat: Double, currentLong: Double, geofencingClient: GeofencingClient) {

        //Filtra as micro cercas com base na localização do usuario
        val microRegioes = filtrarZonasByLocation(geofenceModelList, currentLat, currentLong)

        microRegioes.forEach { micro ->
            //Cria as cercas virtuais para as micro regiões
            val geofence = buildGeoFence(micro, GeoFenceType.MICRO)

            if (geofence != null ){
                geofencingClient.addGeofences(buildGeofencingRequest(listOf(geofence)), geofencePendingIntent)
                    .addOnSuccessListener {
                        Log.v("geocerca", "certo micro")
                    }
                    .addOnFailureListener {
                        Log.v("geocerca", "erro micro")
                    }
            }
        }

        //Salvar o id das cercas criadas atualmente
        val cercasMicroRegiao = microRegioes.map { it.id }
        Preferences(context).salvarGeoCercaAtual = cercasMicroRegiao as ArrayList<String>
    }

    private fun filtrarZonasByLocation(zonas: List<GeoCerca>, lat: Double, long: Double): List<GeoCerca> {
        return zonas.filter { model ->
            val results = FloatArray(1)

            // Verifica se o usuário ultrapassou o raio de 20 km, para que a aplicação gere novas cercas virtuais
            Location.distanceBetween(model.lat, model.lng, lat, long, results)
            results[0] < 20000
        }
    }


    fun buildGeoFence(geofence: GeoCerca, type: GeoFenceType): Geofence? {

        val latitude = geofence.lat
        val longitude = geofence.lng
        val radius = geofence.radius

        val builder = Geofence.Builder()
            .setRequestId(geofence.id)
            .setCircularRegion(
                latitude,
                longitude,
                radius.toFloat()
            )
            .setNotificationResponsiveness(0)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)

        //Saindo da região macro
        if (type == GeoFenceType.MACRO) {
            builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
        } else { //Entrando em região micro
            builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        }

        return builder.build()
    }

}
*/
enum class GeoFenceType {
    MACRO, MICRO
}

class GeofenceHelper(base: Context?) : ContextWrapper(base) {
    var pendingIntent: PendingIntent? = null
    fun getGeofencingRequest(geofence: Geofence?): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(ID: String?, latLng: LatLng, radius: Float, transitionTypes: Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, radius)
            .setRequestId(ID)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(5000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    @JvmName("getPendingIntent1")
    fun getPendingIntent(): PendingIntent {
        if (pendingIntent != null) {
            return pendingIntent!!
        }
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        pendingIntent =
            PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent!!
    }

    fun getErrorString(e: Exception): String {
        if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> return "GEOFENCE_NOT_AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> return "GEOFENCE_TOO_MANY_GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> return "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            }
        }
        return e.localizedMessage
    }

    companion object {
        private const val TAG = "GeofenceHelper"
    }
}