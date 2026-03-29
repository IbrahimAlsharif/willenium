# API Generation Workflow

## When To Read This

Read this reference when the user wants to:

- generate API tests from an approved plan
- update existing API automation from an updated plan
- map API plan sections to Java, JSON, and XML artifacts

## Preconditions

Before generation starts:

- a Markdown plan should exist on disk
- `plan_scope` and `plan_type` should be clear
- the user should have had a chance to review the draft direction when the planning was ambiguous or new

## Source Of Truth

Treat the plan as the source of truth for:

- owned endpoints and services
- intended coverage depth
- artifact mapping
- open questions and assumptions
- auth, header, payload, and response expectations

## Framework Split

Translate the plan into the repo's native API structure:

- shared request setup in `src/test/java/base/ApiSetup.java`
- reusable request execution in `src/test/java/base/ApiClient.java`
- request or service helper logic in `src/test/java/tests/.../<Feature>Api.java`
- assertions in `src/test/java/tests/.../<Feature>ApiTest.java`
- dynamic inputs and expected values in `src/test/java/configs/testdata/...`
- suite wiring in `flows/...`

## Generation Rules

- keep shared request initialization in `base.ApiSetup`
- reuse `base.ApiClient` before hand-writing duplicated RestAssured request setup in feature tests
- keep assertions in `*ApiTest.java`
- keep helper classes focused on request construction and execution intent
- put endpoints, headers, payload fragments, auth inputs, and expected values in JSON instead of hardcoding them
- keep flow ownership obvious and minimal
- update existing helper or test assets when the plan says the behavior belongs there

## Mapping Checklist

When generating from a plan, verify that each area is reflected in code:

- target slug matches the file naming scheme
- API helper class path matches the plan
- API test class path matches the plan
- flow XML path matches the plan
- a matching manual GitHub Actions workflow exists for each new top-level flow or profile
- JSON section names match the plan
- suite composition still includes the relevant API setup suite

## Update Existing Coverage

When the plan updates an already-owned service or endpoint:

- extend the smallest current API helper and test pair that owns the behavior
- strengthen weak assertions instead of duplicating the same path elsewhere
- add new JSON keys instead of hardcoding patch-specific values
- keep the XML wiring obvious and minimal
