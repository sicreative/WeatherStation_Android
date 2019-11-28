package sc.azsphere.weatherstation

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread




class MainActivity : AppCompatActivity(),JsonCallBack<String>,ConnectivityManager.OnNetworkActiveListener,
    SensorFragment.OnListFragmentInteractionListener {
    companion object {


        const val READ_SENSOR_TIME = 5000
    }

    override var jsonReading: Boolean = false


    lateinit var jsonCallBack:JsonCallBack<String>

    lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var sensorList: MutableList<SensorItem>

    private lateinit var dataViewModel:DataViewModel



    fun getDataViewModel():DataViewModel{
        return dataViewModel
    }


    override fun onNetworkActive() {


            runBlocking { startHttpReading() }




    }

    fun CoroutineScope.waitNextReading() = launch{
        delay(READ_SENSOR_TIME.toLong())
        startHttpReading()
    }

    override fun startHttpReading() {
        val url = URL("http://192.168.1.134:8080/mobile.json")
        val urlConnection = url.openConnection() as HttpURLConnection


        val readString  = StringBuffer()

        try{
            val instream = BufferedInputStream(urlConnection.getInputStream())
            val reader = instream.bufferedReader(charset = Charsets.UTF_8)

            reader.useLines { readString.append(it.iterator().forEach { readString.append(it) }) }

            jsonCallBack.jsonDecording(readString.toString())

        }finally {
            urlConnection.disconnect()
            jsonReading = false

           runBlocking {  waitNextReading() }

        }
    }




    override fun jsonDecording(result: String?) {
        if (result==null)
            return
        val json = JSONObject(result)

        sensorList.clear()


        sensorList.add(SensorItem("temperature",(json["temperature"] as Double).toString(),"℃"))
        sensorList.add(SensorItem("humidity",(json["humidity"] as Double).toString(),"%"))
        sensorList.add(SensorItem("pressure",(json["pressure"] as Double).toString(),"hPa"))
        sensorList.add(SensorItem("altitude",(json["altitude"] as Double).toString(),"m"))
        sensorList.add(SensorItem("pm1",(json["pm1"] as Int).toString(),"μg/㎥"))
        sensorList.add(SensorItem("pm25",(json["pm25"] as Int).toString(),"μg/㎥"))
        sensorList.add(SensorItem("pm10",(json["pm10"] as Int).toString(),"μg/㎥"))
        sensorList.add(SensorItem("iaq",(json["iaq"] as Int).toString(),""))
        sensorList.add(SensorItem("eco2",(json["eco2"] as Int).toString(),"ppm"))
        sensorList.add(SensorItem("evoc",(json["evoc"] as Int).toString(),"ppm"))
        sensorList.add(SensorItem("light",(json["light"] as Double).toString(),"lux"))
        sensorList.add(SensorItem("accuracy",(json["accuracy"] as Int).toString(),""))
        sensorList.add(SensorItem("gas",(json["gas"] as Int).toString(),"ohm"))
        sensorList.add(SensorItem("fan",(json["fan"] as Int).toString(),"mV"))




        val data = Data(sensors = sensorList,timestamp = json["time"] as Int )



        dataViewModel.insert(data)





        finishJsonParsing()

    }



    override fun finishJsonParsing() {
        runOnUiThread() {
            viewAdapter.notifyDataSetChanged()
        }
    }



    override fun onListFragmentInteraction(item: SensorItem?) {


        var index = 0;
        for (i in 0..sensorList.count()-1){
            if (sensorList[i].type == item!!.type) {
                index = i
                break
            }

        }




        var detailfragment = DataFragment.newInstance(item!!.type,index)

       val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,detailfragment)
        transaction.addToBackStack(null)
        transaction.commit()


      //  startActivityFromFragment(sensorfragment,intent,0)
        //startActivity(intent)

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorList = mutableListOf()
        viewAdapter = SensorRecyclerViewAdapter(sensorList,this)
        viewManager = LinearLayoutManager(this)

        setContentView(R.layout.activity_main)


        dataViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)).get(DataViewModel::class.java)


        val fragment = SensorFragment.newInstance(0)



        supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()

        findViewById<Button>(R.id.clearbutton).setOnClickListener{
           dataViewModel.deleteall()

        }




    }


    override fun onResume() {
        super.onResume()

        startGetJsonData()



    }

    fun startGetJsonData(){
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        jsonCallBack = this
        if (connectivityManager.allNetworks.isNotEmpty())
            thread(start=true){onNetworkActive()}
        else
            connectivityManager.addDefaultNetworkActiveListener(this)



    }
}
