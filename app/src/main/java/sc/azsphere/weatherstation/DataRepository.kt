package sc.azsphere.weatherstation

import androidx.lifecycle.LiveData

class DataRepository (private val dataDao: DataDao) {

    val alldata: LiveData<List<Data>> = dataDao.getSensors()

    suspend fun insert(data:Data) {
        dataDao.insert(data)
    }

    suspend fun deleteall() {
        dataDao.deleteAll()
    }
}