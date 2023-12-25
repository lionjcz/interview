package com.example.interview.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.interview.MainActivity

abstract class BaseFragment : Fragment() {

    /**
     * layoutID
     */
    @get:LayoutRes
    abstract val mLayoutResId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(mLayoutResId, container, false)
    }

    fun updateTitle(sTitle: String) {
        val viewModel = (activity as? MainActivity)?.viewModel;
        viewModel?.updateTitle(sTitle);
    }

    fun updateSettingsButtonVisibility(b: Boolean) {
        val viewModel = (activity as? MainActivity)?.viewModel;
        viewModel?.updateSettingsButtonVisibility(b);
    }


    fun updateSettingsButtonVisibility2(function: () -> Unit) {
        val mainActivity = activity as? MainActivity
        mainActivity?.viewModel?.setActionOnSettings {
            function()
        }
    }

}