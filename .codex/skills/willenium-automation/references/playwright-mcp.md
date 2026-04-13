# UI MCP For Playwright-Based Output

## What It Is Good For

Use Playwright MCP as the standard live browser tool for UI planning, bug reproduction, locator validation, and behavior investigation.

Use it to:

- inspect real rendered DOM and behavior
- validate selectors and interaction order
- confirm navigation/visibility/text behavior
- reproduce flaky or reported issues
- confirm real journey branches and recovery paths

MCP interaction is discovery and evidence. Final deliverable must be framework-native Playwright assets.

## Business-First Exploration

Before selector work, inspect from business perspective:

- user intent on this page
- business outcome expected next
- trust signals vs drop-off signals
- misleading success states
- recovery behavior when ideal path fails

## Locator Strategy (Playwright-Native)

Preferred order:

1. `getByRole` + accessible name
2. `getByLabel`
3. `getByPlaceholder`
4. `getByTestId`
5. stable CSS/XPath fallback only when needed

Keep strong Playwright locators as-is. No Selenium compatibility translation is required.

## Recommended Usage Pattern

1. Confirm business goal and planned journey scope.
2. Navigate intended journey in Playwright MCP.
3. Capture checkpoints, blockers, and recovery states.
4. Validate locators using Playwright-native strategies.
5. Translate findings into:
   - `src/pw/core/helpers/...`
   - `src/pw/tests/...`
   - `src/pw/config/testdata/...`
   - `flows-ts/...`
   - `test-plans/...`
6. Close MCP session once evidence is complete.

## Guardrails

- Do not treat Playwright MCP as disconnected browsing.
- Do not optimize for selector hunting over business value.
- Do not stop at MCP notes; produce code/artifact updates when requested.
