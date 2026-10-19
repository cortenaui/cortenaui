/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.color

/**
 * CortenaUI — Color Tokens
 *
 * Primitive color palette. These are raw values with no semantic meaning. Do not use these directly
 * in components — use [Palette] instead.
 *
 * Values are stored as Long (ARGB)
 */
internal object ColorTokens {

    // Red - Light
    const val RedLight50: Long = 0xFFFFE5E6
    const val RedLight100: Long = 0xFFFFCCCD
    const val RedLight200: Long = 0xFFFF999B
    const val RedLight300: Long = 0xFFFF6B6D
    const val RedLight400: Long = 0xFFFF383B // Base
    const val RedLight500: Long = 0xFFFF0509
    const val RedLight600: Long = 0xFFD10003
    const val RedLight700: Long = 0xFF990002
    const val RedLight800: Long = 0xFF660002
    const val RedLight900: Long = 0xFF330001
    const val RedLight950: Long = 0xFF1A0000

    // Red - Dark
    const val RedDark50: Long = 0xFFFFE5E6
    const val RedDark100: Long = 0xFFFFD1D2
    const val RedDark200: Long = 0xFFFF9EA0
    const val RedDark300: Long = 0xFFFF7072
    const val RedDark400: Long = 0xFFFF4245 // Base
    const val RedDark500: Long = 0xFFFF0A0E
    const val RedDark600: Long = 0xFFD60003
    const val RedDark700: Long = 0xFF9E0003
    const val RedDark800: Long = 0xFF6B0002
    const val RedDark900: Long = 0xFF330001
    const val RedDark950: Long = 0xFF1A0000

    // Orange - Light
    const val OrangeLight50: Long = 0xFFFFF4EB
    const val OrangeLight100: Long = 0xFFFFE9D6
    const val OrangeLight200: Long = 0xFFFFD1A8
    const val OrangeLight300: Long = 0xFFFFBB80
    const val OrangeLight400: Long = 0xFFFFA252
    const val OrangeLight500: Long = 0xFFFF8C28 // Base
    const val OrangeLight600: Long = 0xFFEB6D00
    const val OrangeLight700: Long = 0xFFB35300
    const val OrangeLight800: Long = 0xFF753700
    const val OrangeLight900: Long = 0xFF3D1C00
    const val OrangeLight950: Long = 0xFF1F0E00

    // Orange - Dark
    const val OrangeDark50: Long = 0xFFFFF2E5
    const val OrangeDark100: Long = 0xFFFFE4CC
    const val OrangeDark200: Long = 0xFFFFC999
    const val OrangeDark300: Long = 0xFFFFAE66
    const val OrangeDark400: Long = 0xFFFF9230 // Base
    const val OrangeDark500: Long = 0xFFFF7900
    const val OrangeDark600: Long = 0xFFCC6100
    const val OrangeDark700: Long = 0xFF994800
    const val OrangeDark800: Long = 0xFF663000
    const val OrangeDark900: Long = 0xFF331800
    const val OrangeDark950: Long = 0xFF1A0C00

    // Yellow - Light
    const val YellowLight50: Long = 0xFFFFFAE5
    const val YellowLight100: Long = 0xFFFFF5CC
    const val YellowLight200: Long = 0xFFFFEB99
    const val YellowLight300: Long = 0xFFFFE066
    const val YellowLight400: Long = 0xFFFFD633
    const val YellowLight500: Long = 0xFFFFCC00 // Base
    const val YellowLight600: Long = 0xFFCCA300
    const val YellowLight700: Long = 0xFF997A00
    const val YellowLight800: Long = 0xFF665200
    const val YellowLight900: Long = 0xFF332900
    const val YellowLight950: Long = 0xFF1A1400

    // Yellow - Dark
    const val YellowDark50: Long = 0xFFFFFBE5
    const val YellowDark100: Long = 0xFFFFF7CC
    const val YellowDark200: Long = 0xFFFFEF99
    const val YellowDark300: Long = 0xFFFFE666
    const val YellowDark400: Long = 0xFFFFDE33
    const val YellowDark500: Long = 0xFFFFD600 // Base
    const val YellowDark600: Long = 0xFFCCAB00
    const val YellowDark700: Long = 0xFF998000
    const val YellowDark800: Long = 0xFF665600
    const val YellowDark900: Long = 0xFF332B00
    const val YellowDark950: Long = 0xFF1A1500

    // Green - Light
    const val GreenLight50: Long = 0xFFEBFAEF
    const val GreenLight100: Long = 0xFFD7F4DE
    const val GreenLight200: Long = 0xFFAEEABD
    const val GreenLight300: Long = 0xFF86DF9C
    const val GreenLight400: Long = 0xFF59D478
    const val GreenLight500: Long = 0xFF34C759 // Base
    const val GreenLight600: Long = 0xFF299E47
    const val GreenLight700: Long = 0xFF207936
    const val GreenLight800: Long = 0xFF155124
    const val GreenLight900: Long = 0xFF0B2812
    const val GreenLight950: Long = 0xFF051409

    // Green - Dark
    const val GreenDark50: Long = 0xFFEAFAEE
    const val GreenDark100: Long = 0xFFD5F6DD
    const val GreenDark200: Long = 0xFFACECBC
    const val GreenDark300: Long = 0xFF82E39A
    const val GreenDark400: Long = 0xFF58DA78
    const val GreenDark500: Long = 0xFF30D158 // Base
    const val GreenDark600: Long = 0xFF25A745
    const val GreenDark700: Long = 0xFF1C7D34
    const val GreenDark800: Long = 0xFF135323
    const val GreenDark900: Long = 0xFF092A11
    const val GreenDark950: Long = 0xFF051509

    // Mint - Light
    const val MintLight50: Long = 0xFFE5FFFC
    const val MintLight100: Long = 0xFFCCFFFA
    const val MintLight200: Long = 0xFF9EFFF5
    const val MintLight300: Long = 0xFF6BFFF0
    const val MintLight400: Long = 0xFF2EFFEA
    const val MintLight500: Long = 0xFF00FAE1
    const val MintLight600: Long = 0xFF00C8B4 // Base
    const val MintLight700: Long = 0xFF009485
    const val MintLight800: Long = 0xFF00665C
    const val MintLight900: Long = 0xFF00332E
    const val MintLight950: Long = 0xFF001A17

    // Mint - Dark
    const val MintDark50: Long = 0xFFE5FFFC
    const val MintDark100: Long = 0xFFCCFFFA
    const val MintDark200: Long = 0xFF9EFFF5
    const val MintDark300: Long = 0xFF6BFFF0
    const val MintDark400: Long = 0xFF3DFFEB
    const val MintDark500: Long = 0xFF0AFFE6
    const val MintDark600: Long = 0xFF00DAC4 // Base
    const val MintDark700: Long = 0xFF00A393
    const val MintDark800: Long = 0xFF006B60
    const val MintDark900: Long = 0xFF003832
    const val MintDark950: Long = 0xFF001A17

    // Teal - Light
    const val TealLight50: Long = 0xFFE5FDFF
    const val TealLight100: Long = 0xFFCCFCFF
    const val TealLight200: Long = 0xFF99F8FF
    const val TealLight300: Long = 0xFF66F5FF
    const val TealLight400: Long = 0xFF38F2FF
    const val TealLight500: Long = 0xFF05EEFF
    const val TealLight600: Long = 0xFF00C2D0 // Base
    const val TealLight700: Long = 0xFF00939E
    const val TealLight800: Long = 0xFF005F66
    const val TealLight900: Long = 0xFF003033
    const val TealLight950: Long = 0xFF00181A

    // Teal - Dark
    const val TealDark50: Long = 0xFFE5FDFF
    const val TealDark100: Long = 0xFFD1FCFF
    const val TealDark200: Long = 0xFF9EF9FF
    const val TealDark300: Long = 0xFF70F6FF
    const val TealDark400: Long = 0xFF42F3FF
    const val TealDark500: Long = 0xFF0FF0FF
    const val TealDark600: Long = 0xFF00D2E0 // Base
    const val TealDark700: Long = 0xFF009EA8
    const val TealDark800: Long = 0xFF006970
    const val TealDark900: Long = 0xFF003538
    const val TealDark950: Long = 0xFF00181A

    // Cyan - Light
    const val CyanLight50: Long = 0xFFE5FBFF
    const val CyanLight100: Long = 0xFFD1F7FF
    const val CyanLight200: Long = 0xFFA3F0FF
    const val CyanLight300: Long = 0xFF75E8FF
    const val CyanLight400: Long = 0xFF47E0FF
    const val CyanLight500: Long = 0xFF1AD8FF
    const val CyanLight600: Long = 0xFF00C1E8 // Base
    const val CyanLight700: Long = 0xFF0090AD
    const val CyanLight800: Long = 0xFF006275
    const val CyanLight900: Long = 0xFF002F38
    const val CyanLight950: Long = 0xFF00191F

    // Cyan - Dark
    const val CyanDark50: Long = 0xFFE6F9FF
    const val CyanDark100: Long = 0xFFCCF4FF
    const val CyanDark200: Long = 0xFF9FEAFF
    const val CyanDark300: Long = 0xFF6CDFFE
    const val CyanDark400: Long = 0xFF3CD4FE // Base
    const val CyanDark500: Long = 0xFF06C8FE
    const val CyanDark600: Long = 0xFF01A3D0
    const val CyanDark700: Long = 0xFF017B9D
    const val CyanDark800: Long = 0xFF01546B
    const val CyanDark900: Long = 0xFF002833
    const val CyanDark950: Long = 0xFF001419

    // Blue - Light
    const val BlueLight50: Long = 0xFFE5F3FF
    const val BlueLight100: Long = 0xFFCCE7FF
    const val BlueLight200: Long = 0xFF99CFFF
    const val BlueLight300: Long = 0xFF66B8FF
    const val BlueLight400: Long = 0xFF33A0FF
    const val BlueLight500: Long = 0xFF0088FF // Base
    const val BlueLight600: Long = 0xFF006DCC
    const val BlueLight700: Long = 0xFF005299
    const val BlueLight800: Long = 0xFF003666
    const val BlueLight900: Long = 0xFF001B33
    const val BlueLight950: Long = 0xFF000E1A

    // Blue - Dark
    const val BlueDark50: Long = 0xFFE5F4FF
    const val BlueDark100: Long = 0xFFCCE9FF
    const val BlueDark200: Long = 0xFF99D3FF
    const val BlueDark300: Long = 0xFF66BDFF
    const val BlueDark400: Long = 0xFF33A7FF
    const val BlueDark500: Long = 0xFF0091FF // Base
    const val BlueDark600: Long = 0xFF0074CC
    const val BlueDark700: Long = 0xFF005799
    const val BlueDark800: Long = 0xFF003A66
    const val BlueDark900: Long = 0xFF001D33
    const val BlueDark950: Long = 0xFF000E1A

    // Indigo - Light
    const val IndigoLight50: Long = 0xFFEDECFE
    const val IndigoLight100: Long = 0xFFD7D4FC
    const val IndigoLight200: Long = 0xFFAFA8FA
    const val IndigoLight300: Long = 0xFF8B82F8
    const val IndigoLight400: Long = 0xFF6255F5 // Base
    const val IndigoLight500: Long = 0xFF3221F2
    const val IndigoLight600: Long = 0xFF1C0CCF
    const val IndigoLight700: Long = 0xFF15099A
    const val IndigoLight800: Long = 0xFF0E066A
    const val IndigoLight900: Long = 0xFF070335
    const val IndigoLight950: Long = 0xFF030118

    // Indigo - Dark
    const val IndigoDark50: Long = 0xFFE5E8FF
    const val IndigoDark100: Long = 0xFFCCD1FF
    const val IndigoDark200: Long = 0xFF9EA8FF
    const val IndigoDark300: Long = 0xFF6D7CFF // Base
    const val IndigoDark400: Long = 0xFF384DFF
    const val IndigoDark500: Long = 0xFF051FFF
    const val IndigoDark600: Long = 0xFF0015D1
    const val IndigoDark700: Long = 0xFF00109E
    const val IndigoDark800: Long = 0xFF000A66
    const val IndigoDark900: Long = 0xFF000533
    const val IndigoDark950: Long = 0xFF00031A

    // Purple - Light
    const val PurpleLight50: Long = 0xFFF9E9FC
    const val PurpleLight100: Long = 0xFFF5D7F9
    const val PurpleLight200: Long = 0xFFEAABF2
    const val PurpleLight300: Long = 0xFFE083EC
    const val PurpleLight400: Long = 0xFFD65BE6
    const val PurpleLight500: Long = 0xFFCB30E0 // Base
    const val PurpleLight600: Long = 0xFFAB1DBF
    const val PurpleLight700: Long = 0xFF80158E
    const val PurpleLight800: Long = 0xFF540E5D
    const val PurpleLight900: Long = 0xFF2C0731
    const val PurpleLight950: Long = 0xFF140316

    // Purple - Dark
    const val PurpleDark50: Long = 0xFFFCECFE
    const val PurpleDark100: Long = 0xFFF8D9FD
    const val PurpleDark200: Long = 0xFFF1AEFA
    const val PurpleDark300: Long = 0xFFEA87F7
    const val PurpleDark400: Long = 0xFFE35CF5
    const val PurpleDark500: Long = 0xFFDC34F2 // Base
    const val PurpleDark600: Long = 0xFFC50EDC
    const val PurpleDark700: Long = 0xFF960BA8
    const val PurpleDark800: Long = 0xFF62076E
    const val PurpleDark900: Long = 0xFF33043A
    const val PurpleDark950: Long = 0xFF1A021D

    // Pink - Light
    const val PinkLight50: Long = 0xFFFFE5EA
    const val PinkLight100: Long = 0xFFFFCCD6
    const val PinkLight200: Long = 0xFFFF94A8
    const val PinkLight300: Long = 0xFFFF617F
    const val PinkLight400: Long = 0xFFFF2D55 // Base
    const val PinkLight500: Long = 0xFFFA0030
    const val PinkLight600: Long = 0xFFC70026
    const val PinkLight700: Long = 0xFF94001C
    const val PinkLight800: Long = 0xFF660013
    const val PinkLight900: Long = 0xFF33000A
    const val PinkLight950: Long = 0xFF1A0005

    // Pink - Dark
    const val PinkDark50: Long = 0xFFFFE5EB
    const val PinkDark100: Long = 0xFFFFCCD6
    const val PinkDark200: Long = 0xFFFF99AD
    const val PinkDark300: Long = 0xFFFF6B89
    const val PinkDark400: Long = 0xFFFF375F // Base
    const val PinkDark500: Long = 0xFFFF0537
    const val PinkDark600: Long = 0xFFD1002A
    const val PinkDark700: Long = 0xFF99001F
    const val PinkDark800: Long = 0xFF660014
    const val PinkDark900: Long = 0xFF33000A
    const val PinkDark950: Long = 0xFF1A0005

    // Brown - Light
    const val BrownLight50: Long = 0xFFF6F2EE
    const val BrownLight100: Long = 0xFFEEE4DD
    const val BrownLight200: Long = 0xFFDECCBF
    const val BrownLight300: Long = 0xFFCDB19D
    const val BrownLight400: Long = 0xFFBD997F
    const val BrownLight500: Long = 0xFFAC7F5E // Base
    const val BrownLight600: Long = 0xFF8D6649
    const val BrownLight700: Long = 0xFF684B36
    const val BrownLight800: Long = 0xFF473324
    const val BrownLight900: Long = 0xFF221811
    const val BrownLight950: Long = 0xFF110C09

    // Brown - Dark
    const val BrownDark50: Long = 0xFFF8F4F1
    const val BrownDark100: Long = 0xFFF0E7E0
    const val BrownDark200: Long = 0xFFE2CFC1
    const val BrownDark300: Long = 0xFFD5BAA5
    const val BrownDark400: Long = 0xFFC6A286
    const val BrownDark500: Long = 0xFFB78A66 // Base
    const val BrownDark600: Long = 0xFF9C6E49
    const val BrownDark700: Long = 0xFF765337
    const val BrownDark800: Long = 0xFF4C3624
    const val BrownDark900: Long = 0xFF261B12
    const val BrownDark950: Long = 0xFF150F0A

    // Gray - Light
    const val GrayLight50: Long = 0xFFF5F5F5
    const val GrayLight100: Long = 0xFFE8E8E9
    const val GrayLight200: Long = 0xFFD3D3D5
    const val GrayLight300: Long = 0xFFBBBBBE
    const val GrayLight400: Long = 0xFFA4A4A8
    const val GrayLight500: Long = 0xFF8E8E93 // Base
    const val GrayLight600: Long = 0xFF707075
    const val GrayLight700: Long = 0xFF555559
    const val GrayLight800: Long = 0xFF39393C
    const val GrayLight900: Long = 0xFF1B1B1D
    const val GrayLight950: Long = 0xFF0F0F10

    // Gray - Dark
    const val GrayDark50: Long = 0xFFF2F2F2
    const val GrayDark100: Long = 0xFFE8E8E8
    const val GrayDark200: Long = 0xFFCECECF
    const val GrayDark300: Long = 0xFFB6B6B9
    const val GrayDark400: Long = 0xFF9C9CA0
    const val GrayDark500: Long = 0xFF858589
    const val GrayDark600: Long = 0xFF6C6C70 // Base
    const val GrayDark700: Long = 0xFF505053
    const val GrayDark800: Long = 0xFF373739
    const val GrayDark900: Long = 0xFF1C1C1D
    const val GrayDark950: Long = 0xFF0D0D0D

    // Neutral
    const val Black: Long = 0xFF000000
    const val White: Long = 0xFFFFFFFF
    const val Transparent: Long = 0x00000000
}
