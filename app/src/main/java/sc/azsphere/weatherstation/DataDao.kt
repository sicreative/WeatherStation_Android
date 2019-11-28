package sc.azsphere.weatherstation

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single


@Dao
interface  DataDao {

    @Query("SELECT * from sensors_table ORDER BY timestamp DESC")
    fun getSensors(): LiveData<List<Data>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insert(data: Data)

    @Query("DELETE FROM sensors_table")
    suspend fun deleteAll()

}