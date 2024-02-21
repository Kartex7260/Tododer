package kanti.tododer.data.room.colorstyle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ColorStyleDao {

    @Query("SELECT * FROM color_style WHERE id = :id LIMIT 1")
    suspend fun getColorStyleData(id: Int): ColorStyleDataEntity?

    @Query("SELECT * FROM color_style WHERE id = :id LIMIT 1")
    suspend fun getColorStyle(id: Int): ColorStyleEntity?

    @Query("SELECT * FROM color_style WHERE type = :type")
    suspend fun getColorStylesDataByType(type: String): List<ColorStyleDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(colorStyle: ColorStyleEntity)

    @Query("DELETE FROM color_style")
    suspend fun clear()
}