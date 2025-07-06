package com.example.md_parser.ui.start.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.italic
import androidx.core.text.strikeThrough
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.md_parser.R
import com.example.md_parser.databinding.FragmentStartViewerBinding
import com.example.md_parser.parser.BlockElement
import com.example.md_parser.parser.InlineElement
import com.example.md_parser.ui.start.view_model.StartViewerViewModel

class StartViewerFragment : Fragment() {

    companion object {
        fun newInstance() = StartViewerFragment()
    }

    private lateinit var binding: FragmentStartViewerBinding
    private val viewModel: StartViewerViewModel by viewModels()

    private lateinit var markupElements: List<BlockElement>
    private val images = mutableListOf<Pair<ImageView, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        markupElements = viewModel.getMarkupElements()
        viewModel.bitmapsLiveData.observe(this) { bitmaps -> updateBitmaps(bitmaps) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartViewerBinding.inflate(inflater, container, false)

        for (element in markupElements) {
            when (element) {
                is BlockElement.Header -> {
                    val t = TextView(context)
                    t.text = element.content
                    when (element.level) {
                        1 -> t.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline1)
                        2 -> t.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline2)
                        3 -> t.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline3)
                        4 -> t.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline4)
                        5 -> t.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline5)
                        6 -> t.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6)
                    }
                    binding.insertPoint.addView(t)
                }

                is BlockElement.Paragraph -> {
                    val s = SpannableStringBuilder()
                    for (child in element.children) {
                        when (child) {
                            is InlineElement.Regular -> {
                                s.append(child.content)
                            }

                            is InlineElement.Bold -> {
                                s.bold { append(child.content) }
                            }

                            is InlineElement.Italic -> {
                                s.italic { append(child.content) }
                            }

                            is InlineElement.Strikethrough -> {
                                s.strikeThrough { append(child.content) }
                            }

                            is InlineElement.Image -> {
                                if (s.isNotEmpty()) {
                                    val t = TextView(context)
                                    t.text = s
                                    binding.insertPoint.addView(t)

                                    s.clear()
                                }

                                val i = ImageView(context)
                                i.contentDescription = child.alt

                                binding.insertPoint.addView(i)
                                images.add(Pair(i, child.url))
                            }
                        }
                    }
                    if (s.isNotEmpty()) {
                        val t = TextView(context)
                        t.text = s
                        binding.insertPoint.addView(t)
                    }
                }

                is BlockElement.Table -> {
                    val tableView = TableLayout(context)
                    tableView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                    tableView.setBackgroundResource(R.drawable.heavy_border)
                    tableView.isStretchAllColumns = true
                    for (row in element.children) {
                        val rowView = TableRow(context)
                        for (cell in row.children) {
                            val textView = TextView(context)
                            textView.setBackgroundResource(R.drawable.border)
                            textView.text = cell.content
                            textView.setPadding(3)
                            rowView.addView(textView)
                        }
                        tableView.addView(rowView)
                    }

                    binding.insertPoint.addView(tableView)
                }

            }
        }

        return binding.root
    }

    private fun updateBitmaps(bitmaps: Map<String, Bitmap>) {
        for (p in images) {
            if (bitmaps.containsKey(p.second)) {
                val i = p.first
                val b = bitmaps[p.second]
                i.setImageBitmap(b)
            }
        }

    }
}