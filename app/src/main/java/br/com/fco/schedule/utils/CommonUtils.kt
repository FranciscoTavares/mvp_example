package br.com.fco.schedule.utils

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import br.com.fco.schedule.R
import com.google.android.material.button.MaterialButton


fun Activity.initActivity(activity: Activity) {
    startActivity(
        Intent(this, activity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    )
}

/**
 * startButtonProgress
 *
 * When executed, it disables the button and starts an indeterminate progress bar.
 *
 * @param progressBar Progress bar that will start
 * @param button MaterialButton that will be disabled
 */
fun startButtonProgress(progressBar: ProgressBar?, button: MaterialButton?) {
    progressBar?.visibility = View.VISIBLE
    progressBar?.isIndeterminate = true
    button?.isEnabled = false
    button?.text = ""
}

/**
 * stopButtonProgress
 *
 * When executed, enables the button, and stops the progress bar.
 *
 * @param progressBar Progress bar that will stop
 * @param button MaterialButton that will be enabled
 * @param buttonText Button text
 */
fun stopButtonProgress(progressBar: ProgressBar?, button: MaterialButton?, buttonText: String?) {
    progressBar?.visibility = View.INVISIBLE
    progressBar?.isIndeterminate = false
    button?.isEnabled = true
    button?.text = buttonText
}
