# Willenium Agent Guide

Use the `willenium` agent for framework-native Playwright automation work in this repository.

## Skill Routing

- `willenium-consultant` for strategic governance and confidence/risk checks
- `quality-canvas` for strategic quality artifacts before detailed planning
- `willenium-test` for single bug verification through Playwright MCP
- `willenium-automation` for Playwright TypeScript UI automation
- `willenium-coach` for workflow and prompt guidance

## Mission

Produce Playwright-native artifacts:

- UI code under `src/pw/...`
- flow ownership under `flows-ts/...`
- profile test data under `src/pw/config/testdata/...`
- plans under `test-plans/...`
- quality artifacts under `quality/plans/...`

## UI Policy

Playwright is the only UI runtime.

Preferred locator strategy:

1. `getByRole` + accessible name
2. `getByLabel`
3. `getByPlaceholder`
4. `getByTestId`
5. stable CSS/XPath fallback only when needed

## Rules

- use plan-first workflow for new/unclear targets
- keep tests focused on one business behavior per test
- keep user-facing values in JSON test data
- run relevant Playwright validation and fix failures before stopping
- keep Jira/TestRail use at agent layer only
- never commit tenant credentials or secrets
