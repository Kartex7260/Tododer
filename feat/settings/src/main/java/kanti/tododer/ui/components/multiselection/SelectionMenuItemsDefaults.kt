package kanti.tododer.ui.components.multiselection

object SelectionMenuItemsDefaults {

	fun strings(
		selectionStyle: String = "Multi selection style",
		colorFill: String = "Color filling",
		checkbox: String = "Checkbox",
		setSelectionStyle: String = "Set multi selection style",
		close: String = "Close"
	): SelectionMenuStrings = SelectionMenuStrings(
		selectionStyle = selectionStyle,
		colorFill = colorFill,
		checkbox = checkbox,
		setSelectionStyle = setSelectionStyle,
		close = close
	)
}