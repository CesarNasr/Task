package com.example.inmobilestask.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.inmobilestask.R
import com.example.inmobilestask.data.models.Items
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*


private const val ARG_ITEM = "arg_item"


class DetailFragment : Fragment(), View.OnClickListener {
    private lateinit var item: Items
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            item = gson.fromJson<Items>(it.getString(ARG_ITEM), Items::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
    }

    private fun setViews(view: View) {
        view.back_btn.setOnClickListener(this)
        view.title.text = item.owner?.login
        view.body.text = item.owner?.id.toString()


        Picasso.get().load(item.owner?.avatar_url).placeholder(R.drawable.ic_photo_placeholder)
            .into(view.image)


    }

    companion object {
        @JvmStatic
        fun newInstance(item: Items) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val itemJson = gson.toJson(item)

                    putString(ARG_ITEM, itemJson)
                }
            }
    }

    override fun onClick(v: View?) {
        when (v) {
            back_btn -> {
                activity?.onBackPressed()
            }
        }
    }
}