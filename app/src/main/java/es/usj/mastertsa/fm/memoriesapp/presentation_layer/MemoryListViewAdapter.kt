package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import es.usj.mastertsa.fm.memoriesapp.domain_layer.Memory
import es.usj.mastertsa.fm.memoriesapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MemoryListViewAdapter(context : Context, private val dataSource : ArrayList<Memory>)
    : BaseAdapter()
{
    private val inflater : LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val appContext : Context = context

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
        val rowView : View = inflater.inflate(
            R.layout.activity_memory_adapter, parent,
            false)

        val titleTextView : TextView = rowView.findViewById(R.id.tvTitle) as TextView
        val dateTextView : TextView = rowView.findViewById(R.id.tvDate) as TextView
        val categoryImageView : ImageView = rowView.findViewById(R.id.ivCategory) as ImageView

        val memory : Memory = getItem(position) as Memory

        titleTextView.text = memory.title
        dateTextView.text = SimpleDateFormat("dd/MM/yyyy",
            Locale.getDefault()).format(memory.date)

        Glide.with(appContext)
            .load(memory.category.getURL())
            .placeholder(R.drawable.no_image_available)
            .into(categoryImageView)

        return rowView
    }
}
