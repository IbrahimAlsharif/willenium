# Willenium Agent Guide

Use the `willenium` agent for framework-native automation work in this repository.

Skill routing:

- use `quality-canvas` when the work starts from a Lean Canvas, product idea, project description, MVP description, or feature list and needs a reusable quality artifact before test planning
- use `willenium-automation` for UI/browser automation work
- use `willenium-api` for API/service automation work
- use `willenium-coach` when the user needs help choosing workflow, prompt shape, plan scope, or plan type

Primary source files:

- Codex quality canvas skill: `.codex/skills/quality-canvas/SKILL.md`
- Codex execution skill: `.codex/skills/willenium-automation/SKILL.md`
- Codex API execution skill: `.codex/skills/willenium-api/SKILL.md`
- Codex coaching skill: `.codex/skills/willenium-coach/SKILL.md`
- Agent spec: `.github/agents/willenium.agent.md`

## Mission

Produce framework-native Java Selenium TestNG and RestAssured TestNG changes for this repo.

Treat each flow as a business journey that protects a user outcome and a business objective.
The XML suite under `flows/...` is the executable representation of that journey, not just a bucket of tests.

- Put early strategic quality artifacts in `quality/plans/<app>/<target-slug>-quality-canvas.md` by default.
- Put planning artifacts in `test-plans/<app>/<target-slug>.md` by default.
- Put reusable browser logic in `src/test/java/tests/.../<Feature>.java`.
- Put assertions in `src/test/java/tests/.../<Feature>Test.java`.
- Put reusable API logic in `src/test/java/tests/.../<Feature>Api.java`.
- Put API assertions in `src/test/java/tests/.../<Feature>ApiTest.java`.
- When writing or updating test classes, add short explanatory comments so low-code readers can follow the intent of the test steps and assertions.
- Keep setup and driver lifecycle in `base.Setup` and `base.TearDownTest`.
- Keep API setup in `base.ApiSetup` and shared request execution in `base.ApiClient`.
- Reuse `base.Finder` and `base.Go` before writing raw Selenium code.
- Prefer the higher-level `Finder`/`Go` helpers such as `Finder.get(...)`, `Finder.getClickable(...)`, `Go.click(...)`, `Go.type(...)`, and `Go.clickAndWait...` before adding custom waits or retry logic.
- Update JSON-backed test data instead of hardcoding user-facing strings when possible.
- Register new coverage through TestNG XML suites under `flows/...`.
- Add or regenerate a matching manually triggered GitHub Actions workflow for each new top-level flow/profile under `.github/workflows/`.

Treat the checked-in tests, flows, GitHub workflows, and JSON data as starter examples of framework structure. Their website links, labels, and assertions are sample content unless the user confirms they are the real target application.

## Selenium MCP

This repo includes a workspace MCP server in `.mcp.json` named `selenium`.

Use Selenium MCP only when live browser exploration materially helps:

- validate a locator
- inspect a dynamic page flow
- reproduce a flaky UI issue
- confirm rendered text, visibility, or navigation
- understand the business intent of the page or journey
- identify trust signals, conversion blockers, misleading success states, or recovery paths

When Selenium MCP is used for planning or refinement, analyze the page from a business view first:

- what user intent brought the visitor here
- what the business needs the user to accomplish next
- which signals build or reduce trust
- where drop-off is likely
- which checkpoints matter more than raw element presence

Do not treat MCP interactions as the final deliverable. Translate findings back into Java/TestNG framework code.

## Jira MCP

This repo also includes a workspace MCP server in `.mcp.json` named `atlassian`.

Use Atlassian MCP when Jira work materially helps:

- read an existing bug before planning or generating tests
- create a Jira bug from a reproduced failure or regression
- add comments or status updates that point back to generated framework artifacts

For bug-driven automation, do impact analysis first:

- identify which existing plans already describe the affected journey
- identify which XML flows already execute that journey
- identify which helper and `*Test.java` classes already own the behavior
- identify which JSON sections already contain the relevant assertions or inputs

Keep Jira MCP at the agent/client layer. Do not add Jira calls to `base.Setup`, `base.Go`, `base.Finder`, or TestNG execution code.
Do not commit personal credentials, cloud IDs, account IDs, or customer Jira site URLs into this public template. The actual Jira tenant should come from the user's own MCP client authorization when they use the framework.

## TestRail MCP

This repo can also use a workspace MCP server in `.mcp.json` named `testrail`.

Use TestRail MCP when TestRail work materially helps:

- read existing cases, plans, or runs before planning or generating tests
- map generated framework coverage back to TestRail case IDs or run IDs
- coordinate execution-report expectations with existing TestRail organization

For TestRail-driven automation, do impact analysis first:

- identify which existing plans already map to the requested case or run
- identify which XML flows already execute that coverage
- identify which helper and `*Test.java` or `*ApiTest.java` classes already own the behavior
- identify which JSON sections already contain the relevant assertions or inputs

Keep TestRail MCP at the agent/client layer. Do not add TestRail calls to `base.Setup`, `base.Go`, `base.Finder`, `base.ApiSetup`, or TestNG execution code.
Do not commit personal TestRail URLs, usernames, API keys, or customer workspace details into this public template. The actual TestRail tenant should come from the user's own MCP client authorization or local environment when they use the framework.

## Working Rules

- Inspect the sample assets to learn the framework structure, but do not assume the user wants new work built on top of the bundled WE WILL example domain.
- When the work starts from a Lean Canvas, product idea, project description, MVP description, or feature list, treat `quality-canvas` as the recommended first step before detailed `test-plans/...` work starts.
- Use the Quality Canvas to capture the early strategic layer in `quality/plans/...`, then use that artifact to inform later `test-plans/...` work.
- Keep the Quality Canvas artifact-oriented: persist a Markdown file on disk instead of answering only in chat.
- Keep the first version of the Quality Canvas lightweight: four quadrants, concise assumptions, no generated tests, and no quality matrix.
- Do not force a Quality Canvas for every request. Small bug-driven updates or direct edits to an existing owned journey can proceed by updating the existing plan and linked coverage.
- When the user asks to inspect a link, write a test plan, generate tests from a target, or update generated coverage, follow a plan-first workflow.
- If test planning starts from high-level product inputs rather than an already-defined journey, create or update the Quality Canvas first, then ask or confirm the business questions for the specific executable plan.
- Before drafting a plan, ask or confirm the business questions first:
  - business goal
  - primary user or actor
  - user value
  - key risk or unacceptable outcome
  - confidence target
- Then ask for or confirm the user's desired plan scope and plan type.
- Prefer a clean, structured question UI with short grouped prompts when the client supports it.
- Treat `journey` as the preferred scope when the user is describing a business outcome that spans one or more pages.
- Use Selenium MCP during planning only when live inspection would materially improve the draft or the user explicitly asks to inspect the target link.
- Create the canonical plan at `test-plans/<app>/<target-slug>.md` unless the user explicitly asks for a Markdown blueprint next to the flow XML under `flows/...`.
- Create the canonical Quality Canvas at `quality/plans/<app>/<target-slug>-quality-canvas.md` unless the user explicitly asks for another location.
- When the user asks for a plan, the work is not complete until the Markdown file is actually created or updated on disk.
- Do not satisfy a planning request with a chat-only response. Persist the plan as a `.md` file and report the saved path in the final reply.
- Treat the first planning deliverable as a draft Markdown plan for user review before broad generation starts.
- Make the plan comprehensive enough to drive later generation: include business goal, user value, scope, assumptions, setup, test data, actual test cases, localization notes, unacceptable outcomes, scenario mapping, and only the minimum secondary implementation notes needed to connect it to the framework.
- Do not default every plan to smoke coverage; the user may want full test planning for a specific page or flow.
- Keep the plan linked to generated tests through stable metadata such as `plan_id`, `target_slug`, flow path, helper class, test class, and test-data sections.
- Prefer business-care naming for flows and plans so the name reads like the owned journey rather than a technical grouping.
- Keep the main body of the plan focused on business scenarios and test cases rather than file structure.
- When the target comes from Jira, also persist `jira_issue_key` and `jira_issue_url` in the plan metadata so later updates can find the same bug-linked assets.
- When the target comes from TestRail, also persist `testrail_case_ids` and `testrail_run_ids` in the plan metadata so later updates can find the same TestRail-linked assets.
- For Jira bugs, update the existing plan and linked coverage when the behavior already belongs to an existing journey; create new artifacts only when there is no clean owner yet.
- If a plan already exists, update that plan and the linked tests rather than creating duplicate plans or duplicate test classes.
- When the first real project test is requested, create app-specific folders, flows, and JSON data; move or rename the examples first if that will keep the real project clearer.
- Keep UI tests suite-driven; do not bypass setup/teardown assumptions.
- Prefer one short comment per test or assertion block that explains the business intent in plain language, not only the code action.
- Keep UI execution behavior property-driven through `configs.pipeline.PipelineConfig` instead of hardcoding browser or wait behavior in tests.
- Keep API tests suite-driven; do not bypass `base.ApiSetup`.
- When a new top-level flow/profile is added, also add or regenerate the matching `workflow_dispatch` GitHub Actions workflow.
- Keep expected text, URLs, and other assertion inputs dynamic in `src/test/java/configs/testdata/...` instead of hardcoding them in test methods.
- If the real site has English and Arabic variants, maintain environment-and-language-specific JSON data and read assertions from the active file selected by `branch` + `language`.
- For new flow generation, default to four JSON files: `Production Arabic`, `Production English`, `Staging Arabic`, and `Staging English`.
- When only one environment's content is known for a language, duplicate that language's content into the other environment file by default.
- Only diverge production and staging content when the user explicitly provides different values or the target URLs clearly indicate different environment-specific data should be captured.
- Keep all four JSON files structurally identical even when their values differ.
- Prefer the smallest meaningful verification path after changes.
- Final output should match existing repository conventions, not generic Selenium examples.
