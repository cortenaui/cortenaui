# Toggle

`Toggle` is Cortena's switch/toggle adjustment component, based on an interactive design with physics-based spring response.

## Concept

The component is a custom pill-shaped switch featuring:

- A sliding capsule thumb that scales up dynamically (spring animation) when pressed, clicked, or dragged.
- Smooth color transition of the track background between the inactive and active states.
- Double-gesture support: can be clicked/tapped directly to switch states, or dragged horizontally to slide the thumb.
- Proper accessibility semantics (`Role.Switch`).
- Fully unclipped drop shadows around the thumb that scale and follow the animation path correctly.

`Toggle` has one size, the Medium tier of the size scale, and ignores `LocalSizeToken`. It is built on the [DragTrack](../primitives/DragTrack.md) primitive.

## API Reference

```kotlin
@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    activeColor: Color = Color.Unspecified,
    inactiveColor: Color = Color.Unspecified,
    thumbColor: Color = Color.Unspecified,
)
```

### Parameters

| Name              | Data Type              | Description                                                                         |
| ----------------- | ---------------------- | ----------------------------------------------------------------------------------- |
| `checked`         | `Boolean`              | Whether the toggle is active (checked) or inactive (unchecked).                     |
| `onCheckedChange` | `((Boolean) -> Unit)?` | Called when the user clicks or drags the toggle to change its state.                |
| `modifier`        | `Modifier`             | Standard Compose modifier.                                                          |
| `enabled`         | `Boolean`              | Disables clicks, gestures, and dims the toggle visually with an alpha when `false`. |
| `activeColor`     | `Color`                | Track background color when active (`checked = true`). Default: success.            |
| `inactiveColor`   | `Color`                | Track background color when inactive (`checked = false`). Default: outline variant. |
| `thumbColor`      | `Color`                | Color of the sliding inner thumb capsule. Default: white.                           |

### Example

```kotlin
var isEnabled by rememberSaveable { mutableStateOf(false) }

Toggle(
    checked = isEnabled,
    onCheckedChange = { isEnabled = it }
)
```

### Custom Colors

```kotlin
var isPremium by rememberSaveable { mutableStateOf(true) }

Toggle(
    checked = isPremium,
    onCheckedChange = { isPremium = it },
    activeColor = Color(LocalColors.current.accent),
    inactiveColor = Color(LocalColors.current.surfaceVariant),
    thumbColor = Color.White
)
```

### Disabled State

```kotlin
Toggle(
    checked = true,
    onCheckedChange = null, // Or pass a callback with `enabled = false`
    enabled = false
)
```
