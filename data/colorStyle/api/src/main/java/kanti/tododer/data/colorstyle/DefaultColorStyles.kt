package kanti.tododer.data.colorstyle

object DefaultColorStyles {

    object Ids {

        const val Standard = -1

        const val Red = 1
        const val Orange = 2
        const val Yellow = 3
        const val Green = 4
        const val LightBlue = 5
        const val Blue = 6
        const val Purple = 7

        fun values(): List<Int> = listOf(
            Standard, Red, Orange, Yellow, Green, LightBlue, Blue, Purple
        )
    }

    fun getById(id: Int): ColorStyle {
        return when (id) {
            Ids.Standard -> Standard
            Ids.Red -> Red
            Ids.Orange -> Orange
            Ids.Yellow -> Yellow
            Ids.Green -> Green
            Ids.LightBlue -> LightBlue
            Ids.Blue -> Blue
            Ids.Purple -> Purple
            else -> throw IllegalArgumentException("Not found default style by id = $id")
        }
    }

    object Standard : ColorStyle {

        override val id: Int = Ids.Standard
        override val name: String = "Standard"
        override val type: ColorStyleType = ColorStyleType.Standard

        override val primaryLight: Long = 0xFF006491
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFc9e6ff
        override val onPrimaryContainerLight: Long = 0xFF001e2f

        override val secondaryLight: Long = 0xFF536600
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFd5ed7e
        override val onSecondaryContainerLight: Long = 0xFF171e00

        override val tertiaryLight: Long = 0xFF805611
        override val onTertiaryLight: Long = 0xFF825500
        override val tertiaryContainerLight: Long = 0xFFffddb4
        override val onTertiaryContainerLight: Long = 0xFF291800

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFba1a1a
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfcfcff
        override val onBackgroundLight: Long = 0xFF191c1e
        override val surfaceLight: Long = 0xFFfcfcff
        override val onSurfaceLight: Long = 0xFF191c1e
        override val surfaceVariantLight: Long = 0xFFdde3ea
        override val onSurfaceVariantLight: Long = 0xFF41474d

        override val outlineLight: Long = 0xFF71787e


        override val primaryDark: Long = 0xFF89ceff
        override val onPrimaryDark: Long = 0xFF00344d
        override val primaryContainerDark: Long = 0xFF004c6e
        override val onPrimaryContainerDark: Long = 0xFFc9e6ff

        override val secondaryDark: Long = 0xFFb9d165
        override val onSecondaryDark: Long = 0xFF2a3400
        override val secondaryContainerDark: Long = 0xFF3e4c00
        override val onSecondaryContainerDark: Long = 0xFFd5ed7e

        override val tertiaryDark: Long = 0xFFffb953
        override val onTertiaryDark: Long = 0xFF452b00
        override val tertiaryContainerDark: Long = 0xFF633f00
        override val onTertiaryContainerDark: Long = 0xFFffddb4

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF191c1e
        override val onBackgroundDark: Long = 0xFFe2e2e5
        override val surfaceDark: Long = 0xFF191c1e
        override val onSurfaceDark: Long = 0xFFe2e2e5
        override val surfaceVariantDark: Long = 0xFF41474d
        override val onSurfaceVariantDark: Long = 0xFFc1c7ce

        override val outlineDark: Long = 0xFF8b9198
    }

    object Red : ColorStyle {

        override val id: Int = Ids.Red
        override val name: String = "Red"
        override val type: ColorStyleType = ColorStyleType.Red

        override val primaryLight: Long = 0xFFba1724
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFffdad7
        override val onPrimaryContainerLight: Long = 0xFF410004

        override val secondaryLight: Long = 0xFF775654
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFffdad7
        override val onSecondaryContainerLight: Long = 0xFF2c1514

        override val tertiaryLight: Long = 0xFF725b2e
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFffdea6
        override val onTertiaryContainerLight: Long = 0xFF271900

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfffbff
        override val onBackgroundLight: Long = 0xFF201a1a
        override val surfaceLight: Long = 0xFFfffbff
        override val onSurfaceLight: Long = 0xFF201a1a
        override val surfaceVariantLight: Long = 0xFFf5dddb
        override val onSurfaceVariantLight: Long = 0xFF534342

        override val outlineLight: Long = 0xFF857371


        override val primaryDark: Long = 0xFFffb3ae
        override val onPrimaryDark: Long = 0xFF68000b
        override val primaryContainerDark: Long = 0xFF930014
        override val onPrimaryContainerDark: Long = 0xFFffdad7

        override val secondaryDark: Long = 0xFFe7bdb9
        override val onSecondaryDark: Long = 0xFF442927
        override val secondaryContainerDark: Long = 0xFF5d3f3d
        override val onSecondaryContainerDark: Long = 0xFFffdad7

        override val tertiaryDark: Long = 0xFFe2c28c
        override val onTertiaryDark: Long = 0xFF402d04
        override val tertiaryContainerDark: Long = 0xFF594319
        override val onTertiaryContainerDark: Long = 0xFFffdea6

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF201a1a
        override val onBackgroundDark: Long = 0xFFede0de
        override val surfaceDark: Long = 0xFF201a1a
        override val onSurfaceDark: Long = 0xFFede0de
        override val surfaceVariantDark: Long = 0xFF534342
        override val onSurfaceVariantDark: Long = 0xFFd8c2bf

        override val outlineDark: Long = 0xFFa08c8a
    }

    object Orange : ColorStyle {

        override val id: Int = Ids.Orange
        override val name: String = "Orange"
        override val type: ColorStyleType = ColorStyleType.Orange

        override val primaryLight: Long = 0xFF855400
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFffddb7
        override val onPrimaryContainerLight: Long = 0xFF2a1700

        override val secondaryLight: Long = 0xFF705b41
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFfcdebc
        override val onSecondaryContainerLight: Long = 0xFF281805

        override val tertiaryLight: Long = 0xFF53643e
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFd6e9b9
        override val onTertiaryContainerLight: Long = 0xFF121f03

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfffbff
        override val onBackgroundLight: Long = 0xFF1f1b16
        override val surfaceLight: Long = 0xFFfffbff
        override val onSurfaceLight: Long = 0xFF1f1b16
        override val surfaceVariantLight: Long = 0xFFf0e0d0
        override val onSurfaceVariantLight: Long = 0xFF504539

        override val outlineLight: Long = 0xFF827568


        override val primaryDark: Long = 0xFFffb95c
        override val onPrimaryDark: Long = 0xFF462a00
        override val primaryContainerDark: Long = 0xFF653e00
        override val onPrimaryContainerDark: Long = 0xFFffddb7

        override val secondaryDark: Long = 0xFFdfc2a2
        override val onSecondaryDark: Long = 0xFF3f2d17
        override val secondaryContainerDark: Long = 0xFF57432b
        override val onSecondaryContainerDark: Long = 0xFFfcdebc

        override val tertiaryDark: Long = 0xFFbacd9f
        override val onTertiaryDark: Long = 0xFF263514
        override val tertiaryContainerDark: Long = 0xFF3c4c28
        override val onTertiaryContainerDark: Long = 0xFFd6e9b9

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF1f1b16
        override val onBackgroundDark: Long = 0xFFebe1d9
        override val surfaceDark: Long = 0xFF1f1b16
        override val onSurfaceDark: Long = 0xFFebe1d9
        override val surfaceVariantDark: Long = 0xFF504539
        override val onSurfaceVariantDark: Long = 0xFFd4c4b5

        override val outlineDark: Long = 0xFF9c8e80
    }

    object Yellow : ColorStyle {

        override val id: Int = Ids.Yellow
        override val name: String = "Yellow"
        override val type: ColorStyleType = ColorStyleType.Yellow

        override val primaryLight: Long = 0xFF656100
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFefe76c
        override val onPrimaryContainerLight: Long = 0xFF1e1c00

        override val secondaryLight: Long = 0xFF625f42
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFe9e4be
        override val onSecondaryContainerLight: Long = 0xFF1e1c05

        override val tertiaryLight: Long = 0xFF3f6654
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFc1ecd5
        override val onTertiaryContainerLight: Long = 0xFF002115

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfffbff
        override val onBackgroundLight: Long = 0xFF1c1c16
        override val surfaceLight: Long = 0xFFfffbff
        override val onSurfaceLight: Long = 0xFF1c1c16
        override val surfaceVariantLight: Long = 0xFFe7e3d1
        override val onSurfaceVariantLight: Long = 0xFF49473a

        override val outlineLight: Long = 0xFF7a7768


        override val primaryDark: Long = 0xFFd2ca53
        override val onPrimaryDark: Long = 0xFF343200
        override val primaryContainerDark: Long = 0xFF4c4800
        override val onPrimaryContainerDark: Long = 0xFFefe76c

        override val secondaryDark: Long = 0xFFccc8a3
        override val onSecondaryDark: Long = 0xFF333118
        override val secondaryContainerDark: Long = 0xFF4a482c
        override val onSecondaryContainerDark: Long = 0xFFe9e4be

        override val tertiaryDark: Long = 0xFFa5d0ba
        override val onTertiaryDark: Long = 0xFF0e3728
        override val tertiaryContainerDark: Long = 0xFF274e3d
        override val onTertiaryContainerDark: Long = 0xFFc1ecd5

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF1c1c16
        override val onBackgroundDark: Long = 0xFFe6e2d9
        override val surfaceDark: Long = 0xFF1c1c16
        override val onSurfaceDark: Long = 0xFFe6e2d9
        override val surfaceVariantDark: Long = 0xFF49473a
        override val onSurfaceVariantDark: Long = 0xFFcbc7b5

        override val outlineDark: Long = 0xFF949181
    }

    object Green : ColorStyle {

        override val id: Int = Ids.Green
        override val name: String = "Green"
        override val type: ColorStyleType = ColorStyleType.Green

        override val primaryLight: Long = 0xFF006e00
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFF8dfb77
        override val onPrimaryContainerLight: Long = 0xFF002200

        override val secondaryLight: Long = 0xFF54634d
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFd7e8cd
        override val onSecondaryContainerLight: Long = 0xFF121f0e

        override val tertiaryLight: Long = 0xFF386568
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFbcebee
        override val onTertiaryContainerLight: Long = 0xFF002022

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfcfdf6
        override val onBackgroundLight: Long = 0xFF1a1c18
        override val surfaceLight: Long = 0xFFfcfdf6
        override val onSurfaceLight: Long = 0xFF1a1c18
        override val surfaceVariantLight: Long = 0xFFdfe4d7
        override val onSurfaceVariantLight: Long = 0xFF43483f

        override val outlineLight: Long = 0xFF73796e


        override val primaryDark: Long = 0xFF72de5e
        override val onPrimaryDark: Long = 0xFF003a00
        override val primaryContainerDark: Long = 0xFF005300
        override val onPrimaryContainerDark: Long = 0xFF8dfb77

        override val secondaryDark: Long = 0xFFbbcbb2
        override val onSecondaryDark: Long = 0xFF263422
        override val secondaryContainerDark: Long = 0xFF1e4d50
        override val onSecondaryContainerDark: Long = 0xFFd7e8cd

        override val tertiaryDark: Long = 0xFFa0cfd2
        override val onTertiaryDark: Long = 0xFF003739
        override val tertiaryContainerDark: Long = 0xFF1e4d50
        override val onTertiaryContainerDark: Long = 0xFFbcebee

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF1a1c18
        override val onBackgroundDark: Long = 0xFFe2e3dc
        override val surfaceDark: Long = 0xFF1a1c18
        override val onSurfaceDark: Long = 0xFFe2e3dc
        override val surfaceVariantDark: Long = 0xFF43483f
        override val onSurfaceVariantDark: Long = 0xFFc3c8bc

        override val outlineDark: Long = 0xFF8d9387
    }

    object LightBlue : ColorStyle {

        override val id: Int = Ids.LightBlue
        override val name: String = "LightBlue"
        override val type: ColorStyleType = ColorStyleType.LightBlue

        override val primaryLight: Long = 0xFF006687
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFc0e8ff
        override val onPrimaryContainerLight: Long = 0xFF001e2b

        override val secondaryLight: Long = 0xFF4d616c
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFd0e6f3
        override val onSecondaryContainerLight: Long = 0xFF091e27

        override val tertiaryLight: Long = 0xFF5f5a7d
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFe4dfff
        override val onTertiaryContainerLight: Long = 0xFF1b1736

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfbfcfe
        override val onBackgroundLight: Long = 0xFF191c1e
        override val surfaceLight: Long = 0xFFfbfcfe
        override val onSurfaceLight: Long = 0xFF191c1e
        override val surfaceVariantLight: Long = 0xFFdce3e9
        override val onSurfaceVariantLight: Long = 0xFF40484c

        override val outlineLight: Long = 0xFF71787d


        override val primaryDark: Long = 0xFF71d2ff
        override val onPrimaryDark: Long = 0xFF003547
        override val primaryContainerDark: Long = 0xFF004d66
        override val onPrimaryContainerDark: Long = 0xFFc0e8ff

        override val secondaryDark: Long = 0xFFb5cad6
        override val onSecondaryDark: Long = 0xFF1f333d
        override val secondaryContainerDark: Long = 0xFF364954
        override val onSecondaryContainerDark: Long = 0xFFd0e6f3

        override val tertiaryDark: Long = 0xFFc8c2ea
        override val onTertiaryDark: Long = 0xFF302c4c
        override val tertiaryContainerDark: Long = 0xFF474364
        override val onTertiaryContainerDark: Long = 0xFFe4dfff

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF191c1e
        override val onBackgroundDark: Long = 0xFFe1e2e5
        override val surfaceDark: Long = 0xFF191c1e
        override val onSurfaceDark: Long = 0xFFe1e2e5
        override val surfaceVariantDark: Long = 0xFF40484c
        override val onSurfaceVariantDark: Long = 0xFFc0c7cd

        override val outlineDark: Long = 0xFF8a9297
    }

    object Blue : ColorStyle {

        override val id: Int = Ids.Blue
        override val name: String = "Blue"
        override val type: ColorStyleType = ColorStyleType.Blue

        override val primaryLight: Long = 0xFF2f56c2
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFdce1ff
        override val onPrimaryContainerLight: Long = 0xFF00164d

        override val secondaryLight: Long = 0xFF595e72
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFdee1f9
        override val onSecondaryContainerLight: Long = 0xFF161b2c

        override val tertiaryLight: Long = 0xFF745470
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFffd7f6
        override val onTertiaryContainerLight: Long = 0xFF2c122a

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfefbff
        override val onBackgroundLight: Long = 0xFF1b1b1f
        override val surfaceLight: Long = 0xFFfefbff
        override val onSurfaceLight: Long = 0xFF1b1b1f
        override val surfaceVariantLight: Long = 0xFFe2e1ec
        override val onSurfaceVariantLight: Long = 0xFF45464f

        override val outlineLight: Long = 0xFF767680


        override val primaryDark: Long = 0xFFb5c4ff
        override val onPrimaryDark: Long = 0xFF00287b
        override val primaryContainerDark: Long = 0xFF073daa
        override val onPrimaryContainerDark: Long = 0xFFdce1ff

        override val secondaryDark: Long = 0xFFc1c5dd
        override val onSecondaryDark: Long = 0xFF2b3042
        override val secondaryContainerDark: Long = 0xFF414659
        override val onSecondaryContainerDark: Long = 0xFFdee1f9

        override val tertiaryDark: Long = 0xFFe3badb
        override val onTertiaryDark: Long = 0xFF432740
        override val tertiaryContainerDark: Long = 0xFF5b3d57
        override val onTertiaryContainerDark: Long = 0xFFffd7f6

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF1b1b1f
        override val onBackgroundDark: Long = 0xFFe4e2e6
        override val surfaceDark: Long = 0xFF1b1b1f
        override val onSurfaceDark: Long = 0xFFe4e2e6
        override val surfaceVariantDark: Long = 0xFF45464f
        override val onSurfaceVariantDark: Long = 0xFFc6c6d0

        override val outlineDark: Long = 0xFF8f909a
    }

    object Purple : ColorStyle {

        override val id: Int = Ids.Purple
        override val name: String = "Purple"
        override val type: ColorStyleType = ColorStyleType.Purple

        override val primaryLight: Long = 0xFF8234c6
        override val onPrimaryLight: Long = 0xFFffffff
        override val primaryContainerLight: Long = 0xFFf1dbff
        override val onPrimaryContainerLight: Long = 0xFF2d0050

        override val secondaryLight: Long = 0xFF665a6f
        override val onSecondaryLight: Long = 0xFFffffff
        override val secondaryContainerLight: Long = 0xFFedddf6
        override val onSecondaryContainerLight: Long = 0xFF211829

        override val tertiaryLight: Long = 0xFF805157
        override val onTertiaryLight: Long = 0xFFffffff
        override val tertiaryContainerLight: Long = 0xFFffd9dc
        override val onTertiaryContainerLight: Long = 0xFF321016

        override val errorLight: Long = 0xFFba1a1a
        override val onErrorLight: Long = 0xFFffffff
        override val errorContainerLight: Long = 0xFFffdad6
        override val onErrorContainerLight: Long = 0xFF410002

        override val backgroundLight: Long = 0xFFfffbff
        override val onBackgroundLight: Long = 0xFF1d1b1e
        override val surfaceLight: Long = 0xFFfffbff
        override val onSurfaceLight: Long = 0xFF1d1b1e
        override val surfaceVariantLight: Long = 0xFFe9dfea
        override val onSurfaceVariantLight: Long = 0xFF4a454d

        override val outlineLight: Long = 0xFF7c757e


        override val primaryDark: Long = 0xFFdeb7ff
        override val onPrimaryDark: Long = 0xFF4a007f
        override val primaryContainerDark: Long = 0xFF680eac
        override val onPrimaryContainerDark: Long = 0xFFf1dbff

        override val secondaryDark: Long = 0xFFd1c1d9
        override val onSecondaryDark: Long = 0xFF372c3f
        override val secondaryContainerDark: Long = 0xFF4e4256
        override val onSecondaryContainerDark: Long = 0xFFedddf6

        override val tertiaryDark: Long = 0xFFf3b7bd
        override val onTertiaryDark: Long = 0xFF4b252a
        override val tertiaryContainerDark: Long = 0xFF653a40
        override val onTertiaryContainerDark: Long = 0xFFffd9dc

        override val errorDark: Long = 0xFFffb4ab
        override val onErrorDark: Long = 0xFF690005
        override val errorContainerDark: Long = 0xFF93000a
        override val onErrorContainerDark: Long = 0xFFffdad6

        override val backgroundDark: Long = 0xFF1d1b1e
        override val onBackgroundDark: Long = 0xFFe7e1e5
        override val surfaceDark: Long = 0xFF1d1b1e
        override val onSurfaceDark: Long = 0xFFe7e1e5
        override val surfaceVariantDark: Long = 0xFF4a454d
        override val onSurfaceVariantDark: Long = 0xFFcdc4ce

        override val outlineDark: Long = 0xFF968e98
    }
}