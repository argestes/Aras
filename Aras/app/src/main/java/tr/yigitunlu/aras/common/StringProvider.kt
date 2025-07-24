package tr.yigitunlu.aras.common

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes stringResId: Int): String
}
