# API Verification Workflow

## When To Read This

Read this reference when the task reaches validation, smoke checking, or deciding what to run after API plan or code changes.

## Verification Principle

Prefer the smallest meaningful verification path that proves the change.

The correct verification depth depends on the request:

- plan-only work
  validate the Markdown draft quality and linkage
- localized API automation update
  run the smallest relevant API flow or suite
- broader regression update
  run the primary owned API flow and note follow-up paths when needed

## Existing Entry Points

Current top-level API example:

- `mvn test -PRunExamplePublicApiEnglish`

## Verification From Plan Type

Use plan type to shape validation expectations:

- `smoke`
  smallest owned path is usually enough
- `happy-path`
  run the main successful request path and confirm the core assertions
- `regression`
  run the owned path that proves the fix and any nearby risky branch if feasible
- `negative-path`
  run the failing or rejected request path that proves the contract behavior
- `full`
  validate the primary owned path and note any additional paths that should follow if not run now

## What To Record

After verification, report:

- what was run
- what was not run
- why the chosen validation depth was appropriate
- any residual risks or follow-up checks

If the work is plan-only, say that code execution was not run yet.
