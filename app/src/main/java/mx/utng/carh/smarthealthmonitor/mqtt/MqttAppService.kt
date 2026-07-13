package mx.utng.carh.smarthealthmonitor.mqtt

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.utng.carh.smarthealthmonitor.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.net.ssl.SSLSocketFactory

class MqttAppService(
    private val onFcUpdate: (Int) -> Unit // Callback para actualizar el Repository
) {
    private var client: MqttAsyncClient? = null

    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttSharedConfig.BROKER_URL,
                MqttSharedConfig.CLIENT_APP,
                MemoryPersistence()
            )

            val options = MqttConnectOptions().apply {
                userName = MqttSharedConfig.USERNAME
                password = MqttSharedConfig.PASSWORD.toCharArray()
                isCleanSession = true
                socketFactory = SSLSocketFactory.getDefault()
            }

            // Callback de mensajes entrantes
            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    when (topic) {
                        MqttSharedConfig.TOPIC_FC -> handleFcMessage(msg)
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    android.util.Log.w("MQTT_APP", "⚠️ Conexión perdida: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    // Suscribirse al topic de FC del reloj
                    client?.subscribe(MqttSharedConfig.TOPIC_FC, MqttSharedConfig.QOS)
                    android.util.Log.d("MQTT_APP", "✅ Conectado y suscrito a ${MqttSharedConfig.TOPIC_FC}")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    android.util.Log.e("MQTT_APP", "❌ Error de conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            android.util.Log.e("MQTT_APP", "❌ Error al configurar MQTT: ${e.message}")
        }
    }

    private fun handleFcMessage(msg: MqttMessage) {
        try {
            val fcMsg = Json.decodeFromString<FcMessage>(String(msg.payload))

            // 1. Actualizar el Repository mediante el callback
            onFcUpdate(fcMsg.bpm)

            // 2. Re-publicar al topic TV con formato enriquecido
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val tvMsg = TvMessage(bpm = fcMsg.bpm, estado = fcMsg.estado, hora = hora)
            val tvPayload = Json.encodeToString(tvMsg).toByteArray()
            val tvMqtt = MqttMessage(tvPayload).apply {
                qos = MqttSharedConfig.QOS
                isRetained = true
            }
            client?.publish(MqttSharedConfig.TOPIC_TV, tvMqtt)
            android.util.Log.d("MQTT_APP", "🔁 Re-publicado al TV: ${fcMsg.bpm} bpm")
        } catch (e: Exception) {
            android.util.Log.e("MQTT_APP", "❌ Error al manejar mensaje: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
        } catch (e: Exception) {
            android.util.Log.e("MQTT_APP", "❌ Error al desconectar: ${e.message}")
        }
    }
}
