package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.EmptyCoroutineContext.get

internal const val colorKey = "color: "
internal const val opacityKey = "opacity: "

/**
 * alias for colors
 */
typealias ColorProperty = Property

val ColorProperty.hover
    get() = alterBrightness(this, Theme().hoverBrightness)

val ColorProperty.darken
    get() = alterBrightness(this, Theme().hoverDarkness)

/**
 * creates a [ColorProperty] from rgb-values
 */
fun rgb(r: Int, g: Int, b: Int) = "rgb($r,$g,$b)"

/**
 * creates a [ColorProperty] from rgba-values
 */
fun rgba(r: Int, g: Int, b: Int, a: Double) = "rgb($r,$g,$b,$a)"

/**
 * creates a [ColorProperty] from hsl-values
 */
fun hsl(h: Int, s: Int, l: Int) = "hsl($h,$s%,$l%)"

/**
 * creates a [ColorProperty] from hsla-values
 */
fun hsla(h: Int, s: Int, l: Int, a: Double) = "hsl($h,$s% c vn,,$l%,$a)"

/**
 * alters the brightness of a given input color in the hex format.
 * Enter a value between 1 and 2 to increase brightness, and a value between 0 and 1 to decrease brightness.
 * Increasing the brightness of a color lets them appear rather faded than shining.
 */
fun alterBrightness(color: ColorProperty, brightness: Double): ColorProperty {
    if (color.length != 7 || color[0] != '#') {
        console.log("wrong color input format")
    }
    val r: Long = color.subSequence(1,3).toString().toLong(16)
    val g: Long = color.subSequence(3,5).toString().toLong(16)
    val b: Long = color.subSequence(5,7).toString().toLong(16)

    val rgb = longArrayOf(r,g,b)
    val res = arrayOf("1", "2", "3")

    for (i: Int in 0..2) {
        var newCalc: Double
        if (brightness > 1) {
            newCalc = rgb[i] + ((brightness-1) * ((255-rgb[i])))
        } else if (brightness < 1) {
            newCalc = rgb[i] - ((1-brightness) * (rgb[i]))
        } else return color

        var new: Int = newCalc.toInt()
        if (new > 255) { new = 255 }
        res[i] = new.toString(16)
        if (res[i].length == 1) { res[i] = "0" + res[i] }
    }
    return "#${res[0]}${res[1]}${res[2]}"
}


/**
 * This _context_ interface offers functions to style the color related CSS properties of a component.
 *
 * It only offers two functions
 * - [color] for setting the color, and
 * - [opacity] for setting the opacity of a component.
 *
 * Both functions have two variants, one for setting the property for all media devices at once and another for
 * setting the properties for each media device independently.
 */
@ExperimentalCoroutinesApi
interface Color : StyleParams {

    /**
     * This function sets the [color](https://developer.mozilla.org/en/docs/Web/CSS/color) property
     * for all media devices.
     *
     * Example call:
     * ```
     * color { primary } // use the predefined values from the theme (by [dev.fritz2.styling.theme.Theme.colors])
     * // color { "lime" } // we don't provide common CSS colors at the moment, you must provide them individually
     * // color { rgba(255, 0, 0, 100) }
     * ```
     *
     * @param value extension function parameter with color type return value,
     *              recommended to use predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the
     *              properties of [Colors]
     */
    fun color(value: Colors.() -> ColorProperty) = property(colorKey, Theme().colors, value)

    /**
     * This function sets the [color](https://developer.mozilla.org/en/docs/Web/CSS/color) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * color(
     *     sm = { primary }
     *     lg = { dark }
     * )
     * ```
     *
     * @param sm extension function parameter with color type return value for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     * @param md extension function parameter with color type return value for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     * @param lg extension function parameter with color type return value for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     * @param xl extension function parameter with color type return value for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     */
    fun color(
        sm: (Colors.() -> ColorProperty)? = null,
        md: (Colors.() -> ColorProperty)? = null,
        lg: (Colors.() -> ColorProperty)? = null,
        xl: (Colors.() -> ColorProperty)? = null
    ) =
        property(colorKey, Theme().colors, sm, md, lg, xl)


    /**
     * This function sets the [opacity](https://developer.mozilla.org/en/docs/Web/CSS/opacity) property
     * for all media devices.
     *
     * Example call:
     * ```
     * opacity { normal } // prefer the predefined values from the theme (by [dev.fritz2.styling.theme.Theme.opacities])
     * // opacity { "0.1" }
     * ```
     *
     * @param value provide a value of type [WeightedValueProperty] that defines the opacity,
     *              recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     */
    fun opacity(value: WeightedValueProperty) = property(opacityKey, Theme().opacities, value)

    /**
     * This function sets the [opacity](https://developer.mozilla.org/en/docs/Web/CSS/opacity) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * opacity(
     *     sm = { normal }
     *     lg = { "0.8" }
     * )
     * ```
     *
     * @param sm extension function parameter with a [WeightedValueProperty] type return value for small media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     * @param md extension function parameter with a [WeightedValueProperty] type return value for medium sized media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     * @param lg extension function parameter with a [WeightedValueProperty] type return value for large media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     * @param xl extension function parameter with a [WeightedValueProperty] type return value for extra large media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     */
    fun opacity(
        sm: WeightedValueProperty? = null,
        md: WeightedValueProperty? = null,
        lg: WeightedValueProperty? = null,
        xl: WeightedValueProperty? = null
    ) =
        property(opacityKey, Theme().opacities, sm, md, lg, xl)
}