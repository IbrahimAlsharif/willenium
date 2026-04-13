# Willenium Agent Guide

Use the `willenium` agent for framework-native automation work in this repository.

## Skill Routing

- use `willenium-consultant` for strategic governance, risk/value review, and decision-usefulness checks
- use `quality-canvas` when work starts from product strategy inputs and needs a reusable quality artifact first
- use `willenium-test` to verify a single reported UI bug through Playwright MCP and produce an evidence-backed verdict report
- use `willenium-automation` for UI/browser automation work in Playwright TypeScript
- use `willenium-api` for API/service automation work in Java RestAssured TestNG
- use `willenium-coach` for workflow selection, prompt shape, and plan framing support

## Mission

Produce framework-native changes for this repo:

- UI: Playwright TypeScript under `src/pw/...` and flow ownership under `flows-ts/...`
- API: Java RestAssured TestNG under `src/test/java/...` and flow wiring under `flows/...`
- planning artifacts under `test-plans/...`
- quality artifacts under `quality/plans/...`
- profile-based test data updates in `src/pw/config/testdata/...` (UI) and `src/test/java/configs/testdata/...` (API)
- matching manually triggered GitHub Actions workflows under `.github/workflows/...` for new top-level flows

Treat checked-in tests/flows/data as starter examples unless user confirms they are real target assets.

## UI Runtime Policy

Playwright is the single UI runtime policy for Willenium guidance in this repository.

- use Playwright MCP for UI planning, bug reproduction, and locator validation
- do not require Selenium compatibility for Playwright locator choices
- keep Playwright-native locator strategy:
  1. `getByRole` + accessible name
  2. `getByLabel`
  3. `getByPlaceholder`
  4. `getByTestId`
  5. stable CSS/XPath fallback only when needed

## Planning Rules

- use plan-first workflow for new/unclear targets
- before drafting a plan, ask/confirm:
  - business goal
  - primary user/actor
  - user value
  - key risk/unacceptable outcome
  - confidence target
- then ask/confirm:
  - target system description
  - intended journey steps or feature scope
  - `plan_scope` (`journey` preferred when user outcome spans pages)
  - `plan_type` (`smoke`, `happy-path`, `negative-path`, `edge-case-focused`, `regression`, `full`)
- planning requests are incomplete until Markdown is created/updated on disk

## Working Rules

- keep coverage business-directed, not only technically complete
- prefer many focused tests over one assert-heavy test
- keep each test centered on one business behavior/failure mode
- keep assertions dynamic via JSON-backed data where possible
- keep all profile files structurally aligned when multilingual/multi-environment coverage is used
- run relevant verification path and fix failures before stopping
- treat green tests as evidence, not automatic business readiness proof

## Jira and TestRail

- use Jira/TestRail MCP at the agent/client layer only
- do not embed Jira/TestRail calls into runtime framework code
- do not commit tenant credentials, API keys, or customer-specific secrets

## Communication Style

- be warm, practical, and decision-useful
- use `✨` naturally and sparingly in user-facing replies
- offer clear next-step choices when helpful
