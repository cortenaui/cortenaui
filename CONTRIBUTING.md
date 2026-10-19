# Contributing to CortenaUI

Thanks for helping. This guide covers setup, the change workflow, and what a pull request needs
before it can be merged.

## Setup

You need JDK 17 or newer, the Android SDK with API 37, Git, and Node.js (for Prettier).

```bash
git clone https://github.com/cortenaui/cortenaui.git
cd cortenaui
./gradlew build
```

Run the showcase app on a device or emulator with Android 13 (API 33) or newer:

```bash
./gradlew :catalog:installDebug
```

## Workflow

1. Open or pick an issue. Discuss a new component or a public API change in the issue before
   writing code.
2. Branch from an up-to-date `master` with a prefix and a descriptive name: `feat/slider-steps`,
   `fix/toggle-rtl-drag`, `docs/list-item-examples`. Prefixes: `feat/`, `fix/`, `refactor/`,
   `docs/`, `chore/`, `ci/`.
3. Write [Conventional Commits](https://www.conventionalcommits.org/) scoped to the component or
   area, for example `feat(Slider): add steps parameter`. Sign off every commit with
   `git commit -s` to certify the [Developer Certificate of Origin](https://developercertificate.org/).
4. Format, build, and test:

   ```bash
   scripts/format.sh
   ./gradlew build
   ```

5. Open a pull request to `master` and fill in the template.

## Code rules

- Components take colors, spacing, sizes, and motion from the theme (`LocalColors`,
  `LocalSpacing`, `LocalSizeToken`, `LocalMotion`). No hardcoded values.
- The library modules (`:foundation`, `:shape`, `:motion`, `:compose`) take no third-party or
  Material dependencies. A new first-party dependency (Kotlin, JetBrains Compose, Google
  androidx) needs a maintainer's approval first.
- No `Build.VERSION.SDK_INT` checks: minSdk 33 covers every API the library uses.
- Public API lives in `commonMain`. Platform code lives in `androidMain` behind
  `expect`/`actual`.
- Never rename an existing parameter. New parameters are additive and have defaults.
- No unused imports, variables, or parameters. Aim for zero compiler warnings, and never add new
  ones.
- Every Kotlin file starts with the license header:

  ```kotlin
  /*
   * SPDX-License-Identifier: Apache-2.0
   * Copyright (C) 2026-present The CortenaOS Project
   */
  ```

- Attributions for third-party code go in [THIRD_PARTY_LICENSES](THIRD_PARTY_LICENSES), not in
  source files.

[ARCHITECTURE](docs/ARCHITECTURE.md) explains the module layout and the design patterns behind
these rules.

## Tests and docs

- New non-trivial logic in `:foundation` (math, token derivation, geometry) comes with a unit
  test in `foundation/src/commonTest/`, written with `kotlin-test`. Run the tests alone with
  `./gradlew testAndroidHostTest`; `./gradlew build` runs them too.
- If you change a component's parameters, update its page in `docs/` in the same pull request.
  If you change visible behavior, update its catalog demo.
- To preview the docs site, run `pip install zensical` and then `zensical build --clean`.

## Formatting

`scripts/format.sh` formats tracked Kotlin files with [ktfmt](https://github.com/facebook/ktfmt)
in Kotlin coding conventions style (`--kotlinlang-style`), and Markdown files with
[Prettier](https://prettier.io/). `scripts/format.sh --check` only reports the files that need
formatting. The script needs Java and Node.js and downloads ktfmt into `build/tools/` on its first
run. On Windows, run it from Git Bash.

## License

By contributing, you agree that your contributions are licensed under the
[Apache License, Version 2.0](LICENSE).
