package sc.azsphere.weatherstation;


import androidx.room.TypeConverter;
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

    companion object
    {
        @TypeConverter
        @JvmStatic
        fun ToStoreString(items:List<SensorItem>):String {
            return Gson().toJson(items)
         }

        @TypeConverter
        @JvmStatic
        fun ToSensorItems(gson:String):List<SensorItem> {



            return Gson().fromJson(gson,object:TypeToken<List<SensorItem>>(){}.type)

        }

    }

}
