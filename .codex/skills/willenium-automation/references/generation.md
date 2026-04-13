# Generation Workflow

## When To Read This

Read this reference when the user wants to:

- generate UI tests from an approved plan
- update existing UI automation from an updated plan
- map plan sections to Playwright artifacts

## Preconditions

Before generation starts:

- a Markdown plan exists on disk
- `plan_scope` and `plan_type` are clear
- the user has reviewed the draft direction when planning was ambiguous

## Source Of Truth

Treat the plan as source of truth for:

- business journey ownership
- intended coverage depth
- artifact mapping
- open assumptions and risks
- test-data needs

## Playwright Mapping

Translate the plan into the repo's Playwright-native structure:

- reusable helper logic in `src/pw/core/helpers/...`
- runtime/data logic in `src/pw/core/runtime/...` and `src/pw/core/data/...`
- assertion-focused specs in `src/pw/tests/...`
- shared fixtures in `src/pw/fixtures/...`
- dynamic inputs/expected values in `src/pw/config/testdata/...`
- flow ownership in `flows-ts/...`

## Generation Rules

- reuse shared helpers before adding one-off page interaction logic
- keep assertions in `.spec.ts` files
- keep each test focused on one business expectation/failure mode
- prefer many focused tests over one assert-heavy test
- keep runtime behavior config-driven through `playwright.config.ts` and runtime profile helpers
- keep user-facing values in JSON test data, not hardcoded literals
- preserve environment/language profile separation
- keep all four profile files structurally identical
- update existing owner assets when plan says behavior belongs there

## Locator Rules

Use Playwright locator strategies directly:

- prefer `getByRole` with accessible names
- then `getByLabel`, `getByPlaceholder`, `getByTestId`
- use stable CSS/XPath only when semantic locators are insufficient
- do not force Selenium-compatible locator rewrites

## Mapping Checklist

When generating from a plan, verify:

- target slug matches naming
- flow names reflect business journeys
- helper + spec paths match plan metadata
- `flows-ts` mapping is updated for new journeys
- matching manual GitHub Actions workflow exists for new top-level Playwright flow
- JSON section names match plan metadata

## Update Existing Coverage

When plan updates an already-owned journey:

- extend the smallest current helper/spec pair that owns the behavior
- strengthen weak assertions instead of duplicating path coverage
- add JSON keys instead of hardcoded patch values
- keep ownership clear and maintainable

## New Coverage

Create new app-specific assets when:

- plan introduces a genuinely new journey
- extending current assets would blur ownership
- user explicitly wants a separate execution path
