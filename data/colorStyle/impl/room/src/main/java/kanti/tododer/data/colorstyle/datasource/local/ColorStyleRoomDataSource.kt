package kanti.tododer.data.colorstyle.datasource.local

import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleData
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.room.colorstyle.ColorStyleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ColorStyleRoomDataSource @Inject constructor(
    private val dao: ColorStyleDao
) : ColorStyleLocalDataSource {

    override suspend fun getColorStyleData(id: Int): ColorStyleData? {
        return withContext(Dispatchers.Default) {
            dao.getColorStyleData(id)?.toColorStyleData()
        }
    }

    override suspend fun getColorStyle(id: Int): ColorStyle? {
        return withContext(Dispatchers.Default) {
            dao.getColorStyle(id)?.toColorStyle()
        }
    }

    override suspend fun getCustomStylesData(): List<ColorStyleData> {
        return withContext(Dispatchers.Default) {
            dao.getColorStylesDataByType(ColorStyleType.Custom.name).map {
                it.toColorStyleData()
            }
        }
    }

    override suspend fun insert(colorStyle: ColorStyle) {
        withContext(Dispatchers.Default) {
            dao.insert(colorStyle.toColorStyleEntity())
        }
    }
}