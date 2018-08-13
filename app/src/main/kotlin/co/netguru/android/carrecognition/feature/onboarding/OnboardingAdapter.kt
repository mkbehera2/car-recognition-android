package co.netguru.android.carrecognition.feature.onboarding

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

class OnboardingAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val PAGE_COUNT = 3
    var progressPhotoFragments: SparseArray<Fragment>

    init {
        progressPhotoFragments = SparseArray(PAGE_COUNT)
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingRecognizeFragment.newInstance()
            1 -> OnboardingRecognizeFragment.newInstance()
            2 -> OnboardingRecognizeFragment.newInstance()
            else -> OnboardingRecognizeFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        progressPhotoFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        progressPhotoFragments.remove(position)
    }
}