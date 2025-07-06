package com.example.md_parser.parser

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MarkdownParserTest {
    @Test
    fun firstLevelHeader_parsedSuccessfully() {
        val result = MarkdownParser.parse("# Test")
        assertEquals(listOf(BlockElement.Header(1,  "Test")), result)
    }

    @Test
    fun secondLevelHeader_parsedSuccessfully() {
        val result = MarkdownParser.parse("## Test")
        assertEquals(listOf(BlockElement.Header(2,  "Test")), result)
    }

    @Test
    fun thirdLevelHeader_parsedSuccessfully() {
        val result = MarkdownParser.parse("### Test")
        assertEquals(listOf(BlockElement.Header(3,  "Test")), result)
    }

    @Test
    fun fourthLevelHeader_parsedSuccessfully() {
        val result = MarkdownParser.parse("#### Test")
        assertEquals(listOf(BlockElement.Header(4,  "Test")), result)
    }

    @Test
    fun fifthLevelHeader_parsedSuccessfully() {
        val result = MarkdownParser.parse("##### Test")
        assertEquals(listOf(BlockElement.Header(5,  "Test")), result)
    }

    @Test
    fun sixthLevelHeader_parsedSuccessfully() {
        val result = MarkdownParser.parse("###### Test")
        assertEquals(listOf(BlockElement.Header(6,  "Test")), result)
    }

    @Test
    fun paragraphPlain_parsedSuccessfully() {
        val testString = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
            Aliquam dui nunc, ultricies et eleifend sed, mattis id mauris. Sed at gravida odio.
            Maecenas id diam nec arcu pellentesque aliquet.
            Mauris aliquam at sem vitae pretium.
            """
        val result = MarkdownParser.parse(testString)
        val expectedString = testString.trimIndent().lines().joinToString(" ")
        assertEquals(listOf(BlockElement.Paragraph(listOf(InlineElement.Regular(expectedString)))), result)
    }

    @Test
    fun emphasis_parsedSuccessfully() {
        val testString = """Lorem *ipsum* dolor sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Italic("ipsum"),
                InlineElement.Regular(" dolor sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun strongEmphasis_parsedSuccessfully() {
        val testString = """Lorem **ipsum** dolor sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Bold("ipsum"),
                InlineElement.Regular(" dolor sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun mixedEmphasis_parsedSuccessfully() {
        val testString = """Lorem **ipsum** *dolor* sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Bold("ipsum"),
                InlineElement.Regular(" "),
                InlineElement.Italic("dolor"),
                InlineElement.Regular(" sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun altEmphasis_parsedSuccessfully() {
        val testString = """Lorem _ipsum_ dolor sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Italic("ipsum"),
                InlineElement.Regular(" dolor sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun altStrongEmphasis_parsedSuccessfully() {
        val testString = """Lorem __ipsum__ dolor sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Bold("ipsum"),
                InlineElement.Regular(" dolor sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun altMixedEmphasis_parsedSuccessfully() {
        val testString = """Lorem __ipsum__ _dolor_ sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Bold("ipsum"),
                InlineElement.Regular(" "),
                InlineElement.Italic("dolor"),
                InlineElement.Regular(" sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun strikethrough_parsedSuccessfully() {
        val testString = """Lorem ~~ipsum~~ dolor sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Regular("Lorem "),
                InlineElement.Strikethrough("ipsum"),
                InlineElement.Regular(" dolor sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun image_parsedSuccessfully() {
        val testString = """![Lorem ipsum](https://shorturl.at/xCGz8) dolor sit amet"""
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Paragraph(
            listOf(
                InlineElement.Image("Lorem ipsum", "https://shorturl.at/xCGz8"),
                InlineElement.Regular(" dolor sit amet"),
            )
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun tableCorrect_parsedSuccessfully() {
        val testString = """
            | id | Column 1 | Column 2 |
            | -- | -------- | -------- |
            | 1  | asdf     | 444.1    |
            | 2  | hjkl     |  55.77   |
            """.trimIndent()
        val result = MarkdownParser.parse(testString)
        val expected = BlockElement.Table(
            listOf(
                TableComponent.TableRow(
                    listOf(
                        TableComponent.TableCell("id"),
                        TableComponent.TableCell("Column 1"),
                        TableComponent.TableCell("Column 2"),
                    )
                ),
                TableComponent.TableRow(
                    listOf(
                        TableComponent.TableCell("1"),
                        TableComponent.TableCell("asdf"),
                        TableComponent.TableCell("444.1"),
                    )
                ),
                TableComponent.TableRow(
                    listOf(
                        TableComponent.TableCell("2"),
                        TableComponent.TableCell("hjkl"),
                        TableComponent.TableCell("55.77"),
                    )
                ),
            )
        )
        assertEquals(listOf(expected), result)
    }
}