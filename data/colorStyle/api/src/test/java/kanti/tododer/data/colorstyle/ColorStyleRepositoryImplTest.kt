package kanti.tododer.data.colorstyle

import kanti.tododer.data.colorstyle.datasource.local.ColorStyleLocalDataSource
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doReturnConsecutively
import org.mockito.kotlin.mock

class ColorStyleRepositoryImplTest {

    private val logTag = "ColorStyleRepositoryImplTest"
    private val logger = PrintLogger()

    @AfterEach
    fun afterEach() {
        logger.d(logTag, "--------- AFTER EACH ---------")
    }

    @Test
    @DisplayName("getAllColorStyleData() with initializing")
    fun getAllColorStyleDataWithInit() = runTest {
        val dataSourceInvokeInsert = buildMap {
            for (colorStyleId in DefaultColorStyles.Ids.values()) {
                this[colorStyleId] = false
            }
        }.toMutableMap()

        val dataSource = mock<ColorStyleLocalDataSource> {
            for (colorStyleId in DefaultColorStyles.Ids.values()) {
                val colorStyle = DefaultColorStyles.getById(colorStyleId)
                onBlocking {
                    logger.d(logTag, "invoke getColorStyleData")
                    getColorStyleData(colorStyleId)
                } doReturnConsecutively listOf(null, colorStyle)
                onBlocking { insert(colorStyle) } doAnswer {
                    logger.d(logTag, "insert invoked")
                    dataSourceInvokeInsert[colorStyleId] = true
                }
            }

            onBlocking { getCustomStylesData() } doReturn listOf(
                ColorStyleData(id = 1, name = "Test 1"),
                ColorStyleData(id = 2, name = "Test 2")
            )
        }
        val repository = ColorStyleRepositoryImpl(
            localDataSource = dataSource,
            logger = logger
        )
        val expected = buildList {
            for (colorStyle in DefaultColorStyles.values()) {
                add(colorStyle)
            }
            add(ColorStyleData(id = 1, name = "Test 1"))
            add(ColorStyleData(id = 2, name = "Test 2"))
        }.toTypedArray()

        val actual = repository.getAllColorStyleData()
        assertArrayEquals(expected, actual.toTypedArray())
        val allInsertExpected = dataSourceInvokeInsert.values.reduce { acc, b -> acc && b }
        assertTrue(allInsertExpected)
    }

    @Test
    @DisplayName("getAllColorStyleData()")
    fun getAllColorStyleData() = runTest {
        val dataSourceNotInvokeInsert = buildMap {
            for (colorStyleId in DefaultColorStyles.Ids.values()) {
                this[colorStyleId] = true
            }
        }.toMutableMap()

        val dataSource = mock<ColorStyleLocalDataSource> {
            for (colorStyleId in DefaultColorStyles.Ids.values()) {
                val colorStyle = DefaultColorStyles.getById(colorStyleId)
                onBlocking {
                    logger.d(logTag, "invoke getColorStyleData")
                    getColorStyleData(colorStyleId)
                } doReturn colorStyle
                onBlocking { insert(colorStyle) } doAnswer {
                    logger.d(logTag, "insert invoked")
                    dataSourceNotInvokeInsert[colorStyleId] = false
                }
            }

            onBlocking { getCustomStylesData() } doReturn listOf(
                ColorStyleData(id = 1, name = "Test 1"),
                ColorStyleData(id = 2, name = "Test 2")
            )
        }
        val repository = ColorStyleRepositoryImpl(
            localDataSource = dataSource,
            logger = logger
        )
        val expected = buildList {
            for (colorStyle in DefaultColorStyles.values()) {
                add(colorStyle)
            }
            add(ColorStyleData(id = 1, name = "Test 1"))
            add(ColorStyleData(id = 2, name = "Test 2"))
        }.toTypedArray()

        val actual = repository.getAllColorStyleData()
        assertArrayEquals(expected, actual.toTypedArray())
        val allInsertExpected = dataSourceNotInvokeInsert.values.reduce { acc, b -> acc && b }
        assertTrue(allInsertExpected)
    }

    @Test
    @DisplayName("getById(Int) default with initializing")
    fun getByIdDefaultWithInit() = runTest {
        val colorStyleId = DefaultColorStyles.Ids.Standard
        val expected = DefaultColorStyles.Standard
        var dataSourceInsertInvoked = false
        val dataSource = mock<ColorStyleLocalDataSource> {
            onBlocking { getColorStyle(colorStyleId) } doReturnConsecutively listOf(
                null,
                expected
            )
            onBlocking { insert(expected) } doAnswer { dataSourceInsertInvoked = true }
        }
        val repository = ColorStyleRepositoryImpl(localDataSource = dataSource, logger = logger)

        val actual = repository.getById(colorStyleId)
        assertEquals(expected, actual)
        assertTrue(dataSourceInsertInvoked)
    }

    @Test
    @DisplayName("getById(Int) default")
    fun getByIdDefault() = runTest {
        val colorStyleId = DefaultColorStyles.Ids.Standard
        val expected = DefaultColorStyles.Standard
        var dataSourceInsertInvoked = false
        val dataSource = mock<ColorStyleLocalDataSource> {
            onBlocking { getColorStyle(colorStyleId) } doReturn expected
            onBlocking { insert(expected) } doAnswer { dataSourceInsertInvoked = true }
        }
        val repository = ColorStyleRepositoryImpl(localDataSource = dataSource, logger = logger)

        val actual = repository.getById(colorStyleId)
        assertEquals(expected, actual)
        assertFalse(dataSourceInsertInvoked)
    }

    @Test
    @DisplayName("getById(Int) custom not found")
    fun getByIdCustomNotFound() = runTest {
        val colorStyleId = 1
        val dataSource = mock<ColorStyleLocalDataSource> {
            onBlocking { getColorStyle(colorStyleId) } doReturn null
        }
        val repository = ColorStyleRepositoryImpl(localDataSource = dataSource, logger = logger)

        val actual = repository.getById(colorStyleId)
        assertNull(actual)
    }

    @Test
    @DisplayName("getById(Int) custom")
    fun getByIdCustom() = runTest {
        val colorStyleId = 1
        val expected = ColorStyle(id = 1)
        val dataSource = mock<ColorStyleLocalDataSource> {
            onBlocking { getColorStyle(colorStyleId) } doReturn expected
        }
        val repository = ColorStyleRepositoryImpl(localDataSource = dataSource, logger = logger)

        val actual = repository.getById(colorStyleId)
        assertEquals(expected, actual)
    }
}