# Planning Workflow

## When To Read This

Read this reference when the task involves:

- inspecting a URL or feature and turning it into a plan
- updating an existing plan
- deciding plan scope or plan type
- preparing a Markdown plan draft for user review

## Planning Contract

Before drafting or updating a plan, first determine the business context, then determine the planning dimensions.

Business questions to answer first:

- what business outcome this journey protects
- who the primary user is
- what value the user must successfully receive
- what unacceptable outcome or failure matters most
- what decision the team needs the resulting coverage to support
- which journey path matters most in the first pass

Then determine two planning inputs:

- `plan_scope`
  Example values: `component`, `page`, `flow`, `journey`, `feature-area`, `multi-flow-regression`
- `plan_type`
  Example values: `smoke`, `happy-path`, `regression`, `full`

If either is unclear, ask a concise follow-up before proceeding.

Interpret `flow` as a business flow and prefer `journey` when the requested coverage is really about an end-to-end user outcome that serves a business goal.

Prefer asking these questions in a structured UI when the client supports it:

- keep the business questions in one short group
- keep scope and depth in a second short group
- avoid leading with implementation or file-structure questions

## Draft-First Rule

The first planning deliverable should be a Markdown draft on disk under:

- `test-plans/<app>/<target-slug>.md`

Do not jump straight from discovery into broad test generation when the plan direction is still unclear.

The expected sequence is:

1. Confirm the business context.
2. Confirm scope and plan type.
3. Inspect existing plans, flows, tests, and test data.
4. Create or update the Markdown draft.
5. Let the user review the draft direction.
6. Generate or update automation from the approved plan.

## When Planning Should Use Selenium MCP

Planning does not automatically require Selenium MCP.

Default approach:

1. confirm business context
2. confirm scope and plan type
3. inspect local plans, flows, tests, and test data
4. draft the Markdown plan
5. use Selenium MCP only if live browser evidence would materially improve the plan

Use Selenium MCP during planning when:

- the user explicitly asks to inspect a real target link
- the page structure or behavior is unclear from the user's description
- rendered text, navigation, visibility, or dynamic content needs confirmation
- live exploration would meaningfully improve the plan's accuracy

Do not use Selenium MCP during planning when:

- the user's description is already sufficient to draft the plan
- the task is mainly updating an existing plan from a known bug or known coverage area
- scope and plan type are still undecided and live browsing would be premature

Practical examples:

- `Write a full test plan for this page`
  Selenium MCP may be useful if the page needs inspection.
- `Update this existing plan for Jira bug ABC-123`
  often no Selenium MCP at first; confirm the business context, desired scope, and plan type with the user, then decide whether live inspection is actually needed.
- `Inspect this target link and create the plan`
  Selenium MCP is likely useful.

## Scope Guidance

Use the smallest scope that matches the user intent:

- `component`
  for localized UI behavior within a larger page
- `page`
  for one page's content, layout, or controls
- `flow`
  for a directed sequence across one area of the app
- `journey`
  for a broader user outcome that may span multiple pages
- `feature-area`
  for a group of related flows or pages
- `multi-flow-regression`
  for cross-cutting fixes or regression coverage across several owned paths

## Plan Type Guidance

Use the depth that matches the requested confidence:

- `smoke`
  small confidence check that the main path still works
- `happy-path`
  successful-path validation with slightly broader assertions
- `regression`
  focused checks for bug fixes, edge conditions, or previously broken behavior
- `full`
  broader positive, negative, edge-case, and localization planning for the owned target

Do not assume every plan is a smoke plan. Page-level and flow-level requests may need full planning depth even before code exists.

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

For Jira-linked work, also include:

- `jira_issue_key`
- `jira_issue_url`
- `affected_flows`
- `affected_tests`
- `affected_testdata`

## Draft Content Expectations

A reviewable draft should cover:

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
- test data needs
- business test cases for the primary path, alternate paths, negative paths, and edge cases
- scenario map
- localization
- secondary implementation notes
- verification strategy
- review status

Keep the plan centered on business scenarios and actual test cases.
Do not let file paths, class names, or XML wiring dominate the main body of the plan.

## Update-vs-Create Rule

Before creating a new plan:

- inspect `test-plans/...` for an existing plan that already owns the journey
- inspect `flows/...` for the current suite owner
- inspect helper/test classes that may already encode the behavior

Prefer updating the smallest existing plan that already owns the behavior.

Create a new plan only when the behavior has no clean existing owner.
