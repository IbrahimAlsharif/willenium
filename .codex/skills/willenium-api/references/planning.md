# API Planning Workflow

## When To Read This

Read this reference when the task involves:

- inspecting an API target and turning it into a plan
- updating an existing API plan
- deciding plan scope or plan type
- preparing a Markdown plan draft for user review

## Planning Contract

Before drafting or updating a plan, determine two inputs:

- `plan_scope`
  Example values: `endpoint`, `service`, `contract`, `integration-flow`, `multi-service-regression`
- `plan_type`
  Example values: `smoke`, `happy-path`, `regression`, `negative-path`, `full`

If either is unclear, ask a concise follow-up before proceeding.

## Draft-First Rule

The first planning deliverable should be a Markdown draft on disk under:

- `test-plans/<app>/<target-slug>.md`

The expected sequence is:

1. Confirm scope and plan type.
2. Inspect existing plans, flows, tests, and test data.
3. Create or update the Markdown draft.
4. Let the user review the draft direction.
5. Generate or update automation from the approved plan.

## Scope Guidance

Use the smallest scope that matches the user intent:

- `endpoint`
  for one route and its contract
- `service`
  for a group of related endpoints owned by the same service
- `contract`
  for a focused request and response validation slice
- `integration-flow`
  for a chained business flow across multiple API calls
- `multi-service-regression`
  for cross-cutting fixes or regression coverage across several services

## Plan Type Guidance

Use the depth that matches the requested confidence:

- `smoke`
  minimal health or main-path contract check
- `happy-path`
  successful request validation with core assertions
- `regression`
  focused checks for bug fixes, edge conditions, or previously broken behavior
- `negative-path`
  validation for rejected requests, permissions, invalid payloads, or error handling
- `full`
  broader positive, negative, edge-case, and environment-aware planning for the owned target

## Plan Metadata

Every plan should keep stable metadata that links it to automation assets.

Baseline fields:

- `plan_id`
- `target_name`
- `target_url`
- `target_slug`
- `app`
- `plan_scope`
- `plan_type`
- `flow_xml`
- `java_helper`
- `java_test`
- `testdata_sections`
- `status`

For API work, make the Java and test data fields point to `*Api.java`, `*ApiTest.java`, and the relevant `api` JSON sections.

## Draft Content Expectations

A reviewable API draft should cover:

- summary
- scope
- plan type
- out-of-scope boundaries
- assumptions and open questions
- impact analysis when relevant
- environment and setup needs
- auth and header needs
- request data and payload needs
- happy paths
- negative paths
- edge cases
- automation mapping
- verification strategy
- review status
