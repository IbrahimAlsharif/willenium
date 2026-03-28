# Willenium Agent Guide

Use the `willenium` agent and the `willenium-automation` skill behavior for any AI-assisted browser automation work in this repository.

Primary source files:

- Codex execution skill: `.codex/skills/willenium-automation/SKILL.md`
- Codex coaching skill: `.codex/skills/willenium-coach/SKILL.md`
- Agent spec: `.github/agents/willenium.agent.md`

## Mission

Produce framework-native Java Selenium TestNG changes for this repo.

- Put planning artifacts in `test-plans/<app>/<target-slug>.md` by default.
- Put reusable browser logic in `src/test/java/tests/.../<Feature>.java`.
- Put assertions in `src/test/java/tests/.../<Feature>Test.java`.
- Keep setup and driver lifecycle in `base.Setup` and `base.TearDownTest`.
- Reuse `base.Finder` and `base.Go` before writing raw Selenium code.
- Update JSON-backed test data instead of hardcoding user-facing strings when possible.
- Register new coverage through TestNG XML suites under `flows/...`.

Treat the checked-in tests, flows, GitHub workflows, and JSON data as starter examples of framework structure. Their website links, labels, and assertions are sample content unless the user confirms they are the real target application.

## Selenium MCP

This repo includes a workspace MCP server in `.mcp.json` named `selenium`.

Use Selenium MCP only when live browser exploration materially helps:

- validate a locator
- inspect a dynamic page flow
- reproduce a flaky UI issue
- confirm rendered text, visibility, or navigation

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

## Working Rules

- Inspect the sample assets to learn the framework structure, but do not assume the user wants new work built on top of the bundled WE WILL example domain.
- When the user asks to inspect a link, write a test plan, generate tests from a target, or update generated coverage, follow a plan-first workflow.
- Before drafting a plan, ask for or confirm the user's desired plan scope and plan type.
- Use Selenium MCP during planning only when live inspection would materially improve the draft or the user explicitly asks to inspect the target link.
- Create the canonical plan at `test-plans/<app>/<target-slug>.md` unless the user explicitly asks for a Markdown blueprint next to the flow XML under `flows/...`.
- When the user asks for a plan, the work is not complete until the Markdown file is actually created or updated on disk.
- Do not satisfy a planning request with a chat-only response. Persist the plan as a `.md` file and report the saved path in the final reply.
- Treat the first planning deliverable as a draft Markdown plan for user review before broad generation starts.
- Make the plan comprehensive enough to drive later generation: include scope, assumptions, setup, test data, happy paths, negative paths, edge cases, localization notes, and explicit mapping to XML, Java, and JSON artifacts.
- Do not default every plan to smoke coverage; the user may want full test planning for a specific page or flow.
- Keep the plan linked to generated tests through stable metadata such as `plan_id`, `target_slug`, flow path, helper class, test class, and test-data sections.
- When the target comes from Jira, also persist `jira_issue_key` and `jira_issue_url` in the plan metadata so later updates can find the same bug-linked assets.
- For Jira bugs, update the existing plan and linked coverage when the behavior already belongs to an existing journey; create new artifacts only when there is no clean owner yet.
- If a plan already exists, update that plan and the linked tests rather than creating duplicate plans or duplicate test classes.
- When the first real project test is requested, create app-specific folders, flows, and JSON data; move or rename the examples first if that will keep the real project clearer.
- Keep UI tests suite-driven; do not bypass setup/teardown assumptions.
- Keep expected text, URLs, and other assertion inputs dynamic in `src/test/java/configs/testdata/...` instead of hardcoding them in test methods.
- If the real site has English and Arabic variants, maintain language-specific JSON data and read assertions from the active language file.
- Prefer the smallest meaningful verification path after changes.
- Final output should match existing repository conventions, not generic Selenium examples.
