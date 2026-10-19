# Interaction

The touch physics behind `Button`, `Slider`, and `Toggle`, exposed so custom components can feel
the same. Everything lives in `framework.cortena.ui.interaction` in the `ui` artifact.

## Concept

Every touchable CortenaUI component is built from the same four pieces:

- **`InteractiveHighlight`** drives tap surfaces such as `Button`. It tracks the press and the
  finger, and draws a tint plus a glow that follows the finger.
- **`DampedAnimation`** drives draggable values such as the `Slider` thumb and the `Toggle` knob.
  It runs spring tracks for the value, its velocity, the press progress, and the press scale.
- **`applyInteractiveAnimation`** turns either source into motion inside `graphicsLayer`: the
  element grows while pressed, follows the finger with a soft pull, and stretches along the drag.
- **`inspectDragGestures`** is the gesture detector under both. It reports the press immediately
  instead of waiting for touch slop.

All per-frame values are plain `Float` and `Offset` reads backed by Compose state. Read them inside
`graphicsLayer` or a draw block so the animation never recomposes.

The springs inside `DampedAnimation` and `InteractiveHighlight` are tuned per track and do not read
`LocalMotion`. This is the one documented exception to the [Motion](Motion.md) rule.

## API Reference

```kotlin
expect class InteractiveHighlight(
    animationScope: CoroutineScope,
    color: Color = Color.Unspecified,
    position: (size: Size, offset: Offset) -> Offset = { _, offset -> offset },
) {
    val pressProgress: Float
    val offset: Offset
    val modifier: Modifier
    val gestureModifier: Modifier
}

class DampedAnimation(
    animationScope: CoroutineScope,
    val initialValue: Float,
    val valueRange: ClosedRange<Float>,
    val visibilityThreshold: Float,
    val initialScale: Float,
    val pressedScale: Float,
    val onDragStarted: DampedAnimation.(position: Offset) -> Unit,
    val onDragStopped: DampedAnimation.() -> Unit,
    val onDrag: DampedAnimation.(size: IntSize, dragAmount: Offset) -> Unit,
) {
    val value: Float
    val progress: Float
    val targetValue: Float
    val pressProgress: Float
    val scaleX: Float
    val scaleY: Float
    val velocity: Float
    val modifier: Modifier

    fun press()
    fun release()
    fun updateValue(value: Float)
}

fun GraphicsLayerScope.applyInteractiveAnimation(
    pressProgress: Float,
    offset: Offset,
    baseTranslationX: Float = 0f,
    baseTranslationY: Float = 0f,
    scaleXMultiplier: Float = 1f,
    scaleYMultiplier: Float = 1f,
)

suspend fun PointerInputScope.inspectDragGestures(
    onDragStart: (down: PointerInputChange) -> Unit = {},
    onDragEnd: (change: PointerInputChange) -> Unit = {},
    onDragCancel: () -> Unit = {},
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
)
```

Create `InteractiveHighlight` and `DampedAnimation` in `remember(animationScope)` with a scope
from `rememberCoroutineScope()`. The scope carries the frame clock the springs need.

### `InteractiveHighlight`

| Parameter / member | Type                       | Description                                                                                                                  |
| ------------------ | -------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| `animationScope`   | `CoroutineScope`           | Runs the animations.                                                                                                         |
| `color`            | `Color`                    | Surface color under the highlight. Dark colors get a lighter highlight, very light colors a darker one. Unspecified is gray. |
| `position`         | `(Size, Offset) -> Offset` | Maps the element size and the pointer position to the glow center. Defaults to the pointer position.                         |
| `pressProgress`    | `Float`                    | `0` at rest, `1` while pressed.                                                                                              |
| `offset`           | `Offset`                   | Drag distance from the press origin in pixels.                                                                               |
| `gestureModifier`  | `Modifier`                 | Tracks presses and drags. Leave it off while disabled.                                                                       |
| `modifier`         | `Modifier`                 | Draws the tint and the glow. Place it after the clip so the glow stays inside the shape.                                     |

On Android the glow is an AGSL `RuntimeShader`.

### `DampedAnimation`

| Parameter / member    | Type                        | Description                                                                                              |
| --------------------- | --------------------------- | -------------------------------------------------------------------------------------------------------- |
| `initialValue`        | `Float`                     | Starting value, inside `valueRange`.                                                                     |
| `valueRange`          | `ClosedRange<Float>`        | Range that `updateValue` clamps to and that `progress` maps to `0..1`.                                   |
| `visibilityThreshold` | `Float`                     | Smallest value change worth animating.                                                                   |
| `initialScale`        | `Float`                     | Scale at rest.                                                                                           |
| `pressedScale`        | `Float`                     | Scale while pressed.                                                                                     |
| `onDragStarted`       | `(Offset) -> Unit`          | Called by `modifier` when a press starts, with the touch position.                                       |
| `onDragStopped`       | `() -> Unit`                | Called by `modifier` when the press ends or is canceled.                                                 |
| `onDrag`              | `(IntSize, Offset) -> Unit` | Called by `modifier` for every move, with the size of the element that carries `modifier` and the delta. |
| `value`               | `Float`                     | Current animated value.                                                                                  |
| `progress`            | `Float`                     | `value` as a fraction of `valueRange`.                                                                   |
| `targetValue`         | `Float`                     | Value the spring is heading to.                                                                          |
| `pressProgress`       | `Float`                     | `0` at rest, `1` while pressed.                                                                          |
| `scaleX`, `scaleY`    | `Float`                     | Press scale per axis. The axes settle at different rates, which gives the squash.                        |
| `velocity`            | `Float`                     | Smoothed speed of `value` in range widths per second.                                                    |
| `modifier`            | `Modifier`                  | Optional pointer input that calls the three callbacks and runs `press` and `release`.                    |
| `press()`             | —                           | Animates `pressProgress` to `1` and the scales to `pressedScale`.                                        |
| `release()`           | —                           | Animates back to rest, after `value` is within 2.5% of the range from its target.                        |
| `updateValue(value)`  | —                           | Springs toward `value`, clamped to `valueRange`.                                                         |

The callbacks are captured once. Read changing values inside them through `rememberUpdatedState`.

### `applyInteractiveAnimation`

| Parameter                              | Type     | Description                                                                            |
| -------------------------------------- | -------- | -------------------------------------------------------------------------------------- |
| `pressProgress`                        | `Float`  | `0` at rest, `1` fully pressed. The element grows by 4dp of its height at `1`.         |
| `offset`                               | `Offset` | Drag distance from the press origin in pixels. `Offset.Zero` gives no pull or stretch. |
| `baseTranslationX`, `baseTranslationY` | `Float`  | Translation in pixels that the pull is added to, for elements that already move.       |
| `scaleXMultiplier`, `scaleYMultiplier` | `Float`  | Extra scale per axis, such as `DampedAnimation.scaleX` and `scaleY`.                   |

### `inspectDragGestures`

`onDragStart` fires on the first down. `onDrag` then fires once with `Offset.Zero` and again for
every move, and `onDragEnd` fires when the last pointer lifts. If the pointer is lost or another
handler consumes a move first, `onDragCancel` fires instead. Every move reported to `onDrag` is
consumed.

## Examples

A tappable chip with the same press feel as `Button`:

```kotlin
@Composable
fun Chip(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    val background = Color(colors.surfaceVariant)
    val animationScope = rememberCoroutineScope()
    val highlight =
        remember(animationScope) {
            InteractiveHighlight(animationScope = animationScope, color = background)
        }

    Box(
        modifier
            .graphicsLayer {
                applyInteractiveAnimation(
                    pressProgress = highlight.pressProgress,
                    offset = highlight.offset,
                )
            }
            .clip(CapsuleShape())
            .background(background)
            .then(highlight.gestureModifier)
            .clickable(
                interactionSource = null,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .then(highlight.modifier)
            .padding(horizontal = spacing.Md.dp, vertical = spacing.Sm.dp)
    ) {
        Text(label)
    }
}
```

A knob that springs along a track and snaps to the nearest end on release:

```kotlin
@Composable
fun SnapKnob(modifier: Modifier = Modifier) {
    val colors = LocalColors.current
    val size = LocalSizeToken.current
    val travel = size.toggleTrackWidth - size.toggleTrackHeight
    val animationScope = rememberCoroutineScope()
    val knob =
        remember(animationScope) {
            DampedAnimation(
                animationScope = animationScope,
                initialValue = 0f,
                valueRange = 0f..1f,
                visibilityThreshold = 0.001f,
                initialScale = 1f,
                pressedScale = 1.3f,
                onDragStarted = {},
                onDragStopped = { updateValue(if (targetValue > 0.5f) 1f else 0f) },
                onDrag = { trackSize, dragAmount ->
                    updateValue(targetValue + dragAmount.x / trackSize.width)
                },
            )
        }

    // The track carries knob.modifier, so onDrag receives the track size. LTR only, for brevity.
    Box(
        modifier
            .size(size.toggleTrackWidth, size.toggleTrackHeight)
            .background(Color(colors.surfaceVariant), CapsuleShape())
            .then(knob.modifier)
    ) {
        Box(
            Modifier.size(size.toggleTrackHeight)
                .graphicsLayer {
                    applyInteractiveAnimation(
                        pressProgress = knob.pressProgress,
                        offset = Offset.Zero,
                        baseTranslationX = knob.progress * travel.toPx(),
                        scaleXMultiplier = knob.scaleX,
                        scaleYMultiplier = knob.scaleY,
                    )
                }
                .background(Color(colors.primary), CapsuleShape())
        )
    }
}
```
