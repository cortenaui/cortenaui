# Container

A box with a shape, fill, shadows, and padding. The static building block for custom surfaces.

!!! warning "Experimental"

    `Container` is part of the primitive layer and needs
    `@OptIn(ExperimentalComponentsApi::class)`. Its API may change before it is marked stable.

## Concept

`Container` draws one surface: a drop shadow, a fill, and an inner shadow, all in the same
`shape`, then lays out its content inside `padding`. Use it for cards, panels, wells, and any
custom component that needs a background. For a surface that reacts to touch, use
[Pressable](Pressable.md), which adds press physics on top of the same surface.

Content is not clipped to `shape`, so children that grow while pressed (such as a `Button`) are
never cut off at the edge. Add `Modifier.clip(shape)` to the content when you need clipping, for
example for an image in a rounded card.

The inner shadow is drawn over the content, like light falling into a recessed well.

Pass `glass` to turn the fill into liquid glass, with `color` as its tint. It blurs the content
behind only inside a `GlassLayer` (an AppBar is one); elsewhere it draws a 90% fill plus a rim. See
[Glass](../extra/Glass.md).

## API Reference

```kotlin
@ExperimentalComponentsApi
@Composable
fun Container(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = Color.Unspecified,
    shadow: Shadow? = null,
    innerShadow: InnerShadow? = null,
    glass: Glass? = null,
    padding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit = {},
)
```

### Parameters

| Name               | Type                              | Default             | Description                                                     |
| ------------------ | --------------------------------- | ------------------- | --------------------------------------------------------------- |
| `modifier`         | `Modifier`                        | `Modifier`          | Applied outside the shadow, so size modifiers set surface size. |
| `shape`            | `Shape`                           | `RectangleShape`    | Outline shared by the fill and both shadows.                    |
| `color`            | `Color`                           | `Color.Unspecified` | Fill color. Unspecified draws no fill.                          |
| `shadow`           | `Shadow?`                         | `null`              | Drop shadow outside `shape`.                                    |
| `innerShadow`      | `InnerShadow?`                    | `null`              | Shadow inside `shape`, drawn over the content.                  |
| `glass`            | `Glass?`                          | `null`              | Liquid glass under the content, tinted by `color`.              |
| `padding`          | `PaddingValues`                   | `PaddingValues()`   | Space between the surface edge and the content.                 |
| `contentAlignment` | `Alignment`                       | `TopStart`          | Alignment of the content inside the padding.                    |
| `content`          | `@Composable BoxScope.() -> Unit` | `{}`                | Content, laid out like a `Box`.                                 |

## Examples

A card:

```kotlin
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun Card(content: @Composable BoxScope.() -> Unit) {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    Container(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedShape(spacing.Lg.dp),
        color = Color(colors.surface),
        shadow = Shadow.Default,
        padding = PaddingValues(spacing.Md.dp),
        content = content,
    )
}
```

A recessed well:

```kotlin
Container(
    shape = RoundedShape(spacing.Lg.dp),
    color = Color(colors.surfaceVariant),
    innerShadow = InnerShadow.Default,
    padding = PaddingValues(spacing.Md.dp),
) {
    Text("Inset")
}
```
