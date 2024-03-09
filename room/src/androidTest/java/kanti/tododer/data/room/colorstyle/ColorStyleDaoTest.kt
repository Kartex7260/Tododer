package kanti.tododer.data.room.colorstyle

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kanti.tododer.data.room.TododerDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ColorStyleDaoTest {

    private val db: TododerDatabase
    private val colorStyleDao: ColorStyleDao

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context, TododerDatabase::class.java, "test").build()
        colorStyleDao = db.colorStyleDao()
    }

    @After
    fun after() = runTest {
        colorStyleDao.clear()
    }

    @Test
    fun getColorStyleData() = runTest {
        val colorStyleId = 1
        var actual = colorStyleDao.getColorStyleData(colorStyleId)
        assertNull(actual)
        val expected = ColorStyleDataEntity(id = colorStyleId)
        colorStyleDao.insert(ColorStyleEntity(id = colorStyleId))

        actual = colorStyleDao.getColorStyleData(colorStyleId)
        assertEquals(expected, actual)
    }

    @Test
    fun getColorStyle() = runTest {
        val colorStyleId = 1
        var actual = colorStyleDao.getColorStyle(colorStyleId)
        assertNull(actual)
        val expected = ColorStyleEntity(id = colorStyleId)
        colorStyleDao.insert(ColorStyleEntity(id = colorStyleId))

        actual = colorStyleDao.getColorStyle(colorStyleId)
        assertEquals(expected, actual)
    }

    @Test
    fun getColorStylesDataByType() = runTest {
        val type = "Custom"
        var actual = colorStyleDao.getColorStylesDataByType(type)
        assertArrayEquals(arrayOf(), actual.toTypedArray())

        colorStyleDao.insert(ColorStyleEntity(id = 1, type = "Standard"))
        colorStyleDao.insert(ColorStyleEntity(id = 2, type = type))
        colorStyleDao.insert(ColorStyleEntity(id = 3, type = type))
        colorStyleDao.insert(ColorStyleEntity(id = 4, type = "Red"))
        val expected = arrayOf(
            ColorStyleDataEntity(id = 2, type = type),
            ColorStyleDataEntity(id = 3, type = type)
        )

        actual = colorStyleDao.getColorStylesDataByType(type)
        assertArrayEquals(expected, actual.toTypedArray())
    }
}