package com.example.interview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() , MainView{
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化Presenter
        presenter = MainPresenter(this, MainModel())

        // 設置按鈕點擊監聽器
        findViewById<Button>(R.id.btn1).setOnClickListener {
            presenter.loadData()
        }

    }

    override fun showData(data: String) {
        Toast.makeText(this, "showData > $data", Toast.LENGTH_SHORT).show()
    }

}

interface MainView {
    fun showData(data: String)

}


class MainPresenter(private val view: MainView, private val model: MainModel) {
    fun loadData() {
        model.fetchData { data ->
            view.showData(data)
        }
    }

}

class MainModel {
    fun getData(): String {
        return "Sample Data"
    }
    fun fetchData(callback: (String) -> Unit) {
        val data = "來自API的數據"
        callback(data)
    }
}
