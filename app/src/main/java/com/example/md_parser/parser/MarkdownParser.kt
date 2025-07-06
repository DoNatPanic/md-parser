package com.example.md_parser.parser

sealed class InlineElement {
    data class Regular(val content: String) : InlineElement()
    data class Bold(val content: String) : InlineElement()
    data class Italic(val content: String) : InlineElement()
    data class Strikethrough(val content: String) : InlineElement()
    data class Image(val alt: String, val url: String) : InlineElement()
}

sealed class TableComponent {
    data class TableCell(val content: String) : TableComponent()
    data class TableRow(val children: List<TableCell>) : TableComponent()
}

sealed class BlockElement {
    data class Header(val level: Int, val content: String) : BlockElement()
    data class Paragraph(val children: List<InlineElement>) : BlockElement()
    data class Table(val children: List<TableComponent.TableRow>) : BlockElement()
}

data class BlockBuilderItem(val type: String, val content: MutableList<String>)

data class EmphasisRun(val start: Int, var end: Int, val char: Char) {
    fun length(): Int {
        return end - start + 1
    }
}

class MarkdownParser {
    companion object {
        fun parse(markdownString: String): List<BlockElement> {
            val result = mutableListOf<BlockElement>()

            val builderItems = mutableListOf<BlockBuilderItem>()
            var lastItem = BlockBuilderItem("paragraph", mutableListOf())
            builderItems.add(lastItem)

            val headerRegex = Regex("^(#{1,6}) (.+)$")

            for (line in markdownString.lines()) {
                val blockType = when {
                    line.isBlank() -> "blank"
                    line.matches(headerRegex) -> "header"
                    line.startsWith("|") -> "table"
                    else -> "paragraph"
                }


                if (blockType != lastItem.type) {
                    // close previous block
                    if (lastItem.type == "paragraph" && lastItem.content.isEmpty()) {
                        builderItems.removeAt(builderItems.lastIndex)
                    }

                    // by default use paragraph type - if another type is met, paragraph will be removed
                    lastItem = BlockBuilderItem(
                        if (blockType == "blank") "paragraph" else blockType,
                        mutableListOf()
                    )
                    builderItems.add(lastItem)
                }

                if (blockType != "blank") {
                    lastItem.content.add(line)
                }

                if (blockType == "header") {
                    // start new builder item since header assumed to be one line only
                    lastItem = BlockBuilderItem("paragraph", mutableListOf())
                    builderItems.add(lastItem)
                }
            }

            for (item in builderItems) {
                val resultItem = when (item.type) {
                    "paragraph" -> if (item.content.isEmpty()) null else BlockElement.Paragraph(
                        parseInlineElements(item.content)
                    )

                    "header" -> {
                        val content = item.content.joinToString(" ")
                        val match = headerRegex.matchEntire(content)!!
                        val headerLevel = match.groups[1]!!.value.length
                        val headerValue = match.groups[2]!!.value
                        BlockElement.Header(headerLevel, headerValue)
                    }

                    "table" -> parseTable(item.content)

                    else -> null
                }
                if (resultItem != null) {
                    result.add(resultItem)
                }
            }

            return result.toList()
        }

        fun parseInlineElements(content: List<String>): List<InlineElement> {
            val resultContent = mutableListOf<InlineElement>()
            for (line in content.map { it.trim() }) {
                val emphasis = mutableListOf<EmphasisRun>()
                var prev = ' '
                for (c in line.withIndex()) {
                    if (c.value == '*' || c.value == '_' || c.value == '~') {
                        if (emphasis.isNotEmpty() && emphasis.last().takeIf { it.char == c.value && it.end == c.index - 1 } != null) {
                            emphasis.last().end += 1
                        } else {
                            emphasis.add(EmphasisRun(c.index, c.index, c.value))
                        }
                    }
                    if (c.value == '[' && prev == '!') {
                        emphasis.add(EmphasisRun(c.index - 1, c.index, '!'))
                    }
                    if (c.value == '(' && prev == ']') {
                        emphasis.add(EmphasisRun(c.index - 1, c.index, ']'))
                    }
                    if (c.value == ')') {
                        emphasis.add(EmphasisRun(c.index, c.index, ')'))
                    }
                    prev = c.value
                }

                val opens = mutableListOf<EmphasisRun>()
                var lineResult = mutableListOf<InlineElement>()
                var tailStart = 0
                var tailEnd = 0
                for (run in emphasis) {
                    var found = false
                    var inlineElement: InlineElement? = null
                    if (run.char == '!' || run.char == ']') {
                        opens.add(run)
                        continue
                    }
                    if (run.char == ')') {
                        var toFind = '!'
                        var linkStart = 0
                        for (open in opens) {
                            if (open.char == toFind) {
                                if (toFind == '!') {
                                    linkStart = open.start
                                    toFind = ']'
                                } else {
                                    found = true
                                    inlineElement = InlineElement.Image(
                                        line.substring(linkStart + 2, open.start),
                                        line.substring(open.end + 1, run.start)
                                    )
                                    tailEnd = linkStart - 1
                                    break
                                }
                            }
                        }
                    } else {
                        for (open in opens) {
                            if (open.char == run.char && open.length() == run.length()) {
                                val content = line.substring(open.end + 1, run.start)
                                inlineElement = when (Pair(open.char, open.length())) {
                                    Pair('*', 1) -> InlineElement.Italic(content)
                                    Pair('*', 2) ->  InlineElement.Bold(content)
                                    Pair('_', 1) ->  InlineElement.Italic(content)
                                    Pair('_', 2) ->  InlineElement.Bold(content)
                                    Pair('~', 1) ->  InlineElement.Strikethrough(content)
                                    Pair('~', 2) ->  InlineElement.Strikethrough(content)
                                    else -> null
                                }
                                if (inlineElement != null) {
                                    found = true
                                    tailEnd = open.start - 1
                                    break
                                }
                            }
                        }
                        if (!found) {
                            opens.add(run)
                        }
                    }

                    if (found && inlineElement != null) {
                        if (tailEnd - tailStart >= 0) {
                            val content = line.substring(tailStart, tailEnd + 1)
                            lineResult.add(InlineElement.Regular(content))
                        }
                        lineResult.add(inlineElement)
                        tailStart = run.end + 1
                        // clear all opened and non closed
                        opens.clear()
                    }
                }

                if (tailStart < line.length) {
                    lineResult.add(InlineElement.Regular(line.substring(tailStart, line.lastIndex + 1)))
                }

                if (lineResult.isNotEmpty()) {
                    if (resultContent.isNotEmpty() && resultContent.last() is InlineElement.Regular && lineResult.first() is InlineElement.Regular) {
                        val prevLast = resultContent.removeAt(resultContent.lastIndex) as InlineElement.Regular
                        val lineFirst = lineResult.removeAt(0) as InlineElement.Regular
                        resultContent.add(InlineElement.Regular(prevLast.content + " " + lineFirst.content))
                    }
                    resultContent.addAll(lineResult)
                }

            }
            return resultContent.toList()
        }

        fun parseTable(content: List<String>): BlockElement.Table {
            val rows = mutableListOf<TableComponent.TableRow>()
            for (line in content.withIndex()) {
                if (line.index == 1) {
                    // delimiter row - ignore for now
                } else {
                    val cells = line.value
                        .removeSurrounding("|")
                        .split("|")
                        .map { TableComponent.TableCell(it.trim()) }
                    rows.add(TableComponent.TableRow(cells))
                }
            }
            return BlockElement.Table(rows)
        }
    }
}