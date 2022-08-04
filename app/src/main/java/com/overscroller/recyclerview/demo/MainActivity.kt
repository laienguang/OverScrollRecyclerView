package com.overscroller.recyclerview.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overscroller.recyclerview.demo.databinding.ActivityMainBinding
import com.overscroller.recyclerview.demo.databinding.VerticalListItemBinding


data class DemoItem(@ColorRes val color: Int)

class DemoListAdapter(val list: List<DemoItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cacheView: View? = convertView
        if (cacheView == null) {
            val itemBinding = VerticalListItemBinding.inflate(layoutInflater, parent, false)
            cacheView = itemBinding.root
            cacheView.tag = itemBinding
        }
        val itemBinding = cacheView.tag as VerticalListItemBinding
        val item = getItem(position) as DemoItem
        itemBinding.root.setBackgroundColor(ContextCompat.getColor(parent.context, item.color))
        itemBinding.colorName.text= parent.resources.getResourceEntryName(item.color);
        return cacheView
    }

}

class DemoViewHolder(view: View, val binding: VerticalListItemBinding): RecyclerView.ViewHolder(view)

class DemoAdapter(val list: List<DemoItem>):
    RecyclerView.Adapter<DemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val  itemBinding = VerticalListItemBinding.inflate(layoutInflater, parent, false)
        return DemoViewHolder(itemBinding.root, itemBinding)
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        val itemBinding = holder.binding
        val item = list[position]
        itemBinding.root.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, item.color))
        itemBinding.colorName.text= holder.itemView.resources.getResourceEntryName(item.color);
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MainActivity : AppCompatActivity() {
     lateinit var  binding: ActivityMainBinding

    val list = listOf(
        DemoItem(android.R.color.holo_purple),
        DemoItem(android.R.color.holo_blue_dark),
        DemoItem(android.R.color.holo_blue_light),
        DemoItem(android.R.color.darker_gray),
        DemoItem(android.R.color.holo_green_dark),
        DemoItem(android.R.color.holo_green_light),
        DemoItem(android.R.color.white),
        DemoItem(android.R.color.holo_orange_light),
        DemoItem(android.R.color.holo_orange_dark),
        DemoItem(android.R.color.holo_red_light),
        DemoItem(android.R.color.holo_red_dark),
        DemoItem(android.R.color.holo_purple),
        DemoItem(android.R.color.holo_blue_dark),
        DemoItem(android.R.color.holo_blue_light),
        DemoItem(android.R.color.darker_gray),
        DemoItem(android.R.color.holo_green_dark),
        DemoItem(android.R.color.holo_green_light),
        DemoItem(android.R.color.white),
        DemoItem(android.R.color.holo_orange_light),
        DemoItem(android.R.color.holo_orange_dark),
        DemoItem(android.R.color.holo_red_light),
        DemoItem(android.R.color.holo_red_dark)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.list.adapter = DemoListAdapter(list)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recycler.layoutManager = layoutManager
        val adapter = DemoAdapter(list)
        binding.recycler.adapter = adapter
    }
}