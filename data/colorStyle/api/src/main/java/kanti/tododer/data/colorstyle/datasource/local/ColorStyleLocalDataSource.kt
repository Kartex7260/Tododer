package kanti.tododer.data.colorstyle.datasource.local

import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleData

interface ColorStyleLocalDataSource {

    suspend fun getColorStyleData(id: Int): ColorStyleData?

    suspend fun getColorStyle(id: Int): ColorStyle?

    suspend fun getCustomStylesData(): List<ColorStyleData>

    suspend fun insert(colorStyle: ColorStyle)
}