# Pressable

A [Container](Container.md) that responds to touch with CortenaUI's press physics and highlight.

!!! warning "Experimental"

    `Pressable` is part of the primitive layer and needs
    `@OptIn(ExperimentalComponentsApi::class)`. Its API may change before it is marked stable.

## Concept

Every touchable CortenaUI component starts from `Pressable`. `Button` is a `Pressable` with a
capsule shape, a style color, and a `Row` of content.

`Pressable` draws the same surface as `Container`, then wires the interaction pipeline from
[Interaction](../extra/Interaction.md):

- While pressed, the surface grows, follows the finger, and stretches along the drag
  (`applyInteractiveAnimation`). Set `interactive = false` to keep the highlight but skip this
  motion.
- A tint and a glow track the touch inside `shape` (`InteractiveHighlight`). The highlight is tuned
  once, from the first `color`: dark fills get a lighter highlight, very light fills a darker one.
- Taps call `onClick`, long presses call `onLongClick`, and the surface is announced with `role`.

Unlike `Container`, content is clipped to `shape`, so the glow stays inside the outline.

When `enabled` is `false`, the surface renders at 38% alpha and ignores touches.

Pass `glass` to turn the fill into liquid glass, as `Button` does. See [Glass](../extra/Glass.md).

## API Reference

```kotlin
@ExperimentalComponentsApi
@Composable
fun Pressable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    interactive: Boolean = true,
    role: Role = Role.Button,
    shape: Shape = RectangleShape,
    color: Color = Color.Unspecified,
    shadow: Shadow? = null,
    innerShadow: InnerShadow? = null,
    glass: Glass? = null,
    padding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
)
```

### Parameters

| Name               | Type                              | Default             | Description                                                                  |
| ------------------ | --------------------------------- | ------------------- | ---------------------------------------------------------------------------- |
| `onClick`          | `() -> Unit`                      | —                   | Called on tap.                                                               |
| `modifier`         | `Modifier`                        | `Modifier`          | Applied outside the press motion and the shadow.                             |
| `enabled`          | `Boolean`                         | `true`              | When `false`, touches are ignored and alpha drops to 38%.                    |
| `onLongClick`      | `(() -> Unit)?`                   | `null`              | Called on long press. `null` treats a long press as a tap.                   |
| `interactive`      | `Boolean`                         | `true`              | When `false`, keeps the highlight but skips the press motion.                |
| `role`             | `Role`                            | `Role.Button`       | Accessibility role announced for the surface.                                |
| `shape`            | `Shape`                           | `RectangleShape`    | Outline for the fill, shadows, clip, and highlight.                          |
| `color`            | `Color`                           | `Color.Unspecified` | Fill color, also used to tune the highlight. Unspecified: none.              |
| `shadow`           | `Shadow?`                         | `null`              | Drop shadow outside `shape`. Floating glass: see [Glass](../extra/Glass.md). |
| `innerShadow`      | `InnerShadow?`                    | `null`              | Shadow inside `shape`, drawn over the content.                               |
| `glass`            | `Glass?`                          | `null`              | Liquid glass under the content, tinted by `color`.                           |
| `padding`          | `PaddingValues`                   | `PaddingValues()`   | Space between the surface edge and the content.                              |
| `contentAlignment` | `Alignment`                       | `TopStart`          | Alignment of the content inside the padding.                                 |
| `content`          | `@Composable BoxScope.() -> Unit` | —                   | Content, laid out like a `Box`.                                              |

## Examples

A tappable tile:

```kotlin
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun Tile(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    Pressable(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedShape(spacing.Lg.dp),
        color = Color(colors.surfaceVariant),
        padding = PaddingValues(spacing.Md.dp),
    ) {
        Text(label)
    }
}
```

A chip that announces itself as a checkbox:

```kotlin
Pressable(
    onClick = { selected = !selected },
    modifier = Modifier.semantics { toggleableState = ToggleableState(selected) },
    role = Role.Checkbox,
    shape = CapsuleShape(),
    color = if (selected) Color(colors.primary) else Color(colors.surfaceVariant),
    padding = PaddingValues(horizontal = spacing.Md.dp, vertical = spacing.Sm.dp),
) {
    Text("Filter")
}
```

Disabled:

```kotlin
Pressable(onClick = {}, enabled = false, color = Color(colors.surfaceVariant)) {
    Text("Unavailable")
}
```
