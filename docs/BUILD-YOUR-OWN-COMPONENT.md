# Build Your Own Component

CortenaUI's own controls are built from public layers. A custom component built on the same layers
looks, moves, and responds like `Button`, `Slider`, and `Toggle`.

## Layers

| Layer          | What it gives you                                             | Pages                                                                                                            |
| -------------- | ------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| Tokens         | Colors, spacing, sizes, type, motion, shapes                  | [Theme](layout/Theme.md), [Motion](extra/Motion.md), [Shape](extra/Shape.md)                                     |
| L1 interaction | Touch physics: press, drag, highlight                         | [Interaction](extra/Interaction.md)                                                                              |
| L2 primitives  | Surfaces with the physics already wired in                    | [Container](primitives/Container.md), [Pressable](primitives/Pressable.md), [DragTrack](primitives/DragTrack.md) |
| Components     | `Button` on `Pressable`, `Slider` and `Toggle` on `DragTrack` | [Button](components/Button.md), [Slider](components/Slider.md), [Toggle](components/Toggle.md)                   |

The L1 pieces are regular public API. The L2 primitives are experimental: opt in with
`@OptIn(ExperimentalComponentsApi::class)`, and expect their API to change before it is marked
stable.

## Pick a Starting Point

| You are building                                  | Start from                           |
| ------------------------------------------------- | ------------------------------------ |
| A static surface: card, well, badge               | `Container`                          |
| Something you tap: chip, tile, list cell          | `Pressable`                          |
| A thumb that moves along a track: level, scrubber | `DragTrack` with a `DampedAnimation` |
| Anything else: two thumbs, a 2D pad, a new layout | The L1 pieces directly               |

The [Interaction](extra/Interaction.md) page builds a chip and a knob by hand from the L1 pieces.

## House Rules

The built-in components follow these rules. A custom component should too:

- **Parameters.** State first, then its callback (`null` disables interaction), then `modifier`,
  `enabled`, options, colors that default to `Color.Unspecified`, and the content slot last. A call
  with only the required arguments gives the common case.
- **Theme values.** Colors come from `LocalColors`, spacing from `LocalSpacing`, sizes from
  `LocalSizeToken`, and motion from `LocalMotion`, never raw values. Resolve a color override with
  `if (color.isSpecified) color else Color(colors.role)`.
- **Animation state.** Create `DampedAnimation` and `InteractiveHighlight` in
  `remember(animationScope)` with a scope from `rememberCoroutineScope()`. Their callbacks are
  captured once, so read changing values inside them through `rememberUpdatedState`.
- **Per-frame reads.** Read `progress`, `pressProgress`, and the other animated values inside
  `graphicsLayer` or a draw block, so the motion never recomposes.
- **Content color.** Provide `LocalContentColor` around the content slot, plus `LocalTextRole`,
  `LocalTextWeight`, or `LocalIconSize` when they matter, so `Text` and `Icon` inside match the
  surface.
- **Disabled.** Render at 38% alpha and drop the gesture modifiers. `Pressable` does both for you.
- **Accessibility.** Announce a role and the current state in `semantics {}`. A drag control also
  needs range info and a `setProgress` action, so screen readers can change it.
- **Right-to-left.** `DragTrack` mirrors the thumb position. Mirror your own touch-to-value
  mapping too.

## Example: Filter Chip

A chip you tap to select, built on `Pressable`:

```kotlin
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun FilterChip(
    selected: Boolean,
    onSelectedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalColors.current
    val motion = LocalMotion.current
    val size = LocalSizeToken.current
    val fill by
        animateColorAsState(
            targetValue = Color(if (selected) colors.primary else colors.surfaceVariant),
            animationSpec = tween(motion.medium, easing = motion.standardEasing),
        )
    val contentColor = Color(if (selected) colors.onPrimary else colors.onSurfaceVariant)

    Pressable(
        onClick = { onSelectedChange?.invoke(!selected) },
        modifier =
            modifier.height(size.componentHeight).semantics {
                toggleableState = ToggleableState(selected)
            },
        enabled = enabled && onSelectedChange != null,
        role = Role.Checkbox,
        shape = CapsuleShape(),
        color = fill,
        padding = PaddingValues(horizontal = size.horizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(size.contentGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(
                LocalContentColor provides contentColor,
                LocalTextWeight provides TextWeight.Medium,
                LocalIconSize provides size.iconSize,
            ) {
                content()
            }
        }
    }
}
```

```kotlin
var nearby by remember { mutableStateOf(false) }

FilterChip(selected = nearby, onSelectedChange = { nearby = it }) { Text("Nearby") }
```

- `Role.Checkbox` with `toggleableState` announces the chip as a checkbox that is on or off.
- The fill crossfades with a `LocalMotion` tween, because a color change is a fixed transition, not
  a gesture. `Pressable` tunes its highlight from the first color only, so the highlight keeps the
  unselected tuning.
- The chip follows `LocalSizeToken`, so `Theme(sizeToken = …)` resizes it with `Button`.

## Example: Dimmer

A bar you drag or tap to set a level from `0` to `1`, built on `DragTrack`. The caller owns the
value, as with `Slider`:

```kotlin
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun Dimmer(
    value: Float,
    onValueChange: ((Float) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    val height = LocalSizeToken.current.componentHeight
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val onChange by rememberUpdatedState(onValueChange)
    val animationScope = rememberCoroutineScope()
    val level =
        remember(animationScope) {
            DampedAnimation(
                animationScope = animationScope,
                initialValue = value,
                valueRange = 0f..1f,
                visibilityThreshold = 0.001f,
                initialScale = 1f,
                pressedScale = 1.2f,
                // Gestures are detected below, so the built-in drag modifier stays unused
                onDragStarted = {},
                onDragStopped = {},
                onDrag = { _, _ -> },
            )
        }

    // The caller owns the value, so the thumb follows it wherever the change came from
    LaunchedEffect(value) { level.updateValue(value) }

    val gestures =
        if (enabled && onValueChange != null) {
            Modifier.pointerInput(isLtr) {
                fun valueAt(x: Float): Float {
                    val fraction = (x / size.width).fastCoerceIn(0f, 1f)
                    return if (isLtr) fraction else 1f - fraction
                }
                inspectDragGestures(
                    onDragStart = { down ->
                        level.press()
                        onChange?.invoke(valueAt(down.position.x))
                    },
                    onDragEnd = { level.release() },
                    onDragCancel = { level.release() },
                ) { change, _ ->
                    onChange?.invoke(valueAt(change.position.x))
                }
            }
        } else {
            Modifier
        }

    DragTrack(
        animation = level,
        modifier =
            modifier
                .fillMaxWidth()
                .height(height)
                .semantics {
                    progressBarRangeInfo = ProgressBarRangeInfo(value, 0f..1f)
                    if (!enabled) disabled()
                    if (enabled && onValueChange != null) {
                        setProgress { target ->
                            onValueChange(target.fastCoerceIn(0f, 1f))
                            true
                        }
                    }
                }
                .then(gestures)
                .graphicsLayer { alpha = if (enabled) 1f else 0.38f },
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

```kotlin
var brightness by remember { mutableFloatStateOf(0.5f) }

Dimmer(value = brightness, onValueChange = { brightness = it })
```

- A touch maps to an absolute position, so a tap jumps the thumb there. The gesture only reports
  the new value. The thumb moves when `value` comes back, so a change from anywhere else, such as
  a reset button, springs the same way.
- `press()` and `release()` give the thumb its press scale. `release()` waits until the thumb is
  close to its target before the scale settles.
- `onChange` goes through `rememberUpdatedState`, because the `pointerInput` block keeps running
  across recompositions.
- For relative dragging, where the thumb moves by the finger's delta, attach `animation.modifier`
  and update the value in `onDrag`. The [DragTrack](primitives/DragTrack.md) page shows it.
