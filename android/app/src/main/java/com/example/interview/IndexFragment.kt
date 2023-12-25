package com.example.interview

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.interview.base.BaseFragment
import org.json.JSONArray
import org.json.JSONObject


class PageAFragment(override val mLayoutResId: Int = R.layout.fragment_page_a) : BaseFragment() {
    private val countryLangMap = mapOf(
        "美國" to "en",
        "日本" to "ja",
        "韓國" to "ko",
        "台灣" to "zh-tw",
    )

    private lateinit var titleTextView: TextView

    private fun showDialog(view: View) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_menu_layout)

        val linearLayout = dialog.findViewById<LinearLayout>(R.id.popup_menu_layout)
        countryLangMap.keys.forEach { country ->
            val button = Button(context).apply {
                text = country
                setOnClickListener {
                    val langCode = countryLangMap[country] ?: "zh-tw"
                    val newUrl = "https://www.travel.taipei/open-api/zh-tw/Events/News?page=1&lang=$langCode"
                    loadDataFromUrl(newUrl, view)
                    dialog.dismiss()
                }
            }
            linearLayout.addView(button)
        }

        dialog.setCancelable(true)
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        updateTitle("Tu")
        updateSettingsButtonVisibility(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        // Find TextView in the layout
        titleTextView = view!!.findViewById(R.id.titleTextView)

        updateSettingsButtonVisibility2 {
            showDialog(view)
        }
        
        val url = "https://www.travel.taipei/open-api/zh-tw/Events/News?page=1&lang=zh-tw"

        loadDataFromUrl(url, view)

        return view
    }

    private fun loadDataFromUrl(url: String, view: View) {
        val requestQueue = Volley.newRequestQueue(requireContext())

        val headers = HashMap<String, String>()
        headers["Accept"] = "application/json"

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                handleData(response, view)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun handleData(response: JSONObject, view: View) {
        Log.d("InterView","How many :${response.length()}")
        Log.d("InterView","Total > ${response["total"]}")
        val dataArray2 = JSONArray(response["data"].toString())
        Log.d("InterView","data.length > ${dataArray2.length()}")
        val dataArray = response.optJSONArray("data")
        val dataList = mutableListOf<DataModel>()
        if (dataArray != null) {
            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val title = item.optString("title")
                val description = item.optString("description")
                val url = item.optString("url")
                // Create a DataModel and add it to the list
                val dataModel = DataModel(title, description,url)
                dataList.add(dataModel)
            }
        }
        Log.d("InterView","dataList.length > ${dataList.size}")

        val rvAdapter = RecyclerViewAdapter(
            object : RecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(dataModel: DataModel) {
                    Log.d(sTAG,"clicked item -${dataModel.title}")
                    val fragment = WebSiteFragment()
                    fragment.arguments = Bundle().apply {
                        this.putString("url",dataModel.url)
                    }
                    goToNext(fragment)
                }
            }
        )
        rvAdapter.setDataList(dataList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

}

data class DataModel(val title: String, val description: String, val url: String)

class RecyclerViewAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(dataModel: DataModel)
    }

    private var dataList = mutableListOf<DataModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(newDataList: List<DataModel>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.your_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d(sTAG,"> ${dataList.size}")
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position],itemClickListener)
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(dataModel: DataModel, clickListener: RecyclerViewAdapter.OnItemClickListener) {
        itemView.findViewById<TextView>(R.id.itemTitleTextView).text = dataModel.title
        itemView.findViewById<TextView>(R.id.itemDescriptionTextView).text = dataModel.description
        itemView.setOnClickListener { clickListener.onItemClick(dataModel) }
    }
}