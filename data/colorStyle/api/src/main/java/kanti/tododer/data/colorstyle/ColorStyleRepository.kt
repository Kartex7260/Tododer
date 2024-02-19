package kanti.tododer.data.colorstyle

interface ColorStyleRepository {

    suspend fun getAllColorStyleData(): List<ColorStyleData>

    suspend fun getById(id: Int): ColorStyle?
}