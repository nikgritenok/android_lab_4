package com.example.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // Класс данных ColorData
    data class ColorData(val colorName: String, val colorHex: Int)

    // Адаптер для RecyclerView
    class Adapter(private val context: Context, private val list: ArrayList<ColorData>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        // ViewHolder для управления элементами списка
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val colorName: TextView = view.findViewById(R.id.textView)
            val colorView: View = view.findViewById(R.id.customView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val colorData = list[position]
            holder.colorName.text = colorData.colorName
            holder.colorView.setBackgroundColor(colorData.colorHex)
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rView)

        // Создание списка объектов ColorData
        val colorList = arrayListOf(
            ColorData("RED", 0xFFFF0000.toInt()),
            ColorData("GREEN", 0xFF00FF00.toInt()),
            ColorData("BLUE", 0xFF0000FF.toInt()),
            ColorData("YELLOW", 0xFFFFFF00.toInt()),
            ColorData("CYAN", 0xFF00FFFF.toInt())
        )

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(this, colorList)

        // Обработка системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}