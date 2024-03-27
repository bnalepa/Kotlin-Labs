package pl.wsei.pam.pl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import pl.wsei.pam.lab01.R
import pl.wsei.pam.pl.lab01.Lab01Activity
import pl.wsei.pam.pl.lab02.Lab02Activity
import pl.wsei.pam.pl.lab03.Lab03Activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }
    fun onClickMainBtnRunLab01(view: View){
        val intent = Intent(this, Lab01Activity::class.java)
        startActivity(intent)
    }
    fun onClickMainBtnRunLab02(view: View){
        val intent = Intent(this, Lab02Activity::class.java)
        startActivity(intent)
    }
    fun onClickMainBtnRunLab03(view: View){
        val intent = Intent(this, Lab03Activity::class.java)
        startActivity(intent)
    }
}