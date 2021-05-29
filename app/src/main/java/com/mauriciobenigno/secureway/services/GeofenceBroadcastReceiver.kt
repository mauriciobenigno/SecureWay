package com.mauriciobenigno.secureway.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.mauriciobenigno.secureway.helper.NotificationHelper
import com.mauriciobenigno.secureway.ui.activity.PrincipalActivity


/*
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object{
        const val JOB_ID = 50
    }

    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        //Entrando em microregiao
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            var geofence = event.triggeringGeofences[0]

            val notificationHelper = NotificationHelper(context)
            notificationHelper.gerarNotificacao("Alerta, região perigosa!", "A zona onde você se encontra foi relatada como perigosa")

        }
        // Saindo de macro região
        else if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            val geofencingClient = LocationServices.getGeofencingClient(context)

            // Remove as micro regiões antes de iniciar uma nova
            //unregisterExistingGeofences(geofencingClient)
            geofencingClient.removeGeofences(Preferences(context).salvarGeoCercaAtual)

            val helper = GeofenceHelper(context)

            helper.configurarCercaMacro(context, event.triggeringLocation.latitude, event.triggeringLocation.longitude, geofencingClient)

            helper.configurarCercaMicro(context, AppRepository(context).getDangerZonesBackground().map { GeoCerca(it.id_zona.toString(), it.coordenada_x, it.coordenada_y, 5.0) }, event.triggeringLocation.latitude, event.triggeringLocation.longitude, geofencingClient)
        }
    }
}

*/

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // an Intent broadcast.
//        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();
        val notificationHelper = NotificationHelper(context)
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...")
            return
        }
        val geofenceList = geofencingEvent.triggeringGeofences
        for (geofence in geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.requestId)
        }
        //        Location location = geofencingEvent.getTriggeringLocation();
        val transitionType = geofencingEvent.geofenceTransition
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Toast.makeText(context, "Você entrou numa zona perigosa", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification(
                    "Zona perigosa", "Você entrou em uma zona considerada perigosa",
                    PrincipalActivity::class.java
                )
            }
        }
    }

    companion object {
        private const val TAG = "GeofenceBroadcastReceiv"
    }
}