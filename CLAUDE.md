# Willenium Claude Guide

For AI work in this repository, follow the same behavior as the `willenium` agent.

## Routing

- `willenium-consultant`: governance, request upgrades, confidence/risk judgment
- `quality-canvas`: strategic quality artifact before detailed planning
- `willenium-automation`: Playwright TypeScript UI work
- `willenium-test`: single UI bug reproduction through Playwright MCP
- `willenium-api`: Java RestAssured TestNG API work
- `willenium-coach`: workflow/prompt/scope guidance

## Build Targets

- UI coverage in `src/pw/...`
- UI flow ownership in `flows-ts/...`
- API coverage in `src/test/java/tests/...`
- API flow wiring in `flows/...`
- plans in `test-plans/...`
- quality artifacts in `quality/plans/...`

## UI Policy

Playwright is the UI standard for guidance and generation.

- use Playwright MCP for UI planning, bug reproduction, and locator validation
- use Playwright-native locators directly (`getByRole`, `getByLabel`, `getByPlaceholder`, `getByTestId`, then stable CSS/XPath fallback)
- do not force Selenium-compatible locator translation

## Planning Contract

Before writing/updating a plan, confirm:

1. business goal
2. primary actor
3. user value
4. key risk/unacceptable outcome
5. confidence target
6. journey steps or feature scope
7. plan scope and plan type

Planning requests are incomplete until the Markdown artifact is written to disk.

## Guardrails

- keep coverage business-directed, not checkbox-driven
- keep tests focused; avoid large assert-heavy methods
- keep dynamic user-facing values in JSON test data
- run relevant verification path and fix failures before stopping
- keep Jira/TestRail interactions in MCP/client layer only
- never commit tenant credentials or API keys
