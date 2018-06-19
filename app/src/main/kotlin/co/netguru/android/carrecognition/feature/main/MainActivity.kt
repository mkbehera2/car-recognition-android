package co.netguru.android.carrecognition.feature.main

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import co.netguru.android.carrecognition.R
import co.netguru.android.carrecognition.feature.camera.onRequestPermissionsResult
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.io.FileOutputStream

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun init(){
        sample_text.text = stringFromJNI()

        cars_detect.setOnClickListener {
            sample_text.text = findCarsJNI(
                    copyRawFileToSDCard("mmod_front_and_rear_end_vehicle_detector.dat"),
                    copyRawFileToSDCard("mmod_cars_test_image.bmp"))
        }
    }


    fun copyRawFileToSDCard(fileName: String): String {

        val rawId = resources.getIdentifier("raw/"+
                fileName.substring(0,fileName.lastIndexOf(".")), null, packageName)
        val input = resources.openRawResource(rawId)
        val pathSDCard = Environment.getExternalStorageDirectory().absolutePath
        val file = File(String.format("%s/%s",pathSDCard,fileName))
        val out = FileOutputStream(file)
        val buff = ByteArray(1024)
        try {
            do {
                val size = input.read(buff)
                if(size >0){
                    out.write(buff, 0, size)
                }
            } while (size > 0)

        } finally {
            input.close()
            out.close()
        }
        Log.d(MainActivity::class.java.simpleName, String.format("FileNamePath = %s", file.absolutePath))
        return file.absolutePath
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun findCarsJNI(modelPath: String,imagePath: String): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("dlib")
        }
    }
}