package kanti.tododer.data.colorstyle.datasource.local

import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleData
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.room.colorstyle.ColorStyleDao
import kanti.tododer.data.room.colorstyle.ColorStyleDataEntity
import kanti.tododer.data.room.colorstyle.ColorStyleEntity
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class ColorStyleRoomDataSourceTest {

    private val logTag = "ColorStyleRoomDataSourceTest"
    private val logger = PrintLogger()

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "---------- AFTER EACH ----------")
    }

    @Test
    @DisplayName("getColorStyleData(Int): not found")
    fun getColorStyleDataNotFound() = runTest {
        val colorStyleId = 1
        val dao = mock<ColorStyleDao> {
            onBlocking { getColorStyleData(colorStyleId) } doReturn null
        }
        val dataSource = ColorStyleRoomDataSource(dao = dao, logger = logger)

        val actual = dataSource.getColorStyleData(colorStyleId)
        assertNull(actual)
    }

    @Test
    @DisplayName("getColorStyleData(Int)")
    fun getColorStyleData() = runTest {
        val colorStyleId = 1
        val dao = mock<ColorStyleDao> {
            onBlocking { getColorStyleData(colorStyleId) } doReturn ColorStyleDataEntity(
                id = 1,
                name = "Test",
                type = "Custom"
            )
        }
        val dataSource = ColorStyleRoomDataSource(dao = dao, logger = logger)
        val expected = ColorStyleData(id = 1, name = "Test", type = ColorStyleType.Custom)

        val actual = dataSource.getColorStyleData(colorStyleId)
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("getColorStyle(Int): not found")
    fun getColorStyleNotFound() = runTest {
        val colorStyleId = 1
        val dao = mock<ColorStyleDao> {
            onBlocking { getColorStyle(colorStyleId) } doReturn null
        }
        val dataSource = ColorStyleRoomDataSource(dao = dao, logger = logger)

        val actual = dataSource.getColorStyle(colorStyleId)
        assertNull(actual)
    }

    @Test
    @DisplayName("getColorStyle(Int)")
    fun getColorStyle() = runTest {
        val colorStyleId = 1
        val dao = mock<ColorStyleDao> {
            onBlocking { getColorStyle(colorStyleId) } doReturn ColorStyleEntity(
                id = 1,
                name = "Test",
                type = "Custom",

                primaryLight = 1,
                onPrimaryLight = 2,
                primaryContainerLight = 3,
                onPrimaryContainerLight = 4,
                secondaryLight = 5,
                onSecondaryLight = 6,
                secondaryContainerLight = 7,
                onSecondaryContainerLight = 8,
                tertiaryLight = 9,
                onTertiaryLight = 10,
                tertiaryContainerLight = 11,
                onTertiaryContainerLight = 12,
                errorLight = 13,
                onErrorLight = 14,
                errorContainerLight = 15,
                onErrorContainerLight = 16,
                backgroundLight = 17,
                onBackgroundLight = 18,
                surfaceLight = 19,
                onSurfaceLight = 20,
                surfaceVariantLight = 21,
                onSurfaceVariantLight = 22,
                outlineLight = 23,

                primaryDark = 24,
                onPrimaryDark = 25,
                primaryContainerDark = 26,
                onPrimaryContainerDark = 27,
                secondaryDark = 28,
                onSecondaryDark = 29,
                secondaryContainerDark = 30,
                onSecondaryContainerDark = 31,
                tertiaryDark = 32,
                onTertiaryDark = 33,
                tertiaryContainerDark = 34,
                onTertiaryContainerDark = 35,
                errorDark = 36,
                onErrorDark = 37,
                errorContainerDark = 38,
                onErrorContainerDark = 39,
                backgroundDark = 40,
                onBackgroundDark = 41,
                surfaceDark = 42,
                onSurfaceDark = 43,
                surfaceVariantDark = 44,
                onSurfaceVariantDark = 45,
                outlineDark = 46
            )
        }
        val dataSource = ColorStyleRoomDataSource(dao = dao, logger = logger)
        val expected = ColorStyle(
            id = 1,
            name = "Test",
            type = ColorStyleType.Custom,

            primaryLight = 1,
            onPrimaryLight = 2,
            primaryContainerLight = 3,
            onPrimaryContainerLight = 4,
            secondaryLight = 5,
            onSecondaryLight = 6,
            secondaryContainerLight = 7,
            onSecondaryContainerLight = 8,
            tertiaryLight = 9,
            onTertiaryLight = 10,
            tertiaryContainerLight = 11,
            onTertiaryContainerLight = 12,
            errorLight = 13,
            onErrorLight = 14,
            errorContainerLight = 15,
            onErrorContainerLight = 16,
            backgroundLight = 17,
            onBackgroundLight = 18,
            surfaceLight = 19,
            onSurfaceLight = 20,
            surfaceVariantLight = 21,
            onSurfaceVariantLight = 22,
            outlineLight = 23,

            primaryDark = 24,
            onPrimaryDark = 25,
            primaryContainerDark = 26,
            onPrimaryContainerDark = 27,
            secondaryDark = 28,
            onSecondaryDark = 29,
            secondaryContainerDark = 30,
            onSecondaryContainerDark = 31,
            tertiaryDark = 32,
            onTertiaryDark = 33,
            tertiaryContainerDark = 34,
            onTertiaryContainerDark = 35,
            errorDark = 36,
            onErrorDark = 37,
            errorContainerDark = 38,
            onErrorContainerDark = 39,
            backgroundDark = 40,
            onBackgroundDark = 41,
            surfaceDark = 42,
            onSurfaceDark = 43,
            surfaceVariantDark = 44,
            onSurfaceVariantDark = 45,
            outlineDark = 46
        )

        val actual = dataSource.getColorStyle(colorStyleId)
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("getCustomStylesData()")
    fun getCustomStylesData() = runTest {
        val dao = mock<ColorStyleDao> {
            val type = ColorStyleType.Custom.name
            onBlocking { getColorStylesDataByType(type) } doReturn listOf(
                ColorStyleDataEntity(id = 1, name = "Test 1", type = type),
                ColorStyleDataEntity(id = 2, name = "Foo", type = type)
            )
        }
        val dataSource = ColorStyleRoomDataSource(dao = dao, logger = logger)
        val expected = arrayOf(
            ColorStyleData(id = 1, name = "Test 1", type = ColorStyleType.Custom),
            ColorStyleData(id = 2, name = "Foo", type = ColorStyleType.Custom)
        )

        val actual = dataSource.getCustomStylesData()
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    @DisplayName("insert(ColorStyle)")
    fun insert() = runTest {
        val colorStyle = ColorStyle(id = 1, name = "Test")
        var daoInsertInvoked = false
        val dao = mock<ColorStyleDao> {
            onBlocking { insert(colorStyle.toColorStyleEntity()) } doAnswer {
                daoInsertInvoked = true
            }
        }
        val dataSource = ColorStyleRoomDataSource(dao = dao, logger = logger)
        dataSource.insert(colorStyle)

        assertTrue(daoInsertInvoked)
    }
}