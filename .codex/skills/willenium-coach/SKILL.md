---
name: willenium-coach
description: Use when the user needs coaching on how to work effectively with the Willenium framework agent, including choosing the right workflow, preparing the right inputs, shaping prompts, and deciding whether to inspect a link, write a plan, generate tests, debug a failure, or work from Jira.
---

# Willenium Coach

Use this skill when the user is unsure how to ask for work, what information to provide, or which Willenium workflow should come next.

This skill is for guidance and prompt shaping. It does not replace the execution skill:

- use `willenium-consultant` to challenge shallow requests, review planning quality, and decide whether the next step should be strategic framing, plan refinement, or execution
- use `quality-canvas` to create a reusable strategic quality artifact before detailed planning starts
- use `willenium-coach` to orient the user
- use `willenium-automation` for UI/browser work
- use `willenium-api` for API/service work
- use the `willenium` agent when invoking the repo agent directly

For Codex users, the practical control surface is the skill layer. Recommend direct skill invocation when that will make the next step clearer.

## What To Help With

- choosing the next best workflow
- deciding whether `willenium-consultant` should review the request before planning or generation
- turning a vague request into a strong implementation prompt
- identifying missing inputs before generation starts
- deciding whether the work should start with a Quality Canvas before test planning
- asking the right business questions before test planning starts
- helping the user choose test plan scope and plan type
- explaining the repo structure in plain language
- steering the user away from duplicate plans, flows, or tests

## Workflow Selection

Map the user to the smallest correct next step:

- if the request is shallow, risky, commercially vague, or likely to create false confidence:
  recommend `willenium-consultant` first
- if they have a URL and want coverage:
  recommend plan-first work
- if they have a Lean Canvas, product idea, project brief, MVP description, or feature list:
  recommend `quality-canvas` first, then detailed planning
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

Before those planning dimensions are finalized, help the user answer the business questions that justify the plan:

- what business outcome the journey or service protects
- who the primary user or actor is
- what user value must be preserved
- what failure would hurt the business or user most
- what misleading success state must be avoided
- what decision or confidence target the resulting coverage should support
- which path matters most for the first planning pass

For every new test plan request, explicitly help the user confirm:

- the intended user journey steps or the specific feature being planned
- the desired plan type such as smoke, happy-path, negative-path, edge-case-focused, regression, or full

## Inputs To Ask For

When useful, guide the user to provide:

- target app or feature name
- URL or environment
- endpoint, service name, or API base URL
- business goal or business outcome
- primary user or actor
- key risk or unacceptable outcome
- decision the team needs to make with the resulting coverage
- desired plan scope
- desired plan type
- intended user journey steps or feature scope
- auth or header requirements
- language coverage needed
- login or seeded-data requirements
- Jira issue key
- TestRail case IDs or run IDs
- screenshots or repro steps
- whether this is new coverage or an update to existing coverage

Do not overwhelm the user. Ask only for the next missing details that materially improve the work.

## Question UX

Ask in a clean, structured UI when the client supports it.

Preferred style:

- group questions by topic such as business context, target context, and coverage depth
- keep each prompt short and decision-oriented
- prefer labeled fields or short structured choices over a long paragraph of questions
- ask business questions before technical planning questions
- avoid asking about file names, Java classes, XML paths, or JSON sections during the initial planning conversation unless the user explicitly wants implementation details
- steer the user toward focused business test cases instead of broad assert-heavy test ideas

Fallback style when no structured UI is available:

- ask a short grouped set of business questions first
- ask planning depth and scope second
- leave implementation and file mapping for later

## Repo Explanation

Explain the repo in simple terms:

- `quality/plans/...` holds strategic quality artifacts such as a Quality Canvas
- `test-plans/...` holds the source-of-truth plans
- `flows/...` holds TestNG suite wiring
- `src/test/java/tests/...` holds helper classes and `*Test.java` or `*ApiTest.java`
- `src/test/java/configs/testdata/...` holds dynamic assertions and inputs
- `src/test/java/base/...` holds shared setup and execution helpers such as `Setup`, `ApiSetup`, `Go`, `Finder`, and `ApiClient`

Remind the user that:

- `willenium-consultant` is the right first skill when they need business-direction, governance, or a strategic review before execution
- a Quality Canvas is a good first step when the input is still strategic and not yet journey-specific
- plans come before generation for new or unclear targets
- business intent should be clarified before plan scope and plan type are locked
- journey steps or feature focus and plan type should be chosen before writing the plan
- Selenium MCP is optional during planning and should be used when live inspection would materially improve the draft and help align the real journey to the plan
- user-facing values should live in JSON, not hardcoded assertions
- API endpoints, headers, payload fragments, and expected response values should also live in JSON rather than test methods
- existing plans and flows should be updated before creating duplicates

## Prompt Patterns

Offer direct prompt upgrades like these:

- `Use willenium-consultant to review this request first and tell me whether it creates real business confidence or false confidence.`
- `Use willenium-consultant to upgrade this technical automation ask into a business-directed plan recommendation.`
- `Use willenium-consultant to review this test plan before generation and recommend what to automate now versus what to defer or inspect manually.`
- `Use willenium-coach to ask me the business questions for this journey in a clean UI before you write the test plan.`
- `Use quality-canvas to turn this Lean Canvas into a Quality Canvas artifact under quality/plans/ before any test-planning work starts.`
- `Use quality-canvas to convert this product brief into a four-quadrant Quality Canvas and keep assumptions clearly labeled.`
- `Use willenium-automation to inspect this URL from a business-journey perspective, then create a test plan under test-plans/ focused on business scenarios and real test cases.`
- `Use willenium-automation to create a business-first test plan under test-plans/ with focused test cases and only brief technical mapping.`
- `Use willenium-coach to clarify the business goal, user value, key risks, plan scope, and plan type before you write the Markdown draft.`
- `Use willenium-coach to decide whether this planning task should inspect the live page with Selenium MCP first or whether the Markdown draft can be written from the current information.`
- `Use willenium-automation to update the existing plan and linked tests for this regression instead of creating duplicate coverage.`
- `Use willenium-api to ask the business and contract questions for this endpoint first, then create a test plan under test-plans/ focused on coverage scenarios and test cases.`
- `Use willenium-api to update the existing API plan and linked tests for this service regression instead of creating duplicate coverage.`
- `Use willenium-coach to help me choose whether this API work should be planned as endpoint, service, contract, or integration-flow coverage and whether it should be smoke, regression, negative-path, or full.`
- `Use willenium-automation to read Jira bug ABC-123, decide which existing flows it affects, update the impacted plan, then update the linked tests.`
- `Use willenium-automation to read TestRail case C123 or run R45, decide which existing flows it maps to, update the impacted plan, then update the linked tests.`
- `Use willenium-automation to debug this failing flow and explain whether the fix belongs in the helper, assertion, test data, or XML suite.`

## Guardrails

- Do not jump straight to generation when the user still needs orientation.
- Do not jump straight to technical mapping when the business questions are still unanswered.
- Do not let planning default to smoke coverage when the user may need a fuller plan.
- Do not encourage one large test with many assertions when several focused business tests would be clearer.
- Do not invent tenant-specific Jira configuration for the user.
- Do not invent tenant-specific TestRail configuration for the user.
- Do not encourage hardcoded data or duplicate flows.
- Keep advice concrete, actionable, and tailored to the repo's conventions.
