# Planning Workflow

## When To Read This

Read this reference when the task involves:

- inspecting a URL or feature and turning it into a plan
- updating an existing plan
- deciding plan scope or plan type
- preparing a Markdown plan draft for user review

If the source input is still a Lean Canvas, product idea, project description, MVP description, or feature list, create or update a Quality Canvas under `quality/plans/...` before drafting the executable plan under `test-plans/...`.

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

- the intended user journey steps or the specific feature being planned
- `plan_scope`
  Example values: `component`, `page`, `flow`, `journey`, `feature-area`, `multi-flow-regression`
- `plan_type`
  Example values: `smoke`, `happy-path`, `negative-path`, `edge-case-focused`, `regression`, `full`

For every new test plan request, explicitly ask for or confirm the journey steps or feature and the plan type before proceeding.
If either is unclear, ask a concise follow-up before proceeding.
When the input is a journey, normalize the journey into reusable business-step candidates rather than leaving the steps as a loose narrative.
Prefer step candidates such as `homepage is ready`, `search returns relevant results`, and `selected result opens details` over tiny actions like `click search field` or over-broad labels like `complete journey`.

Interpret `flow` as a business flow and prefer `journey` when the requested coverage is really about an end-to-end user outcome that serves a business goal.

Prefer asking these questions in a structured UI when the client supports it:

- keep the business questions in one short group
- keep journey steps or feature plus plan type in a second short group
- avoid leading with implementation or file-structure questions

## Draft-First Rule

The first planning deliverable should be a Markdown draft on disk under:

- `test-plans/<app>/<target-slug>.md`

Do not jump straight from discovery into broad test generation when the plan direction is still unclear.

The expected sequence is:

1. If needed, create or update the Quality Canvas for the target.
2. Confirm the business context.
3. Confirm the journey steps or feature and the plan type.
4. Inspect existing plans, flows, tests, and test data.
5. Create or update the Markdown draft.
6. Let the user review the draft direction.
7. Generate or update automation from the approved plan.

## When Planning Should Use UI MCP

Planning does not automatically require live UI MCP exploration.

Default approach:

1. confirm business context
2. confirm the journey steps or feature and the plan type
3. inspect local plans, flows, tests, and test data
4. use Playwright MCP to navigate and investigate the intended journey when it is available and live evidence would materially improve the plan
5. use Selenium MCP when Selenium-runtime parity or bug reproduction needs a second check
6. draft the Markdown plan with live findings aligned to the business cases

Use live UI MCP during planning when:

- the user explicitly asks to inspect a real target link
- the page structure or behavior is unclear from the user's description
- rendered text, navigation, visibility, or dynamic content needs confirmation
- live exploration would meaningfully improve the plan's accuracy
- the team needs the actual journey steps, decision points, or recovery paths confirmed before finalizing the test cases

Do not use live UI MCP during planning when:

- the user's description is already sufficient to draft the plan
- the task is mainly updating an existing plan from a known bug or known coverage area
- journey steps or feature focus and plan type are still undecided and live browsing would be premature

When Playwright MCP or Selenium MCP is used during planning:

- navigate the journey in the same order the plan is expected to cover
- behave like an expert practical test-case designer while navigating, not like a generic page summarizer
- capture the real steps, decision points, trust signals, blockers, misleading states, and recovery paths
- turn those findings into concrete planned test cases, not just notes or selectors
- align exploration depth with the chosen plan type so a smoke plan stays lean and a full plan investigates deeper paths
- keep each planned case executable, business-relevant, and centered on one behavior or failure mode rather than inflating the plan with low-value case counts

Practical examples:

- `Write a full test plan for this page`
  Playwright MCP may be useful if the page needs inspection.
- `Update this existing plan for Jira bug ABC-123`
  often no live UI MCP at first; confirm the business context, intended journey steps or feature, and desired plan type with the user, then decide whether live inspection is actually needed.
- `Inspect this target link and create the plan`
  Playwright MCP is likely useful.

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
- `negative-path`
  failure, rejection, validation, and recovery-oriented coverage as the primary output
- `edge-case-focused`
  unusual but realistic boundary and state-transition coverage as the primary output
- `regression`
  focused checks for bug fixes, historically risky behavior, or adjacent safeguards around the known problem area
- `full`
  broader positive, negative, edge-case, and localization planning for the owned target

Do not assume every plan is a smoke plan. Page-level and flow-level requests may need full planning depth even before code exists.
Make the planned test-case set explicitly match the chosen `plan_type`:

- `smoke`
  produce only the smallest set of critical-path cases needed to prove the main user outcome still works
- `happy-path`
  produce successful-path cases with practical checkpoints and only minimal alternate-path coverage
- `negative-path`
  make failure, rejection, validation, and recovery-oriented cases the primary output
- `edge-case-focused`
  make unusual but realistic boundary and state-transition cases the primary output
- `regression`
  focus the cases on the reported or historically risky behavior plus adjacent safeguards
- `full`
  balance primary, alternate, negative, recovery, and edge cases with practical prioritization

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
Keep implementation mapping brief and secondary.
Prefer a larger number of focused business test cases over a smaller number of broad cases that bundle many expectations together.
Each planned test case should protect one clear business behavior, outcome, or failure mode.
Treat browser exploration as an input to expert test-case generation: the plan should show what the live navigation discovered and how those discoveries changed the selected test cases for the chosen `plan_type`.
When useful, add a short step map that shows how the journey decomposes into reusable business steps and which of those steps should map to `flows/steps/...` for later composition.

## Update-vs-Create Rule

Before creating a new plan:

- inspect `test-plans/...` for an existing plan that already owns the journey
- inspect `flows/...` for the current suite owner
- inspect helper/test classes that may already encode the behavior

Prefer updating the smallest existing plan that already owns the behavior.

Create a new plan only when the behavior has no clean existing owner.
