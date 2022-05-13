package com.zzp.rubbish.bottomdrawer

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.github.heyalex.bottomdrawer.TranslationUpdater

class RotateHandleView(context: Context) : AppCompatImageView(context), TranslationUpdater {
    override fun updateTranslation(value: Float) {
        this.rotation = -180 * value
    }
}