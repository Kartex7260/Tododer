package kanti.tododer.ui.components.colorstyle

object ChangeColorStyleItemDefaults {

    fun strings(
        colorStyle: String = "Color style",
        standard: String = "Tododer",
        dynamic: String = "As system",
        red: String = "Red",
        orange: String = "Orange",
        yellow: String = "Yellow",
        green: String = "Green",
        lightBlue: String = "Light blue",
        blue: String = "Blue",
        purple: String = "Purple"
    ): ChangeColorStyleStrings = ChangeColorStyleStrings(
        colorStyle = colorStyle,
        standard = standard,
        dynamic = dynamic,
        red = red,
        orange = orange,
        yellow = yellow,
        green = green,
        lightBlue = lightBlue,
        blue = blue,
        purple = purple
    )
}