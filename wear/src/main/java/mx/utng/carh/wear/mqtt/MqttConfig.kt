package mx.utng.carh.wear.mqtt

object MqttConfig {
    // ⚠️ Reemplaza con los datos de TU cluster HiveMQ
    const val BROKER_URL  = "ssl://b85e90b2108c453793af746ddfb91a3e.s1.eu.hivemq.cloud:8883"
    const val USERNAME    = "aurora_robelo"  // del Access Management
    const val PASSWORD    = "Admin123*"

    // Topics del proyecto
    const val TOPIC_FC    = "utng/smarthealthmonitor/fc"

    // QoS: 0=best effort, 1=at least once, 2=exactly once
    const val QOS = 1

    // Client ID único
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
}
