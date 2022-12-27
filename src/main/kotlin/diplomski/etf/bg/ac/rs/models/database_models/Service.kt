package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: Int,
    val name: String,
    val icon: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Service

        if (id != other.id) return false
        if (name != other.name) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}