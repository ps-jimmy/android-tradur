package io.husaynhakeem.tradur

import android.util.Log
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*


internal object TextTranslator {

    private val TAG = TextTranslator::class.java.simpleName

    private val translateOptionsBuilder: TranslateOptions.Builder
            by lazy { TranslateOptions.newBuilder().setApiKey(Tradur.apiKey) }

    fun translate(text: String, onStart: () -> Unit, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        async(UI) {
            onStart()
            val targetLanguage = Locale.getDefault().language
            try {
                val translatedText = text.translateTo(targetLanguage)
                onSuccess(translatedText)
            } catch (e: Exception) {
                onFailure()
                onTranslationError(text, targetLanguage, e)
            }
        }
    }

    @Throws(Exception::class)
    private suspend fun String.translateTo(targetLanguage: String): String {
        return bg {
            translateOptionsBuilder.setTargetLanguage(targetLanguage)
                    .build()
                    .service
                    .translate(this@translateTo, Translate.TranslateOption.targetLanguage(targetLanguage))
        }.await().translatedText
    }

    private fun onTranslationError(text: String, targetLanguage: String, e: Exception) {
        Log.e(TAG, "Error translating '$text' into $targetLanguage: ${e.message}")
        e.printStackTrace()
    }
}
