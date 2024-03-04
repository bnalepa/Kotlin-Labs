package pl.wsei.pam.lab01

import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toolbar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    lateinit var mLayout: LinearLayout
    lateinit var mProgress: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLayout = findViewById(R.id.main)
        for (i in 1..6) {
//            val linearLayout = LinearLayout(this)
//            linearLayout.layoutParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//            linearLayout.orientation = LinearLayout.HORIZONTAL
            val checkBox = CheckBox(this)
            checkBox.text = "Zadanie ${i}"
            checkBox.isEnabled = false
            mLayout.addView(checkBox)
//            linearLayout.addView(checkBox)
//            linearLayout.addView(Switch(this))
//            mLayout.addView(linearLayout)
        }
        mProgress = ProgressBar(this, null, androidx.appcompat.R.attr.progressBarStyle, androidx.appcompat.R.style.Widget_AppCompat_ProgressBar_Horizontal)
        mLayout.addView(mProgress)
        mProgress.progress = 67

        if (
            task11(4, 6) in 0.666665..0.666667 &&
            task11(7, -6) in -1.1666667..-1.1666665
        ) {
            (mLayout.get(0) as? CheckBox)?.isChecked = true
        }
        if (
            task12(7U, 6U) == "7 + 6 = 13" &&
            task12(12U, 15U) == "12 + 15 = 27"
        ) {
            (mLayout.get(1) as? CheckBox)?.isChecked = true
        }
        if (
            task13(0.0, 5.4f) && !task13(7.0, 5.4f) &&
            !task13(-6.0, -1.0f) && task13(6.0, 9.1f) &&
            !task13(6.0, -1.0f) && task13(1.0, 1.1f)
        ) {
            (mLayout.get(2) as? CheckBox)?.isChecked = true
        }
        if (
            task14(-2, 5) == "-2 + 5 = -3" &&
            task14(-2, -5) == "-2 - 5 = -7"
        ) {
            (mLayout.get(3) as? CheckBox)?.isChecked = true
        }
        if (
            task15("DOBRY") == 4 &&
            task15("barDzo dobry") == 5 &&
            task15("doStateczny") == 3 &&
            task15("Dopuszczający") == 2 &&
            task15("NIEDOSTATECZNY") == 1 &&
            task15("XYZ") == -1
        ){
            (mLayout.get(4) as? CheckBox)?.isChecked = true
        }
        if (task16(
                    mapOf("A" to 2U, "B" to 4U, "C" to 3U),
                    mapOf("A" to 1U, "B" to 2U)
                ) == 2U
            &&
            task16(
                    mapOf("A" to 2U, "B" to 4U, "C" to 3U),
                    mapOf("F" to 1U, "G" to 2U)
                ) == 0U
            &&
            task16(
                    mapOf("A" to 23U, "B" to 47U, "C" to 30U),
                    mapOf("A" to 1U, "B" to 2U, "C" to 4U)
                ) == 7U
            ) {
            (mLayout.get(5) as? CheckBox)?.isChecked = true
        }
    }

    // Wykonaj dzielenie niecałkowite parametru a przez b
    // Wynik zwróć po instrukcji return
    private fun task11(a: Int, b: Int): Double {
        return 0.0
    }

    // Zdefiniuj funkcję, która zwraca łańcuch dla argumentów bez znaku (zawsze dodatnie) wg schematu
    // <a> + <b> = <a + b>
    // np. dla parametrów a = 2 i b = 3
    // 2 + 3 = 5
    private fun task12(a: UInt, b: UInt): String {
        return ""
    }

    // Zdefiniu funkcję, która zwraca wartość logiczną, jeśli parametr `a` jest nieujemny i mniejszy od `b`
    fun task13(a: Double, b: Float): Boolean {
        return false
    }

    // Zdefiniuj funkcję, która zwraca łańcuch dla argumentów całkowitych ze znakiem wg schematu
    // <a> + <b> = <a + b>
    // np. dla parametrów a = 2 i b = 3
    // 2 + 3 = 5
    // jeśli b jest ujemne należy zmienić znak '+' na '-'
    // np. dla a = -2 i b = -5
    //-2 - 5 = -7
    // Wskazówki:
    // Math.abs(a) - zwraca wartość bezwględną
    fun task14(a: Int, b: Int): String {
        return ""
    }

    // Zdefiniuj funkcję zwracającą ocenę jako liczbę całkowitą na podstawie łańcucha z opisem słownym oceny.
    // Możliwe przypadki:
    // bardzo dobry 	5
    // dobry 			4
    // dostateczny 		3
    // dopuszczający 	2
    // niedostateczny	1
    // Funkcja nie powinna być wrażliwa na wielkość znaków np. Dobry, DORBRY czy DoBrY to ta sama ocena
    // Wystąpienie innego łańcucha w degree funkcja zwraca wartość -1
    fun task15(degree: String): Int {
        return 0
    }

    // Zdefiniuj funkcję zwracającą liczbę możliwych do zbudowania egzemplarzy, które składają się z elementów umieszczonych w asset
    // Zmienna store jest magazynem wszystkich elementów
    // Przykład
    // store = mapOf("A" to 3, "B" to 4, "C" to 2)
    // asset = mapOf("A" to 1, "B" to 2)
    // var items = task16(store, asset)
    // println(items)	=> 2 ponieważ do zbudowania jednego egzemplarza potrzebne są 2 elementy "B" i jeden "A", a w magazynie mamy 2 "A" i 4 "B",
    // czyli do zbudowania trzeciego egzemplarza zabraknie elementów typu "B"
    fun task16(store: Map<String, UInt>, asset: Map<String, UInt>): UInt {
        return UInt.MAX_VALUE
    }
}