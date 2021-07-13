package com.example.mysize

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.mysize.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var dataStore: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val NECK = "NECK"
        const val WAIST = "WAIST"
        const val SLEEVE = "SLEEVE"
        const val INSEAM = "INSEAM"
        const val NAME = "NAME"
        const val WEIGHT = "WEIGHT"
        const val HEIGHT = "HEIGHT"
        const val BMI = "BMI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ビューバインディングの設定
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 背景にフォ-カスが当たっている時はキーボードを隠す
        binding.mainLayout.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                InputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        // "DataStore"という名前でインスタンスを生成
        //dataStore = getSharedPreferences("DataStore", MODE_PRIVATE)
        dataStore = PreferenceManager.getDefaultSharedPreferences(this)

        // SharedPreferencesに保存されているデータを表示する
        binding.neck.setText(dataStore.getString(NECK, null))
        binding.waist.setText(dataStore.getString(WAIST, null))
        binding.sleeve.setText(dataStore.getString(SLEEVE, null))
        binding.inseam.setText(dataStore.getString(INSEAM, null))
        binding.editName.setText(dataStore.getString(NAME, null))
        binding.editWeight.setText(dataStore.getString(WEIGHT, null))
        binding.editHeight.setText(dataStore.getString(HEIGHT, null))
        binding.inputBmi.text = dataStore.getString(BMI, getString(R.string.culBmi))

        binding.saveButton.setOnClickListener {
            onSaveTapped()
            showToast()
        }
    }

    // 保存ボタン押下時の処理に関する関数
    private fun onSaveTapped() {
        val weight: Double? = binding.editWeight.text.toString().toDoubleOrNull()
        val height: Double? = binding.editHeight.text.toString().toDoubleOrNull()
        val bmi: Double?

        if (weight == null || height == null) {
            when {
                weight == null && height != null -> {
                    binding.inputBmi.setText(R.string.culBmiNoWeight)
                    dataStore.edit { putString(BMI, getString(R.string.culBmiNoWeight)) }
                }
                height == null && weight != null -> {
                    binding.inputBmi.setText(R.string.culBmiNoHeight)
                    dataStore.edit { putString(BMI, getString(R.string.culBmiNoHeight)) }
                }
                else -> {
                    binding.inputBmi.setText(R.string.culBmi)
                    dataStore.edit { putString(BMI, getString(R.string.culBmi)) }
                }
            }
        } else {
            bmi = (((weight.toDouble() / (height.toDouble() / 100).pow(2.0))))
            binding.inputBmi.text = String.format("%.1f", bmi)
            dataStore.edit { putString(BMI, String.format("%.1f", bmi)) }
        }

        dataStore.edit {
            putString(NECK, binding.neck.text.toString())
            putString(WAIST, binding.waist.text.toString())
            putString(SLEEVE, binding.sleeve.text.toString())
            putString(INSEAM, binding.inseam.text.toString())
            putString(NAME, binding.editName.text.toString())
            putString(WEIGHT, binding.editWeight.text.toString())
            putString(HEIGHT, binding.editHeight.text.toString())
        }
    }

    private fun showToast() {
        Toast.makeText(this, R.string.saveToast, Toast.LENGTH_SHORT).show()
    }

    // 背景がタップされた際、強制的に背景にフォ-カスを当てる関数
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        binding.mainLayout.requestFocus()
        return super.dispatchTouchEvent(ev)
    }

    /*
    // Viewがタッチされた際に呼ばれる関数
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // InputMethodManager をキャストしながら取得
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // エルビス演算子でViewを取得できなければ return false
        // focusViewには入力しようとしているEditTextが取得される
        val focusView = currentFocus ?: return false

        /* キーボードを閉じる
         *　HIDE_NOT_ALWAYS: ソフト入力ウィンドウを通常は非表示にすべきであることを示す
         */
        inputMethodManager.hideSoftInputFromWindow(
            focusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        return false
    }
     */
}
