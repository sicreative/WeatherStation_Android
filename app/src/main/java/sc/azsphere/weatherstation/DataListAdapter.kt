package sc.azsphere.weatherstation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DataListAdapter internal constructor(
    context: Context,
    val dataIndex: Int
) : RecyclerView.Adapter<DataListAdapter.DataViewHolder>() {



    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var data = emptyList<Data>()


    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataItemView: TextView = itemView.findViewById(R.id.valueTextView)
        val timeView:TextView = itemView.findViewById(R.id.timeTextView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = inflater.inflate(R.layout.data_recycleview_item, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val current = data[position]

        holder.dataItemView.text = current.sensors[dataIndex].value





        holder.timeView.text = DateFormat.getDateTimeInstance().format(Date(current.timestamp.toLong()*1000))

    }

    internal fun setData(data: List<Data>) {
        this.data = data



        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size
}