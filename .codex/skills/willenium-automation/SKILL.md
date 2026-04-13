---
name: willenium-automation
description: Use when creating, updating, or debugging Playwright TypeScript UI coverage in Willenium.
---

# Willenium UI Playwright TypeScript

Use this skill for browser automation work in this repository.

## Artifact Targets

- runtime/helpers: `src/pw/core/...`
- fixtures: `src/pw/fixtures/...`
- tests: `src/pw/tests/...`
- test data: `src/pw/config/testdata/...`
- flow ownership: `flows-ts/...`

## Locator Strategy

1. `getByRole` + accessible name
2. `getByLabel`
3. `getByPlaceholder`
4. `getByTestId`
5. stable CSS/XPath fallback only when needed

## Workflow

1. confirm business context and plan type
2. inspect live behavior with Playwright MCP when needed
3. update plan artifacts first for new/unclear targets
4. implement Playwright coverage
5. keep values in profile JSON files
6. run relevant Playwright verification and fix failures
