package sc.azsphere.weatherstation

import androidx.room.*


@Entity(tableName = "sensors_table")
@TypeConverters(DataConverter::class)
data class Data(@PrimaryKey @ColumnInfo(name = "timestamp") val timestamp: Int,

                @ColumnInfo(name="sensors") val sensors:List<SensorItem>)

