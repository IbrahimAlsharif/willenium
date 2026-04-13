# Framework Structure

## Runtime Shape

Willenium now uses a Playwright TypeScript runtime for UI automation and keeps Java/TestNG for API automation.

### UI runtime (Playwright)

1. `playwright.config.ts` defines profile projects (`production/staging` + `english/arabic`) and runtime settings.
2. `src/pw/core/runtime/RuntimeProfile.ts` resolves active profile and behavior settings.
3. `src/pw/core/data/TestDataFactory.ts` loads and validates profile-mapped JSON data.
4. `src/pw/core/helpers/Finder.ts` and `src/pw/core/helpers/Go.ts` provide shared UI helper patterns.
5. `src/pw/fixtures/willenium.fixture.ts` wires reusable fixtures for tests.
6. `src/pw/tests/...` contains assertion-focused Playwright specs.
7. `flows-ts/...` maps business-flow ownership for Playwright suites.

### API runtime (Java/TestNG)

- `base.ApiSetup`, `base.ApiClient`, `configs.api.ApiContext`
- API helpers in `src/test/java/tests/.../*Api.java`
- API assertions in `src/test/java/tests/.../*ApiTest.java`

## Key Paths

- UI plans: `test-plans/<app>/<target-slug>.md`
- Quality canvas artifacts: `quality/plans/<app>/<target-slug>-quality-canvas.md`
- UI tests: `src/pw/tests/...`
- UI helpers: `src/pw/core/helpers/...`
- UI test data: `src/pw/config/testdata/...`
- UI flow ownership: `flows-ts/...`
- API tests: `src/test/java/tests/...`
- API flow wiring: `flows/...`

## Test Data Rules (UI)

Use profile-based JSON files for UI assertions and inputs:

- `exampleProductionEnglish.json`
- `exampleProductionArabic.json`
- `exampleStagingEnglish.json`
- `exampleStagingArabic.json`

Keep all four files structurally identical even when values differ.

## Locator Strategy (Playwright)

Use Playwright-native locator strategies without Selenium compatibility constraints:

1. `getByRole` + accessible name
2. `getByLabel`
3. `getByPlaceholder`
4. `getByTestId`
5. stable CSS/XPath as fallback only

Do not rewrite strong Playwright locators to match another runtime.

## Execution Entry Points

- `npm run pw:test`
- `npm run pw:test:home:production:english`
- `npm run pw:test:home:production:arabic`
- `npm run pw:test:home:staging:english`
- `npm run pw:test:home:staging:arabic`

Use API Maven profiles only for API coverage.
