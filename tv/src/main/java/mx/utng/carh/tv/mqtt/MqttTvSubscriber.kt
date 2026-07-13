package mx.utng.carh.tv.mqtt

import android.content.Context
import kotlinx.serialization.json.Json
import mx.utng.carh.smarthealthmonitor.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttTvSubscriber(
    private val onMessageReceived: (TvMessage) -> Unit
) {
    private var client: MqttAsyncClient? = null

    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttSharedConfig.BROKER_URL,
                MqttSharedConfig.CLIENT_TV,
                MemoryPersistence()
            )

            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttSharedConfig.TOPIC_TV) {
                        try {
                            val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                            onMessageReceived(tvMsg)
                            android.util.Log.d("MQTT_TV", "📺 Recibido: ${tvMsg.bpm} bpm")
                        } catch (e: Exception) {
                            android.util.Log.e("MQTT_TV", "❌ Error al decodificar: ${e.message}")
                        }
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    android.util.Log.w("MQTT_TV", "⚠️ Conexión perdida: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            val options = MqttConnectOptions().apply {
                userName = MqttSharedConfig.USERNAME
                password = MqttSharedConfig.PASSWORD.toCharArray()
                isCleanSession = true
                socketFactory = SSLSocketFactory.getDefault()
            }

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttSharedConfig.TOPIC_TV, MqttSharedConfig.QOS)
                    android.util.Log.d("MQTT_TV", "✅ TV suscrita a ${MqttSharedConfig.TOPIC_TV}")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    android.util.Log.e("MQTT_TV", "❌ Error de conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            android.util.Log.e("MQTT_TV", "❌ Error: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
        } catch (e: Exception) {
            android.util.Log.e("MQTT_TV", "❌ Error al desconectar: ${e.message}")
        }
    }
}
