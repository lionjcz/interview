package com.example.interview

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.interview.base.BaseFragment
import org.json.JSONArray
import org.json.JSONObject


class PageAFragment(override val mLayoutResId: Int = R.layout.fragment_page_a) : BaseFragment() {


    private lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("Tu");
        updateSettingsButtonVisibility(true)
        updateSettingsButtonVisibility2 {
            showDialog()
        }

    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_menu_layout) // 使用相同的布局文件

        val linearLayout = dialog.findViewById<LinearLayout>(R.id.popup_menu_layout)
        val countries = listOf("美国", "日本", "韩国", "台湾")

        countries.forEach { country ->
            val button = Button(context).apply {
                text = country
                setOnClickListener {
                    // 处理点击事件
                    dialog.dismiss()
                }
            }
            linearLayout.addView(button)
        }

        dialog.setCancelable(true) // 点击外部区域时取消显示
        dialog.show()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        // Find TextView in the layout
        titleTextView = view!!.findViewById(R.id.titleTextView)

        // Load data from the specified URL
        val url = "https://www.travel.taipei/open-api/zh-tw/Events/News?page=1&lang=zh-tw"

        loadDataFromUrl(url, view)

        return view;
    }

    private fun loadDataFromUrl(url: String, view: View) {
        val requestQueue = Volley.newRequestQueue(requireContext())

        // Create a HashMap to store headers
        val headers = HashMap<String, String>()
        headers["Accept"] = "application/json"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Handle the response JSON data
                handleData(response,view)
            },
            Response.ErrorListener { error ->
                // Handle errors
                error.printStackTrace()
            }) {
            // Override the getHeaders method to set custom headers
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }



    private fun handleData(response: JSONObject, view: View) {
        Log.d("InterView","How many :${response.length()}")
        Log.d("InterView","Total > ${response["total"]}")
        var dataArray2 = JSONArray(response["data"].toString());
        Log.d("InterView","data.length > ${dataArray2.length()}")
        val dataArray = response.optJSONArray("data")
        val dataList = mutableListOf<DataModel>()
        if (dataArray != null) {
            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val title = item.optString("title")
                val description = item.optString("description")

                // Create a DataModel and add it to the list
                val dataModel = DataModel(title, description)
                dataList.add(dataModel)
            }
        }
        Log.d("InterView","dataList.length > ${dataList.size}")

        val yourAdapter = YourRecyclerViewAdapter(
            object : YourRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(dataModel: DataModel) {
                    // 在這裡處理點擊事件，例如跳轉到另一個 Fragment
                    val fragment = PageBFragment()
                    // 可以將 dataModel 作為參數傳遞給 PageBFragment
                    // 例如，使用 Bundle 或 ViewModel
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, fragment)
//                        .addToBackStack(null)
//                        .commit()
                }
            }
        )
        yourAdapter.setDataList(dataList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = yourAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

}

data class DataModel(val title: String, val description: String)

class YourRecyclerViewAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<YourViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(dataModel: DataModel)
    }

    private var dataList = mutableListOf<DataModel>()

    fun setDataList(newDataList: List<DataModel>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourViewHolder {
        // Inflate your item layout here
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.your_item_layout, parent, false)
        return YourViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d(sTAG,"> ${dataList.size}")
        return dataList.size
    }

    override fun onBindViewHolder(holder: YourViewHolder, position: Int) {
        // Bind your data to the ViewHolder here
        holder.bind(dataList[position],itemClickListener)
    }

}

class YourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    fun bind(dataModel: DataModel) {
//         itemView.findViewById<TextView>(R.id.itemTitleTextView).text = dataModel.title
//         itemView.findViewById<TextView>(R.id.itemDescriptionTextView).text = dataModel.description
//    }

    fun bind(dataModel: DataModel, clickListener: YourRecyclerViewAdapter.OnItemClickListener) {
        itemView.findViewById<TextView>(R.id.itemTitleTextView).text = dataModel.title
        itemView.findViewById<TextView>(R.id.itemDescriptionTextView).text = dataModel.description
        itemView.setOnClickListener { clickListener.onItemClick(dataModel) }
    }

}