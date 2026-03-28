# Verification Workflow

## When To Read This

Read this reference when the task reaches validation, smoke checking, or deciding what to run after plan or code changes.

## Verification Principle

Prefer the smallest meaningful verification path that proves the change.

The correct verification depth depends on the request:

- plan-only work
  validate the Markdown draft quality and linkage
- localized automation update
  run the smallest relevant flow or suite
- broader regression update
  run the primary owned flow and note follow-up paths when needed

## Existing Entry Points

Current top-level examples:

- `mvn test -PBrowseExampleWeWillEnglish`
- `mvn test -PBrowseExampleWeWillArabic`

Shortest bundled smoke path:

- `example_quick_path.xml`

## Verification From Plan Type

Use plan type to shape validation expectations:

- `smoke`
  smallest owned path is usually enough
- `happy-path`
  run the main successful journey and confirm the core assertions
- `regression`
  run the owned path that proves the fix and any nearby risky branch if feasible
- `full`
  validate the primary owned path and note any additional paths that should follow if not run now

## What To Record

After verification, report:

- what was run
- what was not run
- why the chosen validation depth was appropriate
- any residual risks or follow-up checks

If the work is plan-only, say that code execution was not run yet.

## Selenium MCP In Verification

Use Selenium MCP only when verification needs live clarification such as:

- confirming rendered text
- validating a locator before encoding it in Java
- reproducing a hard-to-reason-about UI behavior

Do not leave the deliverable as an MCP-only session. Translate the result back into framework-native assets.
