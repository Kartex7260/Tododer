package kanti.tododer.data.colorstyle

import kanti.tododer.data.colorstyle.datasource.local.ColorStyleLocalDataSource
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ColorStyleRepositoryImpl @Inject constructor(
    private val localDataSource: ColorStyleLocalDataSource,
    @StandardLog private val logger: Logger
) : ColorStyleRepository {

    override suspend fun getAllColorStyleData(): List<ColorStyleData> {
        val colorStylesData = withContext(Dispatchers.Default) {
            val colorStylesData = ArrayList<ColorStyleData>()
            for (colorStyleId in DefaultColorStyles.Ids.values()) {
                try {
                    val data = getOrInit(
                        get = { localDataSource.getColorStyleData(colorStyleId) },
                        init = {
                            val colorStyle = DefaultColorStyles.getById(colorStyleId)
                            localDataSource.insert(colorStyle)
                        }
                    )
                    colorStylesData.add(data)
                } catch (ex: Throwable) {
                    logger.e(LOG_TAG, ex.localizedMessage, ex)
                }
            }
            val customStylesData = localDataSource.getCustomStylesData()
            colorStylesData.addAll(customStylesData)
            colorStylesData
        }
        logger.d(LOG_TAG, "getAllColorStyleData(): return count(${colorStylesData.size})")
        return colorStylesData
    }

    override suspend fun getById(id: Int): ColorStyle? {
        val result = withContext(Dispatchers.Default) {
            when (id) {
                in DefaultColorStyles.Ids.values() -> {
                    getOrInit(
                        get = { localDataSource.getColorStyle(id) },
                        init = {
                            val colorStyle = DefaultColorStyles.getById(id)
                            localDataSource.insert(colorStyle)
                        }
                    )
                }

                else -> localDataSource.getColorStyle(id)
            }
        }
        logger.d(LOG_TAG, "getById(Int = $id): return $result")
        return result
    }

    private suspend fun <Res> getOrInit(get: suspend () -> Res?, init: suspend () -> Unit): Res {
        val data = get()
        if (data == null) {
            init()
            return get() ?: throw IllegalStateException("Initialization did not work")
        }
        return data
    }

    companion object {

        private const val LOG_TAG = "ColorStyleRepositoryImpl"
    }
}