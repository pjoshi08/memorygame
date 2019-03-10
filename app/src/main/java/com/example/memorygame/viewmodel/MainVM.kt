package com.example.memorygame.viewmodel

import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.model.Numbers
import com.example.memorygame.util.SingleLiveEvent
import com.example.memorygame.view.GridAdapter

class MainVM: ViewModel(), GridAdapter.GridClickListener {

    private var millisSec: MutableLiveData<Long> = MutableLiveData()
    private var numberShown: MutableLiveData<String> = MutableLiveData()
    private var gameEvent: SingleLiveEvent<GameEvent> = SingleLiveEvent()

    fun initView(binding: ActivityMainBinding) {
        val context = binding.root.context

        val gridAdapter = GridAdapter(Numbers.getNumbers(), this)
        binding.adapter = gridAdapter
        val layoutManager = GridLayoutManager(context, 3)
        binding.grid.layoutManager = layoutManager

        object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                gridAdapter.hideItems()
                numberShown.value = gridAdapter.showRandom().toString()
            }

            override fun onTick(millisUntilFinished: Long) {
                millisSec.value = millisUntilFinished
            }
        }.start()
    }

    override fun showNumber(number: Int) {
        numberShown.value = number.toString()
    }

    override fun showError() {
        gameEvent.value = GameEvent.SHOW_ERROR
    }

    override fun gameOver() {
        gameEvent.value = GameEvent.GAME_OVER
    }

    fun gameRestart(view: View) {
        gameEvent.value = GameEvent.GAME_RESTART
    }

    fun getMillisSec(): MutableLiveData<Long> = millisSec
    fun getNumberShown(): MutableLiveData<String> = numberShown
    fun getGameEvent(): MutableLiveData<GameEvent> = gameEvent

    enum class GameEvent {
        GAME_OVER, GAME_RESTART, SHOW_ERROR
    }
}