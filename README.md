# Willenium

Willenium is a Playwright TypeScript framework for business-journey UI automation.

## Stack

- Node.js 18+
- TypeScript
- Playwright

## Project Layout

- `src/pw/core/runtime` runtime profile resolution
- `src/pw/core/data` profile-aware test data loading/validation
- `src/pw/core/helpers` shared UI helpers
- `src/pw/fixtures` reusable Playwright fixtures
- `src/pw/tests` assertion-focused specs
- `src/pw/config/testdata` profile test data files
- `flows-ts` flow ownership metadata
- `test-plans` executable planning artifacts
- `quality/plans` strategic quality artifacts

## Run

```bash
npm install
npx playwright install --with-deps chromium
npm run pw:typecheck
npm run pw:test
```

## Profile Runs

```bash
npm run pw:test:home:production:english
npm run pw:test:home:production:arabic
npm run pw:test:home:staging:english
npm run pw:test:home:staging:arabic
```

## Workflow

1. Plan first (`test-plans/...`).
2. Build/update Playwright coverage in `src/pw/...`.
3. Keep values in `src/pw/config/testdata/...`.
4. Run relevant Playwright suites and fix failures before stopping.

## MCP

Primary UI exploration and bug reproduction uses Playwright MCP.
