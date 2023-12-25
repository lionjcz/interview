package com.example.interview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.interview.databinding.ActivityMainBinding

const val sTAG = "InterView"



class MainActivity: AppCompatActivity(){

    val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainBar);
        supportActionBar?.setDisplayShowTitleEnabled(false);

        // 只在这里设置 LiveData 的观察
        viewModel.textData.observe(this, Observer { data ->
            binding.mainBarTitle.text = data
        })

        val pageAFragment = PageAFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, pageAFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // 可见性和图标更新
        val settingsItem = menu.findItem(R.id.action_settings)
        viewModel.settingsButtonVisible.observe(this, Observer { isVisible ->
            settingsItem.isVisible = isVisible
        })
        viewModel.settingsButtonIcon.observe(this, Observer { iconResId ->
            settingsItem.icon = ContextCompat.getDrawable(this, iconResId)
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                viewModel.actionOnSettings.value?.invoke() // 执行 lambda 函数
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}

class SharedViewModel : ViewModel() {
    val textData = MutableLiveData<String>()
    val settingsButtonVisible = MutableLiveData<Boolean>(true)
    val settingsButtonIcon = MutableLiveData<Int>(R.drawable.ic_settings) // 默认图标

    fun updateTitle(newData: String) {
        textData.value = newData
    }

    fun updateSettingsButtonVisibility(isVisible: Boolean) {
        settingsButtonVisible.value = isVisible
    }

    fun updateSettingsButtonIcon(iconResId: Int) {
        settingsButtonIcon.value = iconResId
    }

    fun someAction(function: () -> Unit) {
        function()
    }

    // 其他 LiveData
    val actionOnSettings: MutableLiveData<() -> Unit> = MutableLiveData()

    // 设置 lambda 函数
    fun setActionOnSettings(action: () -> Unit) {
        actionOnSettings.value = action
    }
}

