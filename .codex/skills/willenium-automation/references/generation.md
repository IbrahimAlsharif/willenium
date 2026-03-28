# Generation Workflow

## When To Read This

Read this reference when the user wants to:

- generate tests from an approved plan
- update existing automation from an updated plan
- map plan sections to Java, JSON, and XML artifacts

## Preconditions

Before generation starts:

- a Markdown plan should exist on disk
- `plan_scope` and `plan_type` should be clear
- the user should have had a chance to review the draft direction when the planning was ambiguous or new

## Source Of Truth

Treat the plan as the source of truth for:

- owned user journeys
- intended coverage depth
- artifact mapping
- open questions and assumptions
- test-data needs

Do not improvise a different target or flow structure if the plan already defines it.

## Framework Split

Translate the plan into the repo's native structure:

- reusable browser logic in `src/test/java/tests/.../<Feature>.java`
- assertions in `src/test/java/tests/.../<Feature>Test.java`
- dynamic inputs and expected values in `src/test/java/configs/testdata/...`
- suite wiring in `flows/...`

Keep:

- setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
- shared interactions in `base.Go`
- shared lookup helpers in `base.Finder`

## Generation Rules

- reuse `Finder` and `Go` before adding raw Selenium code
- keep assertions in `*Test.java`
- keep helper classes focused on actions, locators, and small state checks
- put expected UI text, URLs, credentials, filters, and inputs in JSON instead of hardcoding them
- preserve English/Arabic separation when the product supports both
- update existing helper/test/flow assets when the plan says the behavior belongs there

## Mapping Checklist

When generating from a plan, verify that each area is reflected in code:

- target slug matches the file naming scheme
- helper class path matches the plan
- test class path matches the plan
- flow XML path matches the plan
- JSON section names match the plan
- suite composition still includes setup and teardown

## Update Existing Coverage

When the plan updates an already-owned journey:

- extend the smallest current helper/test pair that owns the behavior
- strengthen weak assertions instead of duplicating the same path elsewhere
- add new JSON keys instead of hardcoding patch-specific values
- keep the XML wiring obvious and minimal

## New Coverage

Create new app-specific assets when:

- the plan introduces a genuinely new journey
- extending the current helper or suite would make ownership confusing
- the user explicitly wants a separate execution path
