# Porting record

Construction Wand candidate `1.21.1-2.12-modporter.1` was generated on November 16, 2026 with the official ModPorter 0.4.0 release and validated as its exact packaged JAR.

## Provenance

| Field | Recorded value |
| --- | --- |
| Upstream repository | https://github.com/Theta-Dev/ConstructionWand.git |
| Upstream commit | `bc64f11a7d799e921995821878a031bcfde4e22a` |
| Upstream Git tree | `a69273532d7b74bbcdccca6ec349fe3e1d28a95c` |
| Upstream license | MIT; original copyright preserved |
| Tool release | [ModPorter v0.4.0 Developer Preview](https://github.com/CrabMods/modporter/releases/tag/v0.4.0) |
| Tool source commit | `da08fa920f733f572a45329b25b78a712de47452` |
| Tool source Git tree | `6d932849fa41e29880205cb3dfffa2ef53e154a5` |
| Official tool JAR SHA-256 | `1134fa8ae04d50d904396fc2b59b77d7e668464f8dd63cfcf4c2eb521d37a9f4` |
| Exporter SHA-256 | `0250f995115805e4b99a85231093a36a7833d6f5c5499e8a865197b6a8bf73d3` |
| Generated output tree SHA-256 | `7d205dbf45ba74670b7cf7f62d16e5ca6fbf8ee7489d9deda14adf67c9f28d94` |
| Build source commit | `13317c472103caa2e6355cf01c4b43ae0887a1ba` |
| Build source Git tree | `375b0911971ed52bdd93ecfeca79e223ec13c74b` |
| Candidate JAR | `constructionwand-1.21.1-2.12-modporter.1.jar` |
| Candidate JAR SHA-256 | `919fa9757f5f7bfe21b4c799a737bee3f5c70196cee1dbeff80bb569ceb95ecb` |
| Game / loader / Java | Minecraft 1.21.1 / NeoForge 21.1.230 / Java 21 |

## Generated source and import

All 103 production source and resource files match the generated export byte for byte. Mod logic has no hand patches. Repository metadata, the public build adapter, documentation, and the independent validation project are separate layers. The uncompiled upstream documentation images and historical build notes are omitted; the pinned upstream revision retains them.

The output-tree fingerprint uses sorted relative paths, file sizes, and file SHA-256 values (`sha256-relative-nul-size-nul-file-sha256-lf-v1`). `MODPORTER-PROVENANCE.json` and `MODPORTER-IMPORT.json` identify the source, tool, and import boundary.

## Recorded validation

| Lane | Process starts | World loads | Scenarios | Cleanup failures | Unexplained log findings |
| --- | --- | --- | --- | --- | --- |
| Dedicated server | 1 | 1 | 2 | 0 | 0 |
| Client | 1 | 1 | 4 | 0 | 0 |

The client matched all **6 registered item IDs** to the pinned upstream source, opened both relevant creative tabs, and captured **26 rendered rows**. All 1169 occupied slot observations have decoded nonblank icon evidence, including 6 target slot observations; icon exceptions are zero. Native item/component equality, client and server inventory cleanup, mouse-state restoration, saved-world shutdown, and the unchanged original fixture all passed.

`VALIDATION.json` records the exact artifact checksum, lane results, source-bound receipts, and evidence hashes. The checks cover loading and item-interface presentation. Detailed item, block, recipe, and integration behavior remains for developer review and focused playtesting.

## Reproduce generation

Use the exact official release JAR and matching v0.4.0 exporter. The source clone disables automatic line-ending conversion so blob-bound checks use the original upstream bytes. Export to a fresh S: directory.

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

After reviewing the generated output, retain the repository-owned public build adapter and prepare its hook:

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

## Reproduce packaged validation

The checked-in `validation/fixtures/client-world.zip` supplies the exact owned, player-free vanilla fixture. Extract it into a separate scratch directory, set the ignored target configuration from `validation/targets/constructionwand.properties`, and follow [the validation guide](validation/README.md). The target is loaded only from its regular JAR in the clean `mods` directory; the validation harness is a separate source-set mod.

## Repository policy

The Minecraft `1.21.1` branch contains this generated developer baseline; later game versions use separate version branches. Migration fixes belong in reusable [ModPorter rules and issues](https://github.com/CrabMods/modporter/issues). The original mod remains MIT with its original copyright. Public build and validation components retain their separate Apache-2.0 notices. Human review is part of preparing a generated port for broader distribution.
