package com.example.memorygame.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.R
import com.example.memorygame.databinding.GridItemBinding
import kotlin.random.Random

class GridAdapter(private var numbers: List<Int>, private val listener: GridClickListener,
                  private var gameOver: Boolean = false) :
    RecyclerView.Adapter<GridAdapter.ViewHolder>() {
    private var selectedNumbers: MutableList<Int> = ArrayList()
    private var hideItems = false

    fun hideItems() {
        this.hideItems = true
        notifyDataSetChanged()
    }

    fun showRandom(): Int {
        val number = numbers[Random.nextInt(8)]
        selectedNumbers.add(number)

        return number
    }

    fun setList(list: List<Int>) {
        numbers = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: GridItemBinding = DataBindingUtil.inflate(inflater, R.layout.grid_item, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 9

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!hideItems) {
            holder.binding.number = numbers[position].toString()
        } else
            holder.binding.number = ""

        if (!gameOver) {
            holder.binding.item.setOnClickListener {
                val number = numbers[position]

                if (!selectedNumbers.contains(number) && selectedNumbers.size == 8) {
                    listener.showNumber(number)
                    listener.gameOver()
                    gameOver = true
                    hideItems = false
                    notifyDataSetChanged()
                } else if (selectedNumbers.contains(number))
                    listener.showError()
                else {
                    selectedNumbers.add(number)
                    listener.showNumber(number)
                }
            }
        } else {
            holder.binding.item.setOnClickListener(null)
        }
    }

    class ViewHolder(v: GridItemBinding): RecyclerView.ViewHolder(v.root) {
        val binding = v
    }

    interface GridClickListener {
        fun showNumber(number: Int)
        fun showError()
        fun gameOver()
    }
}