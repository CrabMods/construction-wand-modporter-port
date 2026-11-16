# Porting Record

This document records the source, generator, validation state, and publication
metadata for the unofficial Construction Wand NeoForge 1.21.1 developer
baseline.

## Source Record

| Field | Value |
| --- | --- |
| Repository | `https://github.com/Theta-Dev/ConstructionWand.git` |
| Branch | `1.20` |
| Commit | `bc64f11a7d799e921995821878a031bcfde4e22a` |
| Git tree | `a69273532d7b74bbcdccca6ec349fe3e1d28a95c` |
| Commit date | `2024-08-01T14:24:59+02:00` |
| Source Minecraft version | `1.20.1` |
| Source loader | Forge `47.0.19` |
| Source version fields | `2.12` |
| License | MIT, Copyright (c) 2021 ThetaDev |

The source version fields are provenance for the pinned branch revision. They
must not be presented as evidence of an official `1.20.1-2.12` binary release.

## Generator Record

| Field | Value |
| --- | --- |
| Tool | [ModPorter](https://github.com/CrabMods/modporter) |
| Migration | Forge 1.20.1 to NeoForge 1.21.1 |
| Required public tool version | `0.4.0` |
| Official release tag and publication evidence | [v0.4.0 Developer Preview](https://github.com/CrabMods/modporter/releases/tag/v0.4.0) |
| Official ModPorter source commit | `da08fa920f733f572a45329b25b78a712de47452` |
| Official ModPorter source Git tree | `6d932849fa41e29880205cb3dfffa2ef53e154a5` |
| Official release JAR SHA-256 | `1134fa8ae04d50d904396fc2b59b77d7e668464f8dd63cfcf4c2eb521d37a9f4` |
| Exporter script SHA-256 | `0250f995115805e4b99a85231093a36a7833d6f5c5499e8a865197b6a8bf73d3` |
| Generated output tree SHA-256 | `7d205dbf45ba74670b7cf7f62d16e5ca6fbf8ee7489d9deda14adf67c9f28d94` |
| Imported candidate Git tree | Pending |
| Target Minecraft version | `1.21.1` |
| Target NeoForge version | `21.1.230` |
| Java toolchain | `21` |
| Candidate JAR | Not built |
| Candidate JAR SHA-256 | Pending |

ModPorter applies reusable source, resource, build-system, metadata, and API
migration rules. This repository is an inspectable generated result and a
learning baseline. It is not intended to accumulate a separate set of
Construction Wand-specific feature implementations. Repeatable fixes should
be made in ModPorter and regenerated here.

## Reproducible Candidate Export

Use the final published ModPorter 0.4.0 fat JAR and its matching bounded export
script. The source checkout must be clean at the pinned upstream commit, and
the output directory must be new and empty.

The exporter verifies the public versioned release and its published
`SHA256SUMS` before running the generator. Generated plant compatibility
sources, when present, remain tracked with their licenses and provenance so
the public checkout contains the complete build inputs.

```powershell
$ScratchRoot = 'S:\modporter-candidate-export\constructionwand-0.4.0'
$env:TEMP = Join-Path $ScratchRoot 'temp'
$env:TMP = $env:TEMP
$env:PYTHONDONTWRITEBYTECODE = '1'
$Upstream = Join-Path $ScratchRoot 'upstream-bc64f11'
$Export = Join-Path $ScratchRoot 'official-export'
$ModPorterSource = 'S:\path\to\official-modporter-v0.4.0-source'
$ModPorterJar = 'S:\path\to\official-modporter-v0.4.0-release\modporter-0.4.0-all.jar'
$OfficialJarSha256 = '<copy from the official v0.4.0 SHA256SUMS asset>'

New-Item -ItemType Directory -Force -Path $env:TEMP | Out-Null
git clone --config core.autocrlf=false --filter=blob:none --no-checkout `
  https://github.com/Theta-Dev/ConstructionWand.git $Upstream
git -C $Upstream checkout --detach `
  bc64f11a7d799e921995821878a031bcfde4e22a

python -B "$ModPorterSource\scripts\export_research_port.py" `
  --jar $ModPorterJar `
  --src $Upstream `
  --out $Export `
  --repo-url https://github.com/CrabMods/construction-wand-modporter-port `
  --mod-id constructionwand `
  --display-name "Construction Wand" `
  --candidate-version 1.21.1-2.12-modporter.1 `
  --upstream-url https://github.com/Theta-Dev/ConstructionWand.git `
  --upstream-commit bc64f11a7d799e921995821878a031bcfde4e22a
```

The scratch root is explicit on `S:` and remains outside every
`.modporter/tmp` producer-owned subtree. `$ModPorterSource`, `$ModPorterJar`,
and `$OfficialJarSha256` must come from the published public `v0.4.0` source
and release assets. Do not substitute an unpublished or locally built JAR.
The official `v0.4.0` Developer Preview is acceptable only after its GitHub
release and published `SHA256SUMS` have been verified.

Review `MODPORTER-PROVENANCE.json`, `MODPORTER-NOTICE`, the preserved MIT
license and copyright, generated metadata, complete source diff, and output
inventory before importing the export into this repository. Build and
packaged-artifact validation must run from the committed public candidate
tree, and the resulting JAR SHA-256 must be added to this record.

After importing the reviewed official export, retain the repository-owned
public build adapter at the root: `AGENTS.md`, `mod-build.ps1`, `mod-build.cmd`,
`scripts/`, and `gradle/modporter-testing.gradle`. Then prepare and check the
candidate before the public build:

```powershell
python -B .\scripts\prepare_candidate_import.py `
  --project-root . `
  --official-jar-sha256 $OfficialJarSha256
python -B .\scripts\prepare_candidate_import.py `
  --project-root . `
  --official-jar-sha256 $OfficialJarSha256 `
  --check
.\mod-build.ps1 ensure
.\mod-build.ps1 build
```

The preparation command validates the official `0.4.0` provenance and reviewed
upstream pin, changes only the generated `build.gradle`, and applies
`gradle/modporter-testing.gradle` as one canonical top-level line. It does not
patch Java or Kotlin. The canonical launcher checks that exact application
again before any Gradle command. CI needs no `gh` command, GitHub token, or
private framework credentials; a maintainer's optional private sidecar stays
outside Git.

## Validation Record

The current results bind the generated source-project workspace used by the
strict benchmark. They do not yet bind a packaged distribution JAR.

| Check | Result | Packaged-candidate follow-up |
| --- | --- | --- |
| Source compilation | Passed | Rebuild from the public candidate source |
| Dedicated server start and controlled stop | Passed | Launch with only the exact candidate JAR installed |
| Client boot and saved-world load | Passed | Launch with only the exact candidate JAR installed |
| Relevant creative tabs and displayed stacks browsed | Passed | Repeat against the exact candidate JAR |
| Strict porter-related warning and error audit | Passed | Repeat against the packaged client and server logs |
| Source-project GameTests | Passed, 1/1 required test | Retain as a development-source gate |

Before a binary is described as tested, the exact packaged artifact must pass
the following checks:

1. Build the candidate from the public source and record its SHA-256.
2. Confirm the JAR contains the MIT license, NeoForge metadata, manifest,
   classes, assets, and data resources.
3. Confirm the JAR excludes benchmark harnesses, GameTest-only classes, local
   run data, build outputs, logs, and nested dependency JARs.
4. Confirm the manifest and NeoForge metadata report the candidate version
   rather than the source-set placeholder `0.0NONE`.
5. Start a clean NeoForge 1.21.1 client and dedicated server with the exact JAR
   installed from their `mods` directories.
6. In one client world, open the creative inventory and browse every displayed
   stack in the relevant tabs, matching the documented interface checks.
7. Record a receipt that binds the artifact hash, installed mod inventory,
   scenario results, process count, world count, and clean shutdown.

Developer review can extend this baseline with construction, angel placement,
destruction, option switching, undo, multiplayer packets, and optional-mod
integration tests. Those detailed behavior tests are not part of the current
candidate's recorded scope.

## Candidate Metadata

The candidate should preserve the original `constructionwand` mod ID so its
resources, configuration, and saved data retain their expected namespace. It
must not be installed alongside another mod using the same ID.

Recommended release metadata:

| Field | Proposed value |
| --- | --- |
| Artifact version | `1.21.1-2.12-modporter.1` |
| Artifact name | `constructionwand-1.21.1-2.12-modporter.1.jar` |
| Display name | `Construction Wand (Unofficial ModPorter Candidate)` |
| Authors | `ThetaDev` |
| Credits | `Unofficial Minecraft 1.21.1 candidate generated by ModPorter 0.4.0 from upstream commit bc64f11a7d799e921995821878a031bcfde4e22a; not affiliated with or endorsed by ThetaDev.` |
| License | `MIT` |
| Issue tracker | `https://github.com/CrabMods/modporter/issues` |

The final metadata should also identify the candidate repository or release
page and include the exact ModPorter release commit used to generate it.

Recommended JAR provenance fields:

```text
ModPorter-Generated: true
ModPorter-Version: 0.4.0
Upstream-Repository: https://github.com/Theta-Dev/ConstructionWand
Upstream-Commit: bc64f11a7d799e921995821878a031bcfde4e22a
Unofficial-Candidate: true
```

## Publication Text

Suggested opening for a future candidate release:

> This is an experimental, unofficial candidate generated by ModPorter 0.4.0
> from ThetaDev's Construction Wand source at commit
> `bc64f11a7d799e921995821878a031bcfde4e22a` on the `1.20` branch. It is not
> an official ThetaDev release and is not affiliated with or endorsed by
> ThetaDev. Original code and assets remain copyright their respective
> authors and are distributed under the included MIT License. Please report
> candidate-specific issues to the ModPorter issue tracker.

Do not add claims about packaged-JAR validation until the exact published
artifact and checksum have passed the checks above.

## Repository Policy

- Repository name: `construction-wand-modporter-port`
- Current default branch: `1.21.1`
- Future Minecraft versions: one branch per target game version
- Primary migration issue tracker:
  `https://github.com/CrabMods/modporter/issues`
- Upstream project issues should receive only problems reproduced in an
  official upstream release.

## GitHub Metadata Proposal

About:

> Unofficial NeoForge 1.21.1 developer baseline for Construction Wand,
> generated by ModPorter from ThetaDev's MIT-licensed Forge 1.20.1 source for
> research, learning, and community review.

Topics:

```text
minecraft
minecraft-mod
minecraft-1-21-1
neoforge
construction-wand
mod-porting
modporter
java
developer-preview
unofficial-port
```


## Import scope

The imported production code and resources match the generated export byte for
byte. The public build hook, verification project, repository metadata, and
developer documentation are maintained separately. Uncompiled upstream images and historical build notes are excluded from this developer baseline; they remain available at the
pinned upstream revision.
