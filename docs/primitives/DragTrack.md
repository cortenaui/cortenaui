# DragTrack

A track with a thumb that slides along it, driven by a `DampedAnimation`.

!!! warning "Experimental"

    `DragTrack` is part of the primitive layer and needs
    `@OptIn(ExperimentalComponentsApi::class)`. Its API may change before it is marked stable.

## Concept

`DragTrack` is the shared base of `Slider` and `Toggle`. It lays out two slots, a `track` and a
`thumb`, and moves the thumb with the [`DampedAnimation`](../extra/Interaction.md) you pass in:

- The thumb sits at the start when `animation.progress` is `0` and at the end when it is `1`,
  inset by `thumbPadding` on both ends. Right-to-left layouts mirror the direction.
- While pressed, the thumb gets CortenaUI's press motion (`applyInteractiveAnimation`) with the
  animation's per-axis press scale, as one layer with its shadow.
- Both slots are centered vertically. The control is as wide and as tall as its largest slot, so
  a thumb taller than the track (like the `Slider` indicator) works without extra layout code.
- The thumb redraws as the value moves, without recomposing or relayouting.

`DragTrack` detects no gestures, because controls map touches to values differently: `Slider`
jumps to the touch position, while `Toggle` drags relative to the thumb and snaps on release.
Attach gestures through `modifier` or inside `track`, and drive the animation with
`updateValue`, `press`, and `release`, or attach `animation.modifier` for relative dragging.

Disabled styling also stays with the caller, since controls dim differently.

## API Reference

```kotlin
@ExperimentalComponentsApi
@Composable
fun DragTrack(
    animation: DampedAnimation,
    modifier: Modifier = Modifier,
    thumbPadding: Dp = 0.dp,
    track: @Composable () -> Unit,
    thumb: @Composable () -> Unit,
)
```

### Parameters

| Name           | Type                     | Default    | Description                                                                   |
| -------------- | ------------------------ | ---------- | ----------------------------------------------------------------------------- |
| `animation`    | `DampedAnimation`        | —          | Value and press physics for the thumb.                                        |
| `modifier`     | `Modifier`               | `Modifier` | Applied to the whole control. Size and gestures usually go here.              |
| `thumbPadding` | `Dp`                     | `0.dp`     | Gap between the thumb and each end of the track.                              |
| `track`        | `@Composable () -> Unit` | —          | Track content. Usually fills the control and draws from `animation.progress`. |
| `thumb`        | `@Composable () -> Unit` | —          | Thumb content. It moves and scales as one layer, shadow included.             |

## Examples

A level bar: drag anywhere on the bar to move the thumb.

```kotlin
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun LevelBar() {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    val height = LocalSizeToken.current.componentHeight
    val direction by
        rememberUpdatedState(if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f)
    val animationScope = rememberCoroutineScope()
    val level =
        remember(animationScope) {
            DampedAnimation(
                animationScope = animationScope,
                initialValue = 0.5f,
                valueRange = 0f..1f,
                visibilityThreshold = 0.001f,
                initialScale = 1f,
                pressedScale = 1.2f,
                onDragStarted = {},
                onDragStopped = {},
                onDrag = { size, dragAmount ->
                    updateValue(targetValue + direction * dragAmount.x / size.width)
                },
            )
        }

    DragTrack(
        animation = level,
        modifier = Modifier.fillMaxWidth().height(height).then(level.modifier),
        thumbPadding = spacing.Xs.dp,
        track = {
            Container(
                modifier = Modifier.fillMaxSize(),
                shape = CapsuleShape(),
                color = Color(colors.surfaceVariant),
            )
        },
        thumb = {
            Container(
                modifier = Modifier.size(height - spacing.Xs.dp * 2),
                shape = CapsuleShape(),
                color = Color(colors.primary),
                shadow = Shadow.Default,
            )
        },
    )
}
```

A thin track that draws its own fill under a taller thumb (LTR only, for brevity):

```kotlin
DragTrack(
    animation = level,
    modifier = Modifier.fillMaxWidth().then(level.modifier),
    track = {
        Box(
            Modifier.fillMaxWidth().height(spacing.Sm.dp).clip(CapsuleShape()).drawBehind {
                drawRect(Color(colors.surfaceVariant))
                drawRect(Color(colors.primary), size = size.copy(width = size.width * level.progress))
            }
        )
    },
    thumb = {
        Container(
            modifier = Modifier.size(spacing.Lg.dp),
            shape = CapsuleShape(),
            color = Color(colors.surface),
            shadow = Shadow.Default,
        )
    },
)
```
