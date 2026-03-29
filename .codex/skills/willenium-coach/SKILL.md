---
name: willenium-coach
description: Use when the user needs coaching on how to work effectively with the Willenium framework agent, including choosing the right workflow, preparing the right inputs, shaping prompts, and deciding whether to inspect a link, write a plan, generate tests, debug a failure, or work from Jira.
---

# Willenium Coach

Use this skill when the user is unsure how to ask for work, what information to provide, or which Willenium workflow should come next.

This skill is for guidance and prompt shaping. It does not replace the execution skill:

- use `willenium-coach` to orient the user
- use `willenium-automation` for UI/browser work
- use `willenium-api` for API/service work
- use the `willenium` agent when invoking the repo agent directly

## What To Help With

- choosing the next best workflow
- turning a vague request into a strong implementation prompt
- identifying missing inputs before generation starts
- helping the user choose test plan scope and plan type
- explaining the repo structure in plain language
- steering the user away from duplicate plans, flows, or tests

## Workflow Selection

Map the user to the smallest correct next step:

- if they have a URL and want coverage:
  recommend plan-first work
- if they already have a plan:
  recommend generation or update of linked assets
- if they have an endpoint or service and want coverage:
  recommend plan-first API work with `willenium-api`
- if they have a failing test:
  recommend debugging the smallest affected flow
- if they have a Jira issue:
  recommend issue read -> impact analysis -> plan update -> test update
- if they have a TestRail case or run:
  recommend case/run read -> impact analysis -> plan update -> test update or reporting alignment
- if they are new to the repo:
  explain the structure and give them a starter prompt

Always help the user clarify two planning dimensions before execution work starts:

- plan scope:
  for UI work use values like page, component, flow, journey, feature area, or multi-flow regression area
- plan scope:
  for API work use values like endpoint, service, contract, integration-flow, or multi-service regression
- plan type:
  for UI work use values like smoke, happy-path, regression, or full coverage
- plan type:
  for API work also consider negative-path coverage when validation of rejected requests or error contracts matters

## Inputs To Ask For

When useful, guide the user to provide:

- target app or feature name
- URL or environment
- endpoint, service name, or API base URL
- desired plan scope
- desired plan type
- auth or header requirements
- language coverage needed
- login or seeded-data requirements
- Jira issue key
- TestRail case IDs or run IDs
- screenshots or repro steps
- whether this is new coverage or an update to existing coverage

Do not overwhelm the user. Ask only for the next missing details that materially improve the work.

## Repo Explanation

Explain the repo in simple terms:

- `test-plans/...` holds the source-of-truth plans
- `flows/...` holds TestNG suite wiring
- `src/test/java/tests/...` holds helper classes and `*Test.java` or `*ApiTest.java`
- `src/test/java/configs/testdata/...` holds dynamic assertions and inputs
- `src/test/java/base/...` holds shared setup and execution helpers such as `Setup`, `ApiSetup`, `Go`, `Finder`, and `ApiClient`

Remind the user that:

- plans come before generation for new or unclear targets
- plan scope and plan type should be chosen before writing the plan
- Selenium MCP is optional during planning and should be used when live inspection would materially improve the draft
- user-facing values should live in JSON, not hardcoded assertions
- API endpoints, headers, payload fragments, and expected response values should also live in JSON rather than test methods
- existing plans and flows should be updated before creating duplicates

## Prompt Patterns

Offer direct prompt upgrades like these:

- `Use willenium-automation to inspect this URL, create a test plan under test-plans/, and list the helper, test, flow, and JSON artifacts it should map to.`
- `Use willenium-coach to help me choose the right plan scope and whether this should be a smoke, regression, or full test plan before you write the Markdown draft.`
- `Use willenium-coach to decide whether this planning task should inspect the live page with Selenium MCP first or whether the Markdown draft can be written from the current information.`
- `Use willenium-automation to update the existing plan and linked tests for this regression instead of creating duplicate coverage.`
- `Use willenium-api to create a test plan under test-plans/ for this endpoint, then map it to ApiSetup, a service helper, an ApiTest class, and JSON test data.`
- `Use willenium-api to update the existing API plan and linked tests for this service regression instead of creating duplicate coverage.`
- `Use willenium-coach to help me choose whether this API work should be planned as endpoint, service, contract, or integration-flow coverage and whether it should be smoke, regression, negative-path, or full.`
- `Use willenium-automation to read Jira bug ABC-123, decide which existing flows it affects, update the impacted plan, then update the linked tests.`
- `Use willenium-automation to read TestRail case C123 or run R45, decide which existing flows it maps to, update the impacted plan, then update the linked tests.`
- `Use willenium-automation to debug this failing flow and explain whether the fix belongs in the helper, assertion, test data, or XML suite.`

## Guardrails

- Do not jump straight to generation when the user still needs orientation.
- Do not let planning default to smoke coverage when the user may need a fuller plan.
- Do not invent tenant-specific Jira configuration for the user.
- Do not invent tenant-specific TestRail configuration for the user.
- Do not encourage hardcoded data or duplicate flows.
- Keep advice concrete, actionable, and tailored to the repo's conventions.
