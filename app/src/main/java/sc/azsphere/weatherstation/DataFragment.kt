package sc.azsphere.weatherstation


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DataFragment() :  Fragment(){

    var name:String? = null
    var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            name = it.getString(ARG_NAME)
            index = it.getInt(ARG_INDEX)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_data, container, false)



        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = DataListAdapter(context,index)



            }
        }


        (context as MainActivity).getDataViewModel().allData.observe(this, Observer { data ->
            // Update the cached copy of the words in the adapter.
            data?.let { ((view as RecyclerView).adapter as DataListAdapter).setData(it) }
        })

        var id = resources.getIdentifier(name,"string", (context as MainActivity).packageName)

        (activity as AppCompatActivity).supportActionBar!!.title = if (id==0) name else getString(id)


        return view
    }


    companion object {


        const val ARG_NAME = "name"
        const val ARG_INDEX = "index"


        @JvmStatic
        fun newInstance(name: String,index:Int) =
            DataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putInt(ARG_INDEX,index)
                }
            }
    }



    override fun onDetach() {
        super.onDetach()
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.app_name)
    }

    /*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        name = intent.getStringExtra(applicationContext.packageName+MainActivity.EXTRA_DETAIL_SENSOR_NAME)
        index = intent.getIntExtra(applicationContext.packageName+MainActivity.EXTRA_DETAIL_SENSOR_INDEX,0)

        val id = resources.getIdentifier(name,"string",packageName)
        title = if (id==0) name else getString(id)



        val recyclerView = findViewById<RecyclerView>(R.id.datarecyclerview)
        val adapter = DataListAdapter(this,index)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        dataViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application)).get(DataViewModel::class.java)



        dataViewModel.allData.observe(this, Observer { data ->
            // Update the cached copy of the words in the adapter.
            data?.let { adapter.setData(it) }
        })

        //dataViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application)).get(DataViewModel::class.java)
    }*/
}
