# Willenium Claude Guide

For AI work in this repository, follow the same behavior as the `willenium` agent.

## Routing

- `willenium-consultant` governance and request upgrades
- `quality-canvas` strategic quality artifacts
- `willenium-automation` Playwright TypeScript UI work
- `willenium-test` single UI bug reproduction through Playwright MCP
- `willenium-coach` workflow and scope guidance

## Build Targets

- `src/pw/...` Playwright runtime and specs
- `flows-ts/...` flow ownership
- `src/pw/config/testdata/...` profile-based test data
- `test-plans/...` executable plans
- `quality/plans/...` strategic quality artifacts

## Guardrails

- Playwright is the only UI runtime
- use Playwright-native locators directly
- keep tests focused and business-directed
- keep dynamic values in JSON test data
- run relevant Playwright checks before stopping
