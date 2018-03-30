package br.com.caramelo.idwallteste.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChangeListener(onTextChange: (String) -> Unit) {
    val editText = this
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            onTextChange(editText.text.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    })
}