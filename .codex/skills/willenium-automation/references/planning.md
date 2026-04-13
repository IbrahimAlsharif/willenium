# Planning Workflow

## When To Read This

Read this reference when the task involves:

- inspecting a URL/feature and turning it into a plan
- updating an existing plan
- deciding plan scope/plan type
- preparing a Markdown plan draft for user review

If input is still strategic (Lean Canvas, product brief, MVP, feature list), create/update a Quality Canvas under `quality/plans/...` before drafting executable plan under `test-plans/...`.

## Planning Contract

Before drafting/updating a plan, determine business context first, then planning dimensions.

Business questions first:

- what business outcome this journey protects
- who the primary user is
- what user value must be preserved
- what unacceptable outcome matters most
- what decision/confidence signal the resulting coverage should support

Then determine planning inputs:

- intended journey steps or feature scope
- `plan_scope` (`component`, `page`, `flow`, `journey`, `feature-area`, `multi-flow-regression`)
- `plan_type` (`smoke`, `happy-path`, `negative-path`, `edge-case-focused`, `regression`, `full`)

## Draft-First Rule

Expected sequence:

1. create/update Quality Canvas when needed
2. confirm business context
3. confirm journey steps/feature + plan type
4. inspect existing plans/flows/tests/test data
5. create/update Markdown draft
6. review direction with user
7. generate/update automation from approved plan

## When Planning Should Use UI MCP

Default approach:

1. confirm business context
2. confirm journey steps/feature + plan type
3. inspect local plans/flows/tests/test data
4. use Playwright MCP to navigate and investigate intended journey when live evidence materially improves plan quality
5. draft Markdown plan with findings aligned to business cases

Use live Playwright MCP during planning when:

- user explicitly asks to inspect a real target link
- page behavior is unclear from description
- rendered text/navigation/dynamic behavior needs confirmation
- live exploration meaningfully improves plan accuracy

When Playwright MCP is used:

- navigate journey in planned order
- behave like an expert test-case designer
- capture real decision points, trust signals, blockers, and recovery paths
- convert findings into concrete test cases
- align exploration depth with selected plan type

## Scope Guidance

Use the smallest scope that matches user intent.

- `component`: localized behavior within a page
- `page`: one page's content/actions
- `flow`: directed sequence in one area
- `journey`: end-to-end user outcome
- `feature-area`: multiple linked journeys
- `multi-flow-regression`: broader targeted regression set
