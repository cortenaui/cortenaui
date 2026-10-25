# Glass

Liquid glass for surfaces that float over content: the content behind is saturated, blurred, and
bent along the edge like the rim of a lens. Everything lives in
`framework.cortena.ui.graphics.glass` in the `ui` artifact.

## Concept

You rarely touch glass directly. `Button` and the AppBar back button use it by default, and
[Container](../primitives/Container.md) and [Pressable](../primitives/Pressable.md) take a `glass`
parameter for custom surfaces.

Glass needs something behind it to blur. A `GlassLayer` records its `background`, and every glass
surface in its `content` draws that recording, blurred, inside its own shape. Every `ContentView`
page is a `GlassLayer`: the page is the background and the AppBar floats over it, so buttons in
the AppBar blur whatever scrolls under them.

In light mode, glass floating over a `GlassLayer` also casts `Shadow.Default`, so it lifts off the
page. A `shadow` passed to `Container` or `Pressable` replaces it.

A glass surface inside the recorded content itself, such as a `Button` in the page, cannot sample a
layer it is part of. It draws its fill at 90% of its alpha and the rim, without the blur.

Over recorded content, the surface color becomes a tint at 60% of its alpha, so the content behind
shows through. `Color.Unspecified` gives clear glass.

### The recipe

The defaults of `Glass` are CortenaUI's tuned recipe, applied in this order:

1. Vibrancy: saturation × 1.5, to bring back the color the blur washes out.
2. Blur: `blurRadius`, 0.0625 of the shorter side (16 dp on a 256 dp surface).
3. Refraction: along the edge, a band `refractionHeight` wide (0.20 of half the shorter side) bends
   the content by `refractionAmount` (0.32 of the shorter side).
4. Rim: 0.5 dp in a lighter shade of the tint, half as opaque as the tint, so it blends with the
   surface. Clear glass gets white at 38%.

Every value scales with the surface, so the same recipe fits a small button and a large card: a
small button blurs lightly and keeps its bent edge visible. Shapes from `framework.cortena.ui.shape`
bend along their corner radii. Other shapes get the blur and the rim but no refraction.

### Cost

- The background is drawn from its recording instead of twice, so a page pays no extra pass.
- Each glass surface adds one layer with a blur and one shader, only over its own area. The shader
  is compiled once per process.
- A glass surface with nothing to blur allocates no layer and draws only its fill and rim.

## API Reference

```kotlin
@Immutable
data class Glass(
    val blurRadius: Float = 0.0625f,
    val refractionHeight: Float = 0.2f,
    val refractionAmount: Float = 0.32f,
)

@Composable
fun GlassLayer(
    background: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
)

fun Modifier.glass(glass: Glass, shape: Shape, color: Color = Color.Unspecified): Modifier
```

### `Glass`

| Name               | Type    | Default   | Description                          |
| ------------------ | ------- | --------- | ------------------------------------ |
| `blurRadius`       | `Float` | `0.0625f` | Blur radius, × the shorter side.     |
| `refractionHeight` | `Float` | `0.2f`    | Band width, × half the shorter side. |
| `refractionAmount` | `Float` | `0.32f`   | Band bend, × the shorter side.       |

### `GlassLayer`

| Name         | Type                              | Default    | Description                                |
| ------------ | --------------------------------- | ---------- | ------------------------------------------ |
| `background` | `@Composable () -> Unit`          | —          | Content behind the glass, recorded for it. |
| `modifier`   | `Modifier`                        | `Modifier` | Applied to the box that holds both layers. |
| `content`    | `@Composable BoxScope.() -> Unit` | —          | Glass surfaces drawn over `background`.    |

### `Modifier.glass`

| Name    | Type    | Default             | Description                          |
| ------- | ------- | ------------------- | ------------------------------------ |
| `glass` | `Glass` | —                   | Blur and refraction recipe.          |
| `shape` | `Shape` | —                   | Outline of the glass.                |
| `color` | `Color` | `Color.Unspecified` | Tint. Unspecified gives clear glass. |

## Examples

Glass in an AppBar needs nothing extra:

```kotlin
AppBar(
    title = { Text("Inbox") },
    trailing = { Button(onClick = {}, iconOnly = true) { Icon(SearchIcon, "Search") } },
)
```

Clear glass over an image:

```kotlin
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun GlassOverImage(painter: Painter) {
    val spacing = LocalSpacing.current
    GlassLayer(
        background = { Image(painter, contentDescription = null, Modifier.fillMaxWidth()) },
    ) {
        Container(
            modifier = Modifier.align(Alignment.Center).size(144.dp),
            shape = RoundedShape(spacing.Xl.dp),
            glass = Glass(),
        )
    }
}
```

A custom surface with a lighter blur:

```kotlin
Box(
    modifier =
        Modifier.size(64.dp).glass(Glass(blurRadius = 0.03f), CapsuleShape(), Color(colors.primary))
)
```
