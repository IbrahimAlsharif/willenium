# Playwright TypeScript Runtime

This folder contains the Playwright TypeScript runtime used during the Willenium UI migration.

## Core design

- `core/runtime/RuntimeProfile.ts`: resolves `branch` and `language` profile plus runtime wait/retry settings.
- `core/data/TestDataFactory.ts`: loads profile test data and enforces that all four profile files keep the same structure.
- `core/helpers/Finder.ts`: helper-first locator access with `id -> name -> css -> xpath` priority.
- `core/helpers/Go.ts`: action helpers (`navigate`, `click`, `type`) aligned with business checkpoints.
- `fixtures/willenium.fixture.ts`: shared fixtures for `runtimeProfile`, `testData`, `finder`, and `go`.

## Profile matrix

The runtime supports four profile combinations:

- `production-english`
- `production-arabic`
- `staging-english`
- `staging-arabic`

## Pilot journey

- Spec: `tests/examples/wewill/home/WeWillHomePage.spec.ts`
- Flow map: `../../flows-ts/examples/wewill/ProtectExampleHomeTrust.flow.ts`

## Run

```bash
npm install
npx playwright install --with-deps chromium
npm run pw:test:home:production:english
```
