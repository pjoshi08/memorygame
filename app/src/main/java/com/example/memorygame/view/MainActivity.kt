package com.example.memorygame.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.memorygame.R
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.viewmodel.MainVM

class MainActivity : AppCompatActivity() {
    private lateinit var vm: MainVM
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        vm = ViewModelProviders.of(this).get(MainVM::class.java)
        binding.viewmodel = vm
        vm.initView(binding)

        setObservers(binding)
    }

    private fun setObservers(binding: ActivityMainBinding) {
        vm.getMillisSec().observe(this, Observer {
            it?.let {
                toast = Toast.makeText(this, "${it/1000}", Toast.LENGTH_SHORT)
                toast.show()
            }
        })

        vm.getNumberShown().observe(this, Observer {
            it?.let {
                binding.selectedItem.text = it
            }
        })

        vm.getGameEvent().observe(this, Observer {
            it?.let { gameEvent ->
                when (gameEvent) {
                    MainVM.GameEvent.GAME_OVER -> {
                        toast.cancel()
                        toast = Toast.makeText(this, resources.getString(R.string.game_over), Toast.LENGTH_SHORT)
                        toast.show()
                        binding.gameRestartButton.visibility = View.VISIBLE
                    }

                    MainVM.GameEvent.GAME_RESTART -> {
                        vm.initView(binding)
                        binding.gameRestartButton.visibility = View.GONE
                        binding.selectedItem.text = ""
                    }

                    MainVM.GameEvent.SHOW_ERROR -> {
                        toast.cancel()
                        toast = Toast.makeText(this, resources.getString(R.string.error), Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
            }
        })
    }
}
