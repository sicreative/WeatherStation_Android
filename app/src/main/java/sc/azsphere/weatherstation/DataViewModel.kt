package sc.azsphere.weatherstation

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch


class DataViewModel(application: Application) : AndroidViewModel(application){



    private val repository: DataRepository

    val allData: LiveData<List<Data>>



    init {

        val dataDao = DataDatabase.getDatabase(application,viewModelScope).dataDao()
        repository = DataRepository(dataDao)
        allData = repository.alldata

    }


    fun insert(data: Data) = viewModelScope.launch {
        repository.insert(data)
    }

    fun deleteall() = viewModelScope.launch {
        repository.deleteall()
    }
}