package com.example.md_parser.ui.start.view_model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.md_parser.parser.BlockElement
import com.example.md_parser.parser.InlineElement
import com.example.md_parser.parser.MarkdownParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.MalformedURLException

class StartViewModel : ViewModel() {
    private val markupData = MutableLiveData(
        """
        ### Lorem ipsum
        
        Lorem ipsum **dolor sit amet**, consectetur adipiscing elit. Aliquam dui nunc, ultricies
        et eleifend sed, mattis id mauris. Sed at gravida odio. Maecenas id diam nec arcu
        pellentesque aliquet. _Mauris aliquam_ at sem vitae pretium. Sed nec orci ut tellus
        tincidunt pellentesque sed in diam. Aenean maximus urna eget ligula volutpat, elementum
        maximus erat congue. Donec tempor metus tempus venenatis facilisis. Cras a ligula quis
        nunc posuere tristique at eget metus. Nunc dapibus ~~nisi~~ nec purus sagittis, a
        tincidunt ante tempus.
        
        ![cat](https://img-webcalypt.ru/thumbnail/large/images/meme-templates/DIwmh2oxu0T2StpwZWCZyNOfvRMRC6PF.jpg)
         
        Nullam lacus sem, bibendum et luctus non, commodo vel nulla. Vestibulum faucibus lorem
        sed felis egestas, nec gravida erat lacinia. Donec ut elementum erat.
        Praesent ut malesuada velit. Nam mattis metus quis porttitor venenatis. Sed
        sollicitudin, erat nec convallis volutpat, leo augue fringilla quam, ut commodo
        elit sapien a quam.
         
        | Activity | Time |
        | -------- | ---- |
        | Load laundry to washing machine | 2min |
        | Wait for washing machine to complete | 50min |
        | Hang the laundry out to dry | 10min |
        | Put laundry in closet | 2days |
        
        **Sed sodales, est vitae interdum convallis,** velit erat pulvinar risus, in bibendum eros
        ante ut neque. Sed blandit pharetra quam non aliquet. Sed vitae semper arcu.
        Pellentesque tincidunt vel est blandit porta. Sed laoreet quam vitae elementum iaculis.
        Integer euismod accumsan gravida. Quisque ut ipsum in nisi iaculis sodales. Curabitur
        pharetra arcu ex, malesuada blandit libero placerat ultrices. Duis id lacus vestibulum,
        congue lorem in, eleifend augue.
        """.trimIndent()
    )
    val markupLiveData: LiveData<String> = markupData

    private val bitmaps = MutableLiveData<MutableMap<String, Bitmap>>(mutableMapOf())
    val bitmapsLiveData: LiveData<MutableMap<String, Bitmap>> = bitmaps

    val markupElementsLiveData: LiveData<List<BlockElement>> = markupLiveData.map { markup ->
        getMarkupElements(markup)
    }

    fun setMarkup(newMarkup: String) {
        markupData.postValue(newMarkup)
    }

    private fun getMarkupElements(markup: String): List<BlockElement> {
        val elements = MarkdownParser.parse(markup)

        val images = elements
            .filterIsInstance<BlockElement.Paragraph>()
            .flatMap { it.children }
            .filterIsInstance<InlineElement.Image>()
        val imageUrls = images.map { it.url }.toSet()

        viewModelScope.launch(Dispatchers.IO) {
            for (imageUrl in imageUrls) {
                var url: URL
                try {
                    url = URL(imageUrl)
                } catch (e: MalformedURLException) {
                    Log.e("ImageLoad", "Invalid image link ${imageUrl}")
                    continue
                }
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()

                try {
                    val instream: InputStream = BufferedInputStream(urlConnection.inputStream)
                    val bmp = BitmapFactory.decodeStream(instream)
                    instream.close()

                    val current = bitmaps.value
                    if (bmp != null) {
                        current?.set(imageUrl, bmp)
                        bitmaps.postValue(current)
                    }
                } catch (e: FileNotFoundException) {
                    Log.e("ImageLoad", "File not found - probably invalid link ${imageUrl}")
                } finally {
                    urlConnection.disconnect()
                }
            }
        }

        return elements
    }
}