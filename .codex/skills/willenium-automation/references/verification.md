# Verification Workflow

## When To Read This

Read this reference when the task reaches validation, smoke checking, or deciding what to run after plan/code changes.

## Verification Principle

Run the relevant Playwright path for the changed UI flow and fix failures before stopping.

Verification depth depends on request:

- plan-only work: validate Markdown draft quality and linkage
- localized automation update: run owning Playwright flow/spec and fix failures
- broader regression update: run primary owned path and note follow-up paths when needed

## Existing Entry Points

- `npm run pw:test`
- `npm run pw:test:home:production:english`
- `npm run pw:test:home:production:arabic`
- `npm run pw:test:home:staging:english`
- `npm run pw:test:home:staging:arabic`

## Verification From Plan Type

- `smoke`: run smallest critical path and fix failures
- `happy-path`: run main success journey and fix failures
- `regression`: run fix-owning path and adjacent safeguard when feasible
- `full`: validate primary path and note additional follow-up paths

## What To Record

After verification, report:

- what was run
- what was not run
- why chosen depth was appropriate
- notable artifacts (screenshots/videos/reports) when helpful
- residual risks/follow-up checks

If work is plan-only, say that execution was not run yet.

## Playwright MCP In Verification

Use Playwright MCP during verification when live clarification is needed, such as:

- confirming rendered text
- validating a locator
- reproducing hard-to-reason-about behavior

Do not leave deliverable as MCP-only session; translate result back into framework-native assets.
