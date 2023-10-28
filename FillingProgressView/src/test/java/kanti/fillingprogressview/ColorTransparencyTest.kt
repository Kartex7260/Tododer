package kanti.fillingprogressview

import android.graphics.Color
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

class ColorTransparencyTest {

	private val basicColor = Color.WHITE
	private val color050 = Color.argb(0.5f, 1f, 1f, 1f)
	private val color000 = Color.argb(0f, 1f, 1f, 1f)

	@Test
	fun testTransparency000() {
		val colorTransparency = ColorTransparency(basicColor)
		val newColor = colorTransparency.transparent(Progress.MIN_VALUE)
		Assert.assertEquals(color000, newColor)
	}

	@Test
	fun testTransparency050() {
		val colorTransparency = ColorTransparency(basicColor)
		val newColor = colorTransparency.transparent(0.5f)
		Assert.assertEquals(color050, newColor)
	}

	@Test
	fun testTransparency100() {
		val colorTransparency = ColorTransparency(basicColor)
		val newColor = colorTransparency.transparent(Progress.MAX_VALUE)
		Assert.assertEquals(basicColor, newColor)
	}

}