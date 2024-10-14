package com.example.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    data class ColorData(val colorName: String, val colorHex: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rView)

        val colorList = arrayListOf(
            ColorData("RED", 0xFFFF0000.toInt()),
            ColorData("GREEN", 0xFF00FF00.toInt()),
            ColorData("BLUE", 0xFF0000FF.toInt()),
            ColorData("YELLOW", 0xFFFFFF00.toInt()),
            ColorData("CYAN", 0xFF00FFFF.toInt())
        )

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = Adapter(this, colorList) { colorName ->
            Toast.makeText(this, "IT'S $colorName", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    class Adapter(
        private val context: Context,
        private val list: ArrayList<ColorData>,
        private val onCellClick: (String) -> Unit
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val colorName: TextView = view.findViewById(R.id.textView)
            val colorView: View = view.findViewById(R.id.customView)

            fun bind(colorData: ColorData, onCellClick: (String) -> Unit) {
                colorName.text = colorData.colorName
                colorView.setBackgroundColor(colorData.colorHex)

                itemView.setOnClickListener {
                    onCellClick(colorData.colorName)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val colorData = list[position]
            holder.bind(colorData, onCellClick)
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}