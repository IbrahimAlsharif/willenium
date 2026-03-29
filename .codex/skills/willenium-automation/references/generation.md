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
- prefer the higher-level shared helpers such as `Finder.get(...)`, `Finder.getClickable(...)`, `Go.click(...)`, `Go.type(...)`, and `Go.clickAndWait...` before adding custom waits or retry blocks
- keep assertions in `*Test.java`
- keep helper classes focused on actions, locators, and small state checks
- keep browser/runtime behavior property-driven through `configs.pipeline.PipelineConfig` instead of embedding wait or browser flags in feature code
- put expected UI text, URLs, credentials, filters, and inputs in JSON instead of hardcoding them
- preserve environment-and-language separation when the product supports both multiple languages and multiple environments
- default new bilingual flow generation to four synchronized JSON files: production arabic, production english, staging arabic, and staging english
- duplicate same-language production and staging content by default unless the user provides different values or the target URLs clearly indicate environment-specific content
- update all four files together whenever the JSON structure changes
- update existing helper/test/flow assets when the plan says the behavior belongs there

## Mapping Checklist

When generating from a plan, verify that each area is reflected in code:

- target slug matches the file naming scheme
- helper class path matches the plan
- test class path matches the plan
- flow XML path matches the plan
- a matching manual GitHub Actions workflow exists for each new top-level flow/profile
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
