package co.netguru.android.carrecognition.feature.camera

import android.media.Image
import com.google.ar.core.HitResult
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView


interface CameraContract {
    interface View : MvpView {
        fun printResult(result: String)
        fun createAnchor(hitPoint: HitResult, text: String)
        fun acquireFrame(): Image?
    }
    interface Presenter: MvpPresenter<View> {
        fun processHitResult(hitPoint: HitResult?)
        fun frameUpdated()
    }
}