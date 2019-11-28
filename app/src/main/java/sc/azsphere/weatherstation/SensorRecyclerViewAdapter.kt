package sc.azsphere.weatherstation

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import sc.azsphere.weatherstation.SensorFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_sensor.view.*

/**
 * [RecyclerView.Adapter] that can display a [SensorItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class SensorRecyclerViewAdapter(
    private val mValues: List<SensorItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<SensorRecyclerViewAdapter.ViewHolder>() {

    private lateinit var packagename: String

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as SensorItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        packagename = parent.context.packageName
        //val test = parent.context.getString(parent.context.resources.getIdentifier("iaq","string",packagename))
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_sensor, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        var id = holder.mView.resources.getIdentifier(item.type,"string",packagename)


        holder.mNameView.text = if (id==0) item.type else holder.mView.resources.getString(id)

        id = holder.mView.resources.getIdentifier(item.type+"_icon","string",packagename)
        holder.mNameIconView.text =  if (id==0) "" else holder.mView.resources.getString(id)

        holder.mContentView.text = item.value
        holder.mUnitView.text = item.unit


        with(holder.mView) {
            tag = item

            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mContentView: TextView = mView.content
        val mUnitView: TextView = mView.unit
        val mNameIconView: TextView = mView.name_icon

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
