# Verification Workflow

## When To Read This

Read this reference when the task reaches validation, smoke checking, or deciding what to run after plan or code changes.

## Verification Principle

Run the relevant suite for the changed UI flow and fix failures before stopping.

The correct verification depth depends on the request:

- plan-only work
  validate the Markdown draft quality and linkage
- localized automation update
  run the relevant owned flow or suite and fix failures before stopping
- broader regression update
  run the primary owned flow, fix failures, and note follow-up paths when needed

## Existing Entry Points

Current top-level examples:

- `mvn test -PProtectExampleHomeTrustEnglish`
- `mvn test -PProtectExampleHomeTrustArabic`

Shortest bundled smoke path:

- `example_quick_path.xml`

## Verification From Plan Type

Use plan type to shape validation expectations:

- `smoke`
  run the owned smoke path for the changed flow and fix failures before stopping
- `happy-path`
  run the main successful journey, confirm the core assertions, and fix failures before stopping
- `regression`
  run the owned path that proves the fix, fix failures, and cover any nearby risky branch if feasible
- `full`
  validate the primary owned path, fix failures, and note any additional paths that should follow if not run now

## What To Record

After verification, report:

- what was run
- what was not run
- why the chosen validation depth was appropriate
- notable evidence artifacts such as screenshots, page-source captures, or report paths when they materially help debugging
- any residual risks or follow-up checks

If the work is plan-only, say that code execution was not run yet.

## Selenium MCP In Verification

Use Selenium MCP during verification when live clarification is needed after the required planning inspection, such as:

- confirming rendered text
- validating a locator before encoding it in Java
- reproducing a hard-to-reason-about UI behavior

Do not leave the deliverable as an MCP-only session. Translate the result back into framework-native assets.
