# Separator

`Separator` is Cortena's visual divider component, used to group content or separate items in a layout.

## Concept

The component is a clean line with:

- Support for both **Horizontal** and **Vertical** orientations.
- Configurable **thickness** and **indents** (insets) on both ends of the separator.
- Automatic integration with Cortena's theme, defaulting to the semantic `outlineVariant` color role at 60% opacity for a clean, non-intrusive appearance.

## API Reference

```kotlin
@Composable
fun Separator(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    thickness: Dp = 1.dp,
    color: Color = Color.Unspecified,
    indent: Dp = 0.dp,
)
```

### Parameters

| Name          | Data Type     | Description                                                                                                    |
| ------------- | ------------- | -------------------------------------------------------------------------------------------------------------- |
| `modifier`    | `Modifier`    | Standard Compose modifier.                                                                                     |
| `orientation` | `Orientation` | Layout direction: `Orientation.Horizontal` or `Orientation.Vertical`. Default: `Orientation.Horizontal`.       |
| `thickness`   | `Dp`          | Line thickness. Default: `1.dp`.                                                                               |
| `color`       | `Color`       | Color of the separator line. Defaults to semantic `outlineVariant` at 60% opacity.                             |
| `indent`      | `Dp`          | Padding applied at both ends of the line (start/end for horizontal, top/bottom for vertical). Default: `0.dp`. |

### Example

#### Horizontal Separator (Default)

```kotlin
Column {
    Text("Section 1")
    Separator()
    Text("Section 2")
}
```

#### Vertical Separator

```kotlin
Row(modifier = Modifier.height(24.dp)) {
    Text("Left")
    Separator(
        orientation = Orientation.Vertical,
        indent = 4.dp
    )
    Text("Right")
}
```

#### Custom Thickness and Colors

```kotlin
Separator(
    thickness = 2.dp,
    color = Color(LocalColors.current.primary),
    indent = 16.dp
)
```
