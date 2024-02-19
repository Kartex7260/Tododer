package kanti.tododer.data.colorstyle.datasource.local

import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleData
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.room.colorstyle.ColorStyleDao
import javax.inject.Inject

class ColorStyleRoomDataSource @Inject constructor(
    private val dao: ColorStyleDao
) : ColorStyleLocalDataSource {

    override suspend fun getColorStyleData(id: Int): ColorStyleData? {
        return dao.getColorStyleData(id)?.toColorStyleData()
    }

    override suspend fun getColorStyle(id: Int): ColorStyle? {
        return dao.getColorStyle(id)?.toColorStyle()
    }

    override suspend fun getCustomStylesData(): List<ColorStyleData> {
        return dao.getColorStylesDataByType(ColorStyleType.Custom.name).map {
            it.toColorStyleData()
        }
    }

    override suspend fun insert(colorStyle: ColorStyle) {
        dao.insert(colorStyle.toColorStyleEntity())
    }
}