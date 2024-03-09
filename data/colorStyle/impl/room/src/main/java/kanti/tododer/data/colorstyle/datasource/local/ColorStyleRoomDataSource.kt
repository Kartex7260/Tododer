package kanti.tododer.data.colorstyle.datasource.local

import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleData
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.room.colorstyle.ColorStyleDao
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ColorStyleRoomDataSource @Inject constructor(
    private val dao: ColorStyleDao,
    @StandardLog private val logger: Logger
) : ColorStyleLocalDataSource {

    override suspend fun getColorStyleData(id: Int): ColorStyleData? {
        val result = withContext(Dispatchers.Default) {
            dao.getColorStyleData(id)?.toColorStyleData()
        }
        logger.d(LOG_TAG, "getColorStyleData(Int = $id): return $result")
        return result
    }

    override suspend fun getColorStyle(id: Int): ColorStyle? {
        val result = withContext(Dispatchers.Default) {
            dao.getColorStyle(id)?.toColorStyle()
        }
        logger.d(LOG_TAG, "getColorStyle(Int = $id): return $result")
        return result
    }

    override suspend fun getCustomStylesData(): List<ColorStyleData> {
        val result = withContext(Dispatchers.Default) {
            dao.getColorStylesDataByType(ColorStyleType.Custom.name).map {
                it.toColorStyleData()
            }
        }
        logger.d(LOG_TAG, "getCustomStylesData(): return count(${result.size})")
        return result
    }

    override suspend fun insert(colorStyle: ColorStyle) {
        withContext(Dispatchers.Default) {
            dao.insert(colorStyle.toColorStyleEntity())
        }
        logger.d(LOG_TAG, "insert(ColorStyle = $colorStyle)")
    }

    companion object {

        private const val LOG_TAG = "ColorStyleRoomDataSource"
    }
}