---
plan_id: framework_selenium-java-testng-to-playwright-typescript-migration
target_name: Selenium Java TestNG to Playwright TypeScript Migration
target_url: current-runtime-framework
target_slug: selenium-java-testng-to-playwright-typescript-migration
app: framework
quality_canvas:
plan_scope: framework migration
plan_type: full
business_goal: Migrate UI automation stack to Playwright TypeScript without losing environment control, data governance, or reusable helper abstractions
user_value: Teams can author and run stable UI automation faster while preserving existing quality signals and test data discipline
confidence_target: Release owners can trust parity between legacy Selenium flows and new Playwright flows before decommissioning Java UI suites
unacceptable_outcomes:
  - false confidence where migrated tests pass but business checkpoints are weaker than current Java coverage
  - environment mix-ups where staging/prod assertions or credentials are resolved from the wrong profile
  - data model drift across language or environment JSON files
  - helper regression that forces raw locator usage instead of stable Finder/Go patterns
scenario_map:
  - migration baseline and governance
  - helper parity and data/env parity
  - pilot flow migration and side-by-side execution
  - phased rollout with decommission checkpoints
intensity_level: high
jira_issue_key:
jira_issue_url:
affected_flows:
  - flows/
affected_tests:
  - src/test/java/base/Setup.java
  - src/test/java/base/Finder.java
  - src/test/java/base/Go.java
  - src/test/java/configs/pipeline/PipelineConfig.java
  - src/test/java/configs/testdata/TestDataFactory.java
affected_testdata:
  - src/test/java/configs/testdata/exampleProductionArabic.json
  - src/test/java/configs/testdata/exampleProductionEnglish.json
  - src/test/java/configs/testdata/exampleStagingArabic.json
  - src/test/java/configs/testdata/exampleStagingEnglish.json
flow_xml: flows/
java_helper: base.Finder, base.Go
java_test: tests.*.*.*
testdata_sections:
  - shared journey sections (per app)
status: draft
---

# Test Plan: Selenium Java TestNG to Playwright TypeScript Migration

## Summary

This plan defines a controlled migration path from `Selenium Java TestNG` to `Playwright TypeScript` while preserving the current Willenium operating model:

- environment-aware execution (`production` vs `staging`)
- language-aware test data management (`arabic` vs `english`)
- helper-first authoring style (`Finder` and `Go`) instead of raw locator-heavy test methods

This is a framework migration plan, not just a syntax rewrite plan. The intent is to keep business confidence stable while switching execution technology.

Working assumptions for this draft:

- plan scope: `framework migration`
- plan type: `full`

## Business Goal

- modernize the UI automation execution engine to Playwright for better speed, stability, and debugging ergonomics
- keep the same decision value of current coverage so release confidence does not drop during migration
- keep migration incremental to avoid delivery freeze

## User Value

- QA and automation engineers can write focused tests faster with less flakiness
- product stakeholders keep clear `prod/stage` and `ar/en` quality signals during transition
- maintainers keep readable helper APIs similar to existing `Finder/Go` behavior

## Confidence Target

- prove that migrated Playwright suites protect the same business checkpoints as legacy Java suites
- approve Java UI suite decommission only after parity gates pass on representative journey coverage

## Business Questions Answered

- primary actor: QA/automation team maintaining business journey coverage
- core outcome: migrate runtime stack without losing trust in quality reporting
- business-critical failure: tests still pass but no longer prove real journey outcomes
- decision supported: when to run mixed mode, when to switch default to Playwright, and when to retire Selenium UI flows

## Journey Steps Or Feature

This plan treats the target as a framework feature area with these migration steps:

1. establish target Playwright architecture and governance guardrails
2. implement data/env parity layer (`prod/stage`, `ar/en`) in TypeScript
3. implement helper parity layer (`Finder` + `Go`) in TypeScript
4. migrate one high-value pilot journey with side-by-side assertions
5. migrate remaining suites in waves with parity gates
6. decommission Java UI suites only after evidence-based exit criteria pass

## Scope

- create Playwright TypeScript project structure compatible with current Willenium conventions
- preserve JSON-backed test data strategy and four-file environment/language pattern
- preserve environment selection model through config-driven profile resolution
- introduce TypeScript helper abstractions equivalent to `Finder` and `Go`
- migrate existing UI suites in controlled waves with measurable parity checks
- keep API stack (`RestAssured Java`) out of this migration unless requested later

## Plan Type

- `full`
- this depth is required because migration affects framework architecture, helper contracts, data governance, execution flows, CI wiring, and release confidence

## Out Of Scope

- full API test migration to Playwright API testing in this phase
- immediate deletion of all Java UI artifacts
- changing business intent of existing journeys during migration
- introducing new product coverage unrelated to parity and modernization goals

## Assumptions

- Java UI tests remain runnable during transition as baseline reference
- JSON test data remains the source of truth for user-facing expectations
- environment and language profiles will continue to be selected by runtime config
- migration can proceed wave-by-wave without a hard cutover date in phase 1

## Unacceptable Outcomes

- Playwright migration reduces assertion quality to "page rendered" checks only
- mixed environment data gets cross-loaded (for example staging run reading prod expectations)
- helper abstractions are bypassed and raw selectors spread across test bodies
- CI reports become incomparable across legacy and migrated suites

## Open Questions

- should TypeScript execution be orchestrated alongside existing TestNG XML flows, or via new Playwright projects only
- preferred CI strategy during transition: dual run on same trigger or separate workflows per stack
- decommission threshold: percentage of migrated critical journeys vs absolute parity checklist completion
- reporting target during transition (current reports only vs unified dashboard)

## Jira Linkage

- no Jira issue linked yet
- this migration plan is currently locally scoped and can be linked later

## Impact Analysis

- setup/runtime:
  `base.Setup` and `configs.pipeline.PipelineConfig` currently own runtime behavior that must be mirrored in TypeScript config
- helper layer:
  `base.Finder` and `base.Go` patterns must be preserved via a typed Playwright helper facade
- data layer:
  `configs.testdata` model and JSON profile files must remain structurally stable
- suite wiring:
  `flows/...` currently govern execution ownership; migration needs a parallel suite mapping matrix in TypeScript
- CI:
  workflow dispatch logic must support mixed mode until cutover

## Environment And Setup

- keep explicit runtime profile dimensions:
  - `branch` or `environment`: `production` / `staging`
  - `language`: `english` / `arabic`
- target TypeScript config model:
  - `playwright.config.ts` reads profile selectors from env vars or CLI args
  - central `RuntimeProfile` resolver returns base URL, locale, timeouts, and data file path
  - one source for profile resolution used by all suites

Example target profile matrix:

1. `production + english`
2. `production + arabic`
3. `staging + english`
4. `staging + arabic`

## Test Data

- preserve JSON-first strategy and avoid hardcoding assertion text in test files
- keep four structurally identical files for UI assertions:
  - `production-ar.json`
  - `production-en.json`
  - `staging-ar.json`
  - `staging-en.json`
- create TypeScript `TestDataFactory` equivalent that:
  - resolves active profile
  - validates schema consistency
  - exposes typed accessors for tests/helpers

## Helper Parity Design (`Finder` and `Go`)

Design target:

- `Finder` (TypeScript)
  - wrapper around `page.locator(...)` and stable locator builders
  - helper methods for readiness and clickability checks
  - selector preference policy: `id` -> `name` -> stable CSS -> XPath fallback only
- `Go` (TypeScript)
  - action-oriented methods (`click`, `type`, `clickAndWait`, `navigate`, `assertVisibleCheckpoint`)
  - embedded waits aligned with business checkpoints, not arbitrary sleeps
  - central logging and step naming for report readability

Rule:

- tests should call business-oriented helper methods first, not raw Playwright actions directly, unless explicitly justified

## Test Cases

1. Migration governance checkpoint:
   prove that the new TypeScript architecture enforces profile-based env/data resolution before any journey executes.
2. Helper parity checkpoint:
   prove that one representative legacy flow can be expressed with `Finder/Go`-style helpers and stable assertions.
3. Data parity checkpoint:
   prove that the same journey runs successfully on `staging-en` and `production-ar` with profile-specific expectations loaded from JSON.
4. Cutover safety checkpoint:
   prove that side-by-side Selenium vs Playwright results align on core business checkpoints for pilot journeys.

## Localization

- preserve language-specific expectations in dedicated JSON values
- keep shared key structure across Arabic and English data files
- include at least one Arabic pilot journey in phase 1 parity to avoid English-only migration bias

## Scenario Map

- primary success path: profile resolved -> data loaded -> helper-driven journey passes in Playwright
- alternate path: same journey under second environment/language profile passes with different JSON expectations
- failure path: profile/data mismatch is detected early and fails with clear diagnostics
- recovery path: fallback to legacy Selenium run remains available until parity gate is restored

## Intensity Level

- `high`, because this migration impacts execution trust, release confidence, and framework maintainability

## Secondary Implementation Notes

- legacy ownership remains:
  - `src/test/java/base/Finder.java`
  - `src/test/java/base/Go.java`
  - `src/test/java/configs/testdata/*`
- target TypeScript ownership (proposed):
  - `src/pw/core/runtime/RuntimeProfile.ts`
  - `src/pw/core/data/TestDataFactory.ts`
  - `src/pw/core/helpers/Finder.ts`
  - `src/pw/core/helpers/Go.ts`
  - `src/pw/tests/<app>/<journey>.spec.ts`
  - `flows-ts/<app>/<journey>.flow.ts` (optional logical suite map if needed)

## Verification Strategy

- phase gate 1: architecture and profile resolution validated in CI
- phase gate 2: pilot journey side-by-side parity (Selenium + Playwright) passes
- phase gate 3: migration wave parity score reaches agreed threshold on critical journeys
- phase gate 4: Java UI decommission checklist passes

Minimum CI transition pattern:

1. `legacy-ui-smoke` (Java Selenium)
2. `pw-ui-smoke` (Playwright TS)
3. optional nightly `legacy-vs-pw-parity` comparator report

## Migration Waves (Execution Roadmap)

1. Wave 0 - Foundation
   - scaffold Playwright TypeScript structure
   - add runtime profile resolver
   - add typed data loader with schema validation
2. Wave 1 - Helper Layer
   - implement `Finder.ts` and `Go.ts` parity APIs
   - migrate one small internal smoke flow to validate helper ergonomics
3. Wave 2 - Pilot Journeys
   - migrate 2 to 4 high-value UI journeys (include Arabic coverage)
   - run side-by-side with Selenium and compare checkpoints
4. Wave 3 - Bulk Migration
   - migrate remaining prioritized flows by business criticality
   - keep mixed mode CI until parity target is met
5. Wave 4 - Decommission
   - lock regression evidence
   - retire Java UI flow ownership gradually
   - keep rollback branch and last-known-good baseline

## Rollback And Safety

- keep legacy Java UI suites runnable until final cutover approval
- maintain migration feature flag in CI (`USE_PLAYWRIGHT_UI=true/false`)
- for any parity regression, revert impacted journey to legacy owner while fixing Playwright implementation

## Review Status

- ready for review as migration draft
- waiting for confirmation of:
  - preferred CI mixed-mode strategy
  - pilot journey shortlist
  - cutover acceptance threshold
