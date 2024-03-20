package pl.wsei.pam.pl.lab02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import pl.wsei.pam.lab01.R
import pl.wsei.pam.pl.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab02)
        var favGrid: GridLayout = findViewById(R.id.favorites_grid)

        for(i in 0 until favGrid.childCount)
        {
            val view = favGrid.getChildAt(i)
            if(view is Button)
            {
                view.setOnClickListener {
                    val size = view.text.toString()
                    setBoard(size)
                }

            }
        }
    }
        private fun setBoard(size: String){
            val rows: Int = Character.getNumericValue(size[0])
            val cols: Int = Character.getNumericValue(size[4])
            Toast.makeText(this,"Selected $rows x $cols", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Lab03Activity::class.java)
            intent.putExtra("columns", cols)
            intent.putExtra("rows", rows)
            startActivity(intent)
        }



}