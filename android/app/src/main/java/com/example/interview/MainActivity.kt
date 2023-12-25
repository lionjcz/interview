package com.example.interview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.interview.databinding.ActivityMainBinding

const val sTAG = "InterView"



class MainActivity: AppCompatActivity(){

    val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel.textData.observe(this) { data ->
            binding.mainBarTitle.text = data
        }

        val pageAFragment = PageAFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, pageAFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val settingsItem = menu.findItem(R.id.action_settings)
        viewModel.settingsButtonVisible.observe(this) { isVisible ->
            settingsItem.isVisible = isVisible
        }
        viewModel.settingsButtonIcon.observe(this) { iconResId ->
            settingsItem.icon = ContextCompat.getDrawable(this, iconResId)
        }

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

    fun goToNext(fragment: WebSiteFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}

class MainActivityViewModel : ViewModel() {
    val textData = MutableLiveData<String>()
    val settingsButtonVisible = MutableLiveData(true)
    val settingsButtonIcon = MutableLiveData(R.drawable.ic_settings)

    fun updateTitle(newData: String) {
        textData.value = newData
    }

    fun updateSettingsButtonVisibility(isVisible: Boolean) {
        settingsButtonVisible.value = isVisible
    }

    val actionOnSettings: MutableLiveData<() -> Unit> = MutableLiveData()

    fun setActionOnSettings(action: () -> Unit) {
        actionOnSettings.value = action
    }

}

