package sc.azsphere.weatherstation

data class SensorItem(val type: String, val value: String, val unit: String) {
    override fun toString(): String = value
}