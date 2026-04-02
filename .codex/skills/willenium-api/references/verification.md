# API Verification Workflow

## When To Read This

Read this reference when the task reaches validation, smoke checking, or deciding what to run after API plan or code changes.

## Verification Principle

Run the relevant suite for the changed API flow and fix failures before stopping.

The correct verification depth depends on the request:

- plan-only work
  validate the Markdown draft quality and linkage
- localized API automation update
  run the relevant owned API flow or suite and fix failures before stopping
- broader regression update
  run the primary owned API flow, fix failures, and note follow-up paths when needed

## Existing Entry Points

Current top-level API example:

- `mvn test -PProtectExamplePublicApiContractEnglish`

## Verification From Plan Type

Use plan type to shape validation expectations:

- `smoke`
  run the owned smoke path for the changed flow and fix failures before stopping
- `happy-path`
  run the main successful request path, confirm the core assertions, and fix failures before stopping
- `regression`
  run the owned path that proves the fix, fix failures, and cover any nearby risky branch if feasible
- `negative-path`
  run the failing or rejected request path that proves the contract behavior and fix failures before stopping
- `full`
  validate the primary owned path, fix failures, and note any additional paths that should follow if not run now

## What To Record

After verification, report:

- what was run
- what was not run
- why the chosen validation depth was appropriate
- any residual risks or follow-up checks

If the work is plan-only, say that code execution was not run yet.
