package com.chuckerteam.chucker.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.api.ExceptionCollector
import com.chuckerteam.chucker.internal.data.entity.ThrowableType
import kotlinx.android.synthetic.main.activity_main_sample.*
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private lateinit var client: HttpBinClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sample)
        client = HttpBinClient(applicationContext)

        do_http.setOnClickListener { client.doHttpActivity() }
        trigger_exception.setOnClickListener { client.recordException() }

        with(launch_chucker_directly) {
            visibility = if (Chucker.isOp) View.VISIBLE else View.GONE
            setOnClickListener { launchChuckerDirectly() }
        }


    }

    private fun launchChuckerDirectly() {
        // Optionally launch Chucker directly from your own app UI
        startActivity(Chucker.getLaunchIntent(this, Chucker.SCREEN_HTTP))
    }

    fun crash(view: View) {
       // ExceptionCollector.logThrowable(ThrowableType.TAG_CRASH,RuntimeException("crash......"))
        var i = 4/0;
    }
    fun block(view: View) {
        ExceptionCollector.logThrowable(ThrowableType.TAG_block,RuntimeException("block......"))
    }
    fun leak(view: View) {
        ExceptionCollector.logThrowable(ThrowableType.TAG_leak,RuntimeException("leak......"))
        ExceptionCollector.logThrowable(ThrowableType.TAG_normal,RuntimeException("normal......"))
    }
}
