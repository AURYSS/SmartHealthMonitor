package mx.utng.carh.wear.mqtt

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.utng.carh.smarthealthmonitor.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttWearPublisher(private val context: Context) {

    private var client: MqttAsyncClient? = null

    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttSharedConfig.BROKER_URL,
                MqttSharedConfig.CLIENT_WEAR,
                MemoryPersistence()
            )

            val options = MqttConnectOptions().apply {
                userName = MqttSharedConfig.USERNAME
                password = MqttSharedConfig.PASSWORD.toCharArray()
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = SSLSocketFactory.getDefault()
            }

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    android.util.Log.d("MQTT_WEAR", "✅ Conectado a HiveMQ Cloud")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    android.util.Log.e("MQTT_WEAR", "❌ Error de conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            android.util.Log.e("MQTT_WEAR", "❌ Error al configurar MQTT: ${e.message}")
        }
    }

    /** Publicar FC al topic MQTT */
    fun publishFC(bpm: Int, estado: String) {
        if (client?.isConnected != true) return

        try {
            val message = FcMessage(bpm = bpm, estado = estado)
            val payload = Json.encodeToString(message).toByteArray()

            val mqttMessage = MqttMessage(payload).apply {
                qos = MqttSharedConfig.QOS
                isRetained = true // el TV verá el último valor al conectarse
            }

            client?.publish(MqttSharedConfig.TOPIC_FC, mqttMessage)
            android.util.Log.d("MQTT_WEAR", "📤 Publicado: $bpm bpm -> ${MqttSharedConfig.TOPIC_FC}")
        } catch (e: Exception) {
            android.util.Log.e("MQTT_WEAR", "❌ Error al publicar: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
        } catch (e: Exception) {
            android.util.Log.e("MQTT_WEAR", "❌ Error al desconectar: ${e.message}")
        }
    }
}
