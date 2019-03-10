package com.example.memorygame.model

class Numbers {

    companion object {
        fun getNumbers(): List<Int> {
            val list = (1..9).toList()
            return list.shuffled()
        }
    }
}