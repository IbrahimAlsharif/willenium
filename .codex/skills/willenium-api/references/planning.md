# API Planning Workflow

## When To Read This

Read this reference when the task involves:

- inspecting an API target and turning it into a plan
- updating an existing API plan
- deciding plan scope or plan type
- preparing a Markdown plan draft for user review

If the source input is still a Lean Canvas, product idea, project description, MVP description, or feature list, create or update a Quality Canvas under `quality/plans/...` before drafting the executable API plan under `test-plans/...`.

## Planning Contract

Before drafting or updating a plan, first determine the business and contract context, then determine the planning dimensions.

Business questions to answer first:

- what business workflow or system outcome this API coverage protects
- who the primary actor or consuming system is
- what value or contract must be preserved
- what unacceptable outcome matters most
- what decision the team needs the resulting coverage to support
- which request or integration path matters most in the first pass

Then determine two planning inputs:

- the intended feature, contract slice, or integration steps being planned
- `plan_scope`
  Example values: `endpoint`, `service`, `contract`, `integration-flow`, `multi-service-regression`
- `plan_type`
  Example values: `smoke`, `happy-path`, `negative-path`, `edge-case-focused`, `regression`, `full`

For every new API test plan request, explicitly ask for or confirm the intended feature, contract slice, or integration steps and the plan type before proceeding.
If either is unclear, ask a concise follow-up before proceeding.

Prefer asking these questions in a structured UI when the client supports it:

- keep business and contract questions together
- keep feature or integration steps plus plan type in a second short group
- avoid leading with helper names, flow files, or suite structure

## Draft-First Rule

The first planning deliverable should be a Markdown draft on disk under:

- `test-plans/<app>/<target-slug>.md`

The expected sequence is:

1. If needed, create or update the Quality Canvas for the target.
2. Confirm the business and contract context.
3. Confirm the feature, contract slice, or integration steps and the plan type.
4. Inspect existing plans, flows, tests, and test data.
5. Create or update the Markdown draft.
6. Let the user review the draft direction.
7. Generate or update automation from the approved plan.

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
- `quality_canvas`
- `plan_scope`
- `plan_type`
- `business_goal`
- `user_value`
- `confidence_target`
- `unacceptable_outcomes`
- `scenario_map`
- `intensity_level`
- `flow_xml`
- `java_helper`
- `java_test`
- `testdata_sections`
- `status`

For API work, make the Java and test data fields point to `*Api.java`, `*ApiTest.java`, and the relevant `api` JSON sections.

## Draft Content Expectations

A reviewable API draft should cover:

- summary
- business goal
- user value
- confidence target
- scope
- plan type
- out-of-scope boundaries
- assumptions and open questions
- unacceptable outcomes
- impact analysis when relevant
- environment and setup needs
- auth and header needs
- request data and payload needs
- business and contract test cases for primary, alternate, negative, and edge paths
- scenario map
- secondary implementation notes
- verification strategy
- review status

Keep the plan centered on behavior, contracts, and real test cases.
Do not let file structure or framework mapping dominate the plan body.
Keep implementation mapping brief and secondary.
Prefer a larger number of focused business or contract test cases over a smaller number of broad cases that bundle many expectations together.
Each planned API test case should protect one clear contract promise, business behavior, or failure mode.
