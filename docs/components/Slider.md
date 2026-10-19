# Slider

`Slider` is Cortena's value adjustment component.

## Concept

The component is a capsule with:

- A filled background segment that represents the current progress.
- A white pill indicator by default, with a subtle shadow for contrast on light surfaces.
- Button-like interactive feedback on the indicator: press glow, drag offset, and stretch.

The component is controlled. You own the state and pass the current value back through `onValueChange`.

`Slider` has one size, the Small tier of the size scale, and ignores `LocalSizeToken`. It is built on the [DragTrack](../primitives/DragTrack.md) primitive.

## API Reference

```kotlin
@Composable
fun Slider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    steps: Int = 0,
    indicatorColor: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = Color.Unspecified
)
```

### Parameters

| Name             | Data Type                         | Description                                                                                                        |
| ---------------- | --------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| `value`          | `() -> Float`                     | Current slider value.                                                                                              |
| `onValueChange`  | `(Float) -> Unit`                 | Called with a coerced value while dragging.                                                                        |
| `valueRange`     | `ClosedFloatingPointRange<Float>` | Minimum and maximum allowed values.                                                                                |
| `modifier`       | `Modifier`                        | Standard Compose modifier.                                                                                         |
| `enabled`        | `Boolean`                         | Disables drag and gesture effects when `false`.                                                                    |
| `steps`          | `Int`                             | Number of discrete steps (tick marks) evenly distributed across the track. If 0, behaves continuously. Default: 0. |
| `indicatorColor` | `Color`                           | Indicator pill color. Default: white.                                                                              |
| `trackColor`     | `Color`                           | Track color. Default: surface variant.                                                                             |
| `progressColor`  | `Color`                           | Filled progress color. Default: primary.                                                                           |

### Example

```kotlin
var ratio by rememberSaveable { mutableFloatStateOf(0.5f) }

Slider(
    value = { ratio },
    onValueChange = { ratio = it },
    valueRange = 0f..1f,
)
```

### Custom Colors

```kotlin
var ratio by rememberSaveable { mutableFloatStateOf(0.5f) }

Slider(
    value = { ratio },
    onValueChange = { ratio = it },
    valueRange = 0f..1f,
    indicatorColor = Color.White,
    trackColor = Color(LocalColors.current.surface),
    progressColor = Color(LocalColors.current.primary).copy(alpha = 0.24f),
)
```

### Discrete Slider

If `steps > 0`, the slider behaves discretely, jumping to specific values and rendering visual tick marks.

```kotlin
var discreteValue by rememberSaveable { mutableFloatStateOf(0f) }

Slider(
    value = { discreteValue },
    onValueChange = { discreteValue = it },
    valueRange = -4f..4f,
    steps = 7, // Creates 8 intervals, snapping exactly to integers like -4, -3, ... 4
)
```
