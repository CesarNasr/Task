package com.example.inmobilestask.ui

import android.net.sip.SipSession
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inmobilestask.R
import com.example.inmobilestask.data.models.Items
import com.example.inmobilestask.data.models.NetworkState
import kotlinx.android.synthetic.main.loader_item.view.*
import kotlinx.android.synthetic.main.repository_item.view.*


class RepoListAdapter(private var items: MutableList<Items>, val clickListener: (Items) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    // list consists of two items, 1st is the data item, second is the loader item

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Items) {
            itemView.apply {
                name.text = item.name
                fullname.text = item.full_name
                item_id.text = item.id.toString()

            }

        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.progress_circular.visibility = VISIBLE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            DataViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.repository_item,
                    parent,
                    false
                )
            )
        } else FooterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.loader_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            var item: Items = items[position]
            (holder as DataViewHolder).bind(items[position])
            holder.itemView.setOnClickListener { clickListener(item) }
        } else (holder as FooterViewHolder).bind()
    }


    override fun getItemViewType(position: Int): Int {
        return if (items[position].id== null) FOOTER_VIEW_TYPE else DATA_VIEW_TYPE
    }

    fun setList(items: List<Items>) {
        this.items.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    fun addLoadingItem() {
        val loadingItem = Items()

        this.items.add(loadingItem)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return items.size
    }


}

