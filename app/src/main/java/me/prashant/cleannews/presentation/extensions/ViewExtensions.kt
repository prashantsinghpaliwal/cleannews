package me.prashant.cleannews.presentation.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun EditText.addTextChangedListenerDebounce(
    delayMillis: Long = 300,
    scope: CoroutineScope,
    callback: (String) -> Unit,
): TextWatcher {
    var job: Job? = null

    return object : TextWatcher {
        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int,
        ) {
            val searchText = s.toString().trim()

            // Cancel the previous job if it's still active
            job?.cancel()

            // Launch a coroutine with debounce delay
            job =
                scope.launch {
                    delay(delayMillis)
                    if (s.toString().trim() == searchText) {
                        callback(searchText)
                    }
                }
        }

        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int,
        ) = Unit
    }.also { this.addTextChangedListener(it) }
}
