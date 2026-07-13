package mx.utng.carh.smarthealthmonitor.mqtt

object MqttSharedConfig {
    const val BROKER_URL  = "ssl://b85e90b2108c453793af746ddfb91a3e.s1.eu.hivemq.cloud:8883"
    const val USERNAME    = "aurora_robelo"
    const val PASSWORD    = "Admin123*"

    const val TOPIC_FC    = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV    = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"

    const val QOS = 1

    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP  = "smarthealthmonitor-app"
    const val CLIENT_TV   = "smarthealthmonitor-tv"
}
