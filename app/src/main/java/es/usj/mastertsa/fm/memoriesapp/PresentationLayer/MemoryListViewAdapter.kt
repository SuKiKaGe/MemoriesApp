package es.usj.mastertsa.fm.memoriesapp.PresentationLayer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import es.usj.mastertsa.fm.memoriesapp.DomainLayer.Memory
import es.usj.mastertsa.fm.memoriesapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MemoryListViewAdapter(context : Context, private val dataSource : ArrayList<Memory>) : BaseAdapter()
{
    private val inflater : LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount() : Int
    {
        return dataSource.size
    }

    override fun getItem(position : Int) : Any
    {
        return dataSource[position]
    }

    override fun getItemId(position : Int) : Long
    {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "DefaultLocale")
    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View
    {
        // Get view for row item
        val rowView = inflater.inflate(
            R.layout.activity_memory_adapter, parent,
            false)

        val titleTextView = rowView.findViewById(R.id.tvTitle) as TextView
        val dateTextView = rowView.findViewById(R.id.tvDate) as TextView
        val categoryImageView = rowView.findViewById(R.id.ivCategory) as ImageView

        val memory = getItem(position) as Memory

        titleTextView.text = memory.title
        dateTextView.text = SimpleDateFormat("dd/MM/yyyy",
            Locale.getDefault()).format(memory.date)

        val resourceId = inflater.context.resources.getIdentifier(
            memory.category.name.toLowerCase(), "drawable", inflater.context.packageName)

        categoryImageView.setImageResource(resourceId)

        return rowView
    }
}
