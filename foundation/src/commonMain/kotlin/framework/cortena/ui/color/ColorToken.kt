/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.color

/**
 * CortenaUI — Color Token
 *
 * Public color token palette. Each token is an [AdaptiveColor] holding light and dark variants. Use
 * [AdaptiveColor.resolve] to obtain the raw Long for a given theme, or use the Compose extension
 * `AdaptiveColor.value()` inside a @Composable to auto-resolve.
 */
object ColorToken {

    // Red
    val Red50 = AdaptiveColor(ColorTokens.RedLight50, ColorTokens.RedDark50)
    val Red100 = AdaptiveColor(ColorTokens.RedLight100, ColorTokens.RedDark100)
    val Red200 = AdaptiveColor(ColorTokens.RedLight200, ColorTokens.RedDark200)
    val Red300 = AdaptiveColor(ColorTokens.RedLight300, ColorTokens.RedDark300)
    val Red400 = AdaptiveColor(ColorTokens.RedLight400, ColorTokens.RedDark400)
    val Red500 = AdaptiveColor(ColorTokens.RedLight500, ColorTokens.RedDark500)
    val Red600 = AdaptiveColor(ColorTokens.RedLight600, ColorTokens.RedDark600)
    val Red700 = AdaptiveColor(ColorTokens.RedLight700, ColorTokens.RedDark700)
    val Red800 = AdaptiveColor(ColorTokens.RedLight800, ColorTokens.RedDark800)
    val Red900 = AdaptiveColor(ColorTokens.RedLight900, ColorTokens.RedDark900)
    val Red950 = AdaptiveColor(ColorTokens.RedLight950, ColorTokens.RedDark950)

    // Orange
    val Orange50 = AdaptiveColor(ColorTokens.OrangeLight50, ColorTokens.OrangeDark50)
    val Orange100 = AdaptiveColor(ColorTokens.OrangeLight100, ColorTokens.OrangeDark100)
    val Orange200 = AdaptiveColor(ColorTokens.OrangeLight200, ColorTokens.OrangeDark200)
    val Orange300 = AdaptiveColor(ColorTokens.OrangeLight300, ColorTokens.OrangeDark300)
    val Orange400 = AdaptiveColor(ColorTokens.OrangeLight400, ColorTokens.OrangeDark400)
    val Orange500 = AdaptiveColor(ColorTokens.OrangeLight500, ColorTokens.OrangeDark500)
    val Orange600 = AdaptiveColor(ColorTokens.OrangeLight600, ColorTokens.OrangeDark600)
    val Orange700 = AdaptiveColor(ColorTokens.OrangeLight700, ColorTokens.OrangeDark700)
    val Orange800 = AdaptiveColor(ColorTokens.OrangeLight800, ColorTokens.OrangeDark800)
    val Orange900 = AdaptiveColor(ColorTokens.OrangeLight900, ColorTokens.OrangeDark900)
    val Orange950 = AdaptiveColor(ColorTokens.OrangeLight950, ColorTokens.OrangeDark950)

    // Yellow
    val Yellow50 = AdaptiveColor(ColorTokens.YellowLight50, ColorTokens.YellowDark50)
    val Yellow100 = AdaptiveColor(ColorTokens.YellowLight100, ColorTokens.YellowDark100)
    val Yellow200 = AdaptiveColor(ColorTokens.YellowLight200, ColorTokens.YellowDark200)
    val Yellow300 = AdaptiveColor(ColorTokens.YellowLight300, ColorTokens.YellowDark300)
    val Yellow400 = AdaptiveColor(ColorTokens.YellowLight400, ColorTokens.YellowDark400)
    val Yellow500 = AdaptiveColor(ColorTokens.YellowLight500, ColorTokens.YellowDark500)
    val Yellow600 = AdaptiveColor(ColorTokens.YellowLight600, ColorTokens.YellowDark600)
    val Yellow700 = AdaptiveColor(ColorTokens.YellowLight700, ColorTokens.YellowDark700)
    val Yellow800 = AdaptiveColor(ColorTokens.YellowLight800, ColorTokens.YellowDark800)
    val Yellow900 = AdaptiveColor(ColorTokens.YellowLight900, ColorTokens.YellowDark900)
    val Yellow950 = AdaptiveColor(ColorTokens.YellowLight950, ColorTokens.YellowDark950)

    // Green
    val Green50 = AdaptiveColor(ColorTokens.GreenLight50, ColorTokens.GreenDark50)
    val Green100 = AdaptiveColor(ColorTokens.GreenLight100, ColorTokens.GreenDark100)
    val Green200 = AdaptiveColor(ColorTokens.GreenLight200, ColorTokens.GreenDark200)
    val Green300 = AdaptiveColor(ColorTokens.GreenLight300, ColorTokens.GreenDark300)
    val Green400 = AdaptiveColor(ColorTokens.GreenLight400, ColorTokens.GreenDark400)
    val Green500 = AdaptiveColor(ColorTokens.GreenLight500, ColorTokens.GreenDark500)
    val Green600 = AdaptiveColor(ColorTokens.GreenLight600, ColorTokens.GreenDark600)
    val Green700 = AdaptiveColor(ColorTokens.GreenLight700, ColorTokens.GreenDark700)
    val Green800 = AdaptiveColor(ColorTokens.GreenLight800, ColorTokens.GreenDark800)
    val Green900 = AdaptiveColor(ColorTokens.GreenLight900, ColorTokens.GreenDark900)
    val Green950 = AdaptiveColor(ColorTokens.GreenLight950, ColorTokens.GreenDark950)

    // Mint
    val Mint50 = AdaptiveColor(ColorTokens.MintLight50, ColorTokens.MintDark50)
    val Mint100 = AdaptiveColor(ColorTokens.MintLight100, ColorTokens.MintDark100)
    val Mint200 = AdaptiveColor(ColorTokens.MintLight200, ColorTokens.MintDark200)
    val Mint300 = AdaptiveColor(ColorTokens.MintLight300, ColorTokens.MintDark300)
    val Mint400 = AdaptiveColor(ColorTokens.MintLight400, ColorTokens.MintDark400)
    val Mint500 = AdaptiveColor(ColorTokens.MintLight500, ColorTokens.MintDark500)
    val Mint600 = AdaptiveColor(ColorTokens.MintLight600, ColorTokens.MintDark600)
    val Mint700 = AdaptiveColor(ColorTokens.MintLight700, ColorTokens.MintDark700)
    val Mint800 = AdaptiveColor(ColorTokens.MintLight800, ColorTokens.MintDark800)
    val Mint900 = AdaptiveColor(ColorTokens.MintLight900, ColorTokens.MintDark900)
    val Mint950 = AdaptiveColor(ColorTokens.MintLight950, ColorTokens.MintDark950)

    // Teal
    val Teal50 = AdaptiveColor(ColorTokens.TealLight50, ColorTokens.TealDark50)
    val Teal100 = AdaptiveColor(ColorTokens.TealLight100, ColorTokens.TealDark100)
    val Teal200 = AdaptiveColor(ColorTokens.TealLight200, ColorTokens.TealDark200)
    val Teal300 = AdaptiveColor(ColorTokens.TealLight300, ColorTokens.TealDark300)
    val Teal400 = AdaptiveColor(ColorTokens.TealLight400, ColorTokens.TealDark400)
    val Teal500 = AdaptiveColor(ColorTokens.TealLight500, ColorTokens.TealDark500)
    val Teal600 = AdaptiveColor(ColorTokens.TealLight600, ColorTokens.TealDark600)
    val Teal700 = AdaptiveColor(ColorTokens.TealLight700, ColorTokens.TealDark700)
    val Teal800 = AdaptiveColor(ColorTokens.TealLight800, ColorTokens.TealDark800)
    val Teal900 = AdaptiveColor(ColorTokens.TealLight900, ColorTokens.TealDark900)
    val Teal950 = AdaptiveColor(ColorTokens.TealLight950, ColorTokens.TealDark950)

    // Cyan
    val Cyan50 = AdaptiveColor(ColorTokens.CyanLight50, ColorTokens.CyanDark50)
    val Cyan100 = AdaptiveColor(ColorTokens.CyanLight100, ColorTokens.CyanDark100)
    val Cyan200 = AdaptiveColor(ColorTokens.CyanLight200, ColorTokens.CyanDark200)
    val Cyan300 = AdaptiveColor(ColorTokens.CyanLight300, ColorTokens.CyanDark300)
    val Cyan400 = AdaptiveColor(ColorTokens.CyanLight400, ColorTokens.CyanDark400)
    val Cyan500 = AdaptiveColor(ColorTokens.CyanLight500, ColorTokens.CyanDark500)
    val Cyan600 = AdaptiveColor(ColorTokens.CyanLight600, ColorTokens.CyanDark600)
    val Cyan700 = AdaptiveColor(ColorTokens.CyanLight700, ColorTokens.CyanDark700)
    val Cyan800 = AdaptiveColor(ColorTokens.CyanLight800, ColorTokens.CyanDark800)
    val Cyan900 = AdaptiveColor(ColorTokens.CyanLight900, ColorTokens.CyanDark900)
    val Cyan950 = AdaptiveColor(ColorTokens.CyanLight950, ColorTokens.CyanDark950)

    // Blue
    val Blue50 = AdaptiveColor(ColorTokens.BlueLight50, ColorTokens.BlueDark50)
    val Blue100 = AdaptiveColor(ColorTokens.BlueLight100, ColorTokens.BlueDark100)
    val Blue200 = AdaptiveColor(ColorTokens.BlueLight200, ColorTokens.BlueDark200)
    val Blue300 = AdaptiveColor(ColorTokens.BlueLight300, ColorTokens.BlueDark300)
    val Blue400 = AdaptiveColor(ColorTokens.BlueLight400, ColorTokens.BlueDark400)
    val Blue500 = AdaptiveColor(ColorTokens.BlueLight500, ColorTokens.BlueDark500)
    val Blue600 = AdaptiveColor(ColorTokens.BlueLight600, ColorTokens.BlueDark600)
    val Blue700 = AdaptiveColor(ColorTokens.BlueLight700, ColorTokens.BlueDark700)
    val Blue800 = AdaptiveColor(ColorTokens.BlueLight800, ColorTokens.BlueDark800)
    val Blue900 = AdaptiveColor(ColorTokens.BlueLight900, ColorTokens.BlueDark900)
    val Blue950 = AdaptiveColor(ColorTokens.BlueLight950, ColorTokens.BlueDark950)

    // Indigo
    val Indigo50 = AdaptiveColor(ColorTokens.IndigoLight50, ColorTokens.IndigoDark50)
    val Indigo100 = AdaptiveColor(ColorTokens.IndigoLight100, ColorTokens.IndigoDark100)
    val Indigo200 = AdaptiveColor(ColorTokens.IndigoLight200, ColorTokens.IndigoDark200)
    val Indigo300 = AdaptiveColor(ColorTokens.IndigoLight300, ColorTokens.IndigoDark300)
    val Indigo400 = AdaptiveColor(ColorTokens.IndigoLight400, ColorTokens.IndigoDark400)
    val Indigo500 = AdaptiveColor(ColorTokens.IndigoLight500, ColorTokens.IndigoDark500)
    val Indigo600 = AdaptiveColor(ColorTokens.IndigoLight600, ColorTokens.IndigoDark600)
    val Indigo700 = AdaptiveColor(ColorTokens.IndigoLight700, ColorTokens.IndigoDark700)
    val Indigo800 = AdaptiveColor(ColorTokens.IndigoLight800, ColorTokens.IndigoDark800)
    val Indigo900 = AdaptiveColor(ColorTokens.IndigoLight900, ColorTokens.IndigoDark900)
    val Indigo950 = AdaptiveColor(ColorTokens.IndigoLight950, ColorTokens.IndigoDark950)

    // Purple
    val Purple50 = AdaptiveColor(ColorTokens.PurpleLight50, ColorTokens.PurpleDark50)
    val Purple100 = AdaptiveColor(ColorTokens.PurpleLight100, ColorTokens.PurpleDark100)
    val Purple200 = AdaptiveColor(ColorTokens.PurpleLight200, ColorTokens.PurpleDark200)
    val Purple300 = AdaptiveColor(ColorTokens.PurpleLight300, ColorTokens.PurpleDark300)
    val Purple400 = AdaptiveColor(ColorTokens.PurpleLight400, ColorTokens.PurpleDark400)
    val Purple500 = AdaptiveColor(ColorTokens.PurpleLight500, ColorTokens.PurpleDark500)
    val Purple600 = AdaptiveColor(ColorTokens.PurpleLight600, ColorTokens.PurpleDark600)
    val Purple700 = AdaptiveColor(ColorTokens.PurpleLight700, ColorTokens.PurpleDark700)
    val Purple800 = AdaptiveColor(ColorTokens.PurpleLight800, ColorTokens.PurpleDark800)
    val Purple900 = AdaptiveColor(ColorTokens.PurpleLight900, ColorTokens.PurpleDark900)
    val Purple950 = AdaptiveColor(ColorTokens.PurpleLight950, ColorTokens.PurpleDark950)

    // Pink
    val Pink50 = AdaptiveColor(ColorTokens.PinkLight50, ColorTokens.PinkDark50)
    val Pink100 = AdaptiveColor(ColorTokens.PinkLight100, ColorTokens.PinkDark100)
    val Pink200 = AdaptiveColor(ColorTokens.PinkLight200, ColorTokens.PinkDark200)
    val Pink300 = AdaptiveColor(ColorTokens.PinkLight300, ColorTokens.PinkDark300)
    val Pink400 = AdaptiveColor(ColorTokens.PinkLight400, ColorTokens.PinkDark400)
    val Pink500 = AdaptiveColor(ColorTokens.PinkLight500, ColorTokens.PinkDark500)
    val Pink600 = AdaptiveColor(ColorTokens.PinkLight600, ColorTokens.PinkDark600)
    val Pink700 = AdaptiveColor(ColorTokens.PinkLight700, ColorTokens.PinkDark700)
    val Pink800 = AdaptiveColor(ColorTokens.PinkLight800, ColorTokens.PinkDark800)
    val Pink900 = AdaptiveColor(ColorTokens.PinkLight900, ColorTokens.PinkDark900)
    val Pink950 = AdaptiveColor(ColorTokens.PinkLight950, ColorTokens.PinkDark950)

    // Brown
    val Brown50 = AdaptiveColor(ColorTokens.BrownLight50, ColorTokens.BrownDark50)
    val Brown100 = AdaptiveColor(ColorTokens.BrownLight100, ColorTokens.BrownDark100)
    val Brown200 = AdaptiveColor(ColorTokens.BrownLight200, ColorTokens.BrownDark200)
    val Brown300 = AdaptiveColor(ColorTokens.BrownLight300, ColorTokens.BrownDark300)
    val Brown400 = AdaptiveColor(ColorTokens.BrownLight400, ColorTokens.BrownDark400)
    val Brown500 = AdaptiveColor(ColorTokens.BrownLight500, ColorTokens.BrownDark500)
    val Brown600 = AdaptiveColor(ColorTokens.BrownLight600, ColorTokens.BrownDark600)
    val Brown700 = AdaptiveColor(ColorTokens.BrownLight700, ColorTokens.BrownDark700)
    val Brown800 = AdaptiveColor(ColorTokens.BrownLight800, ColorTokens.BrownDark800)
    val Brown900 = AdaptiveColor(ColorTokens.BrownLight900, ColorTokens.BrownDark900)
    val Brown950 = AdaptiveColor(ColorTokens.BrownLight950, ColorTokens.BrownDark950)

    // Gray
    val Gray50 = AdaptiveColor(ColorTokens.GrayLight50, ColorTokens.GrayDark50)
    val Gray100 = AdaptiveColor(ColorTokens.GrayLight100, ColorTokens.GrayDark100)
    val Gray200 = AdaptiveColor(ColorTokens.GrayLight200, ColorTokens.GrayDark200)
    val Gray300 = AdaptiveColor(ColorTokens.GrayLight300, ColorTokens.GrayDark300)
    val Gray400 = AdaptiveColor(ColorTokens.GrayLight400, ColorTokens.GrayDark400)
    val Gray500 = AdaptiveColor(ColorTokens.GrayLight500, ColorTokens.GrayDark500)
    val Gray600 = AdaptiveColor(ColorTokens.GrayLight600, ColorTokens.GrayDark600)
    val Gray700 = AdaptiveColor(ColorTokens.GrayLight700, ColorTokens.GrayDark700)
    val Gray800 = AdaptiveColor(ColorTokens.GrayLight800, ColorTokens.GrayDark800)
    val Gray900 = AdaptiveColor(ColorTokens.GrayLight900, ColorTokens.GrayDark900)
    val Gray950 = AdaptiveColor(ColorTokens.GrayLight950, ColorTokens.GrayDark950)

    // Neutral
    val Black = AdaptiveColor(ColorTokens.Black, ColorTokens.Black)
    val White = AdaptiveColor(ColorTokens.White, ColorTokens.White)
    val Transparent = AdaptiveColor(ColorTokens.Transparent, ColorTokens.Transparent)
}
