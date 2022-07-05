package com.chuckerteam.chucker.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.sample.databinding.ActivityMainSampleBinding
import com.hss01248.image.dataforphotoselet.ImgDataSeletor
import com.hss01248.network.body.meta.interceptor.BodyUtil2
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.devio.takephoto.wrap.TakeOnePhotoListener
import java.io.File
import java.io.IOException

private val interceptorTypeSelector = InterceptorTypeSelector()

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainSampleBinding

    private val client by lazy {
        createOkHttpClient(applicationContext, interceptorTypeSelector)
    }

    private val httpTasks by lazy {
        listOf(HttpBinHttpTask(client), DummyImageHttpTask(client), PostmanEchoHttpTask(client))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BodyUtil2.attachBaseContext(application)
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainSampleBinding.inflate(layoutInflater)

        with(mainBinding) {
            setContentView(root)
            doHttp.setOnClickListener {
                for (task in httpTasks) {
                    task.run()
                }
            }
            picFileDoHttp!!.setOnClickListener {
                pickFileToUpload()
            }

            launchChuckerDirectly.visibility = if (Chucker.isOp) View.VISIBLE else View.GONE
            launchChuckerDirectly.setOnClickListener { launchChuckerDirectly() }

            interceptorTypeLabel.movementMethod = LinkMovementMethod.getInstance()
            useApplicationInterceptor.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    interceptorTypeSelector.value = InterceptorType.APPLICATION
                }
            }
            useNetworkInterceptor.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    interceptorTypeSelector.value = InterceptorType.NETWORK
                }
            }
        }

     /*   StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )*/
    }

    private fun pickFileToUpload() {

        ImgDataSeletor.startPickOneWitchDialog(this, object : TakeOnePhotoListener {
            override fun onSuccess(path: String) {
                upload(path)
            }

            override fun onFail(path: String, msg: String) {}
            override fun onCancel() {}
        })
    }

    private fun upload(path: String) {
        var mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(path.substring(path.lastIndexOf(".")+1));
        val requestBody = RequestBody.create(mimetype!!.toMediaType(), File(path))
        val reqeust = Request.Builder()
            .url("https://httpbin.org/post").post(requestBody).build()
        client.newCall(reqeust).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun launchChuckerDirectly() {
        // Optionally launch Chucker directly from your own app UI
        startActivity(Chucker.getLaunchIntent(this))
    }
}
