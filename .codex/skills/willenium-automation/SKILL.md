---
name: willenium-automation
description: Use when creating, updating, or debugging Java Selenium TestNG UI coverage in the Willenium framework, especially when work must follow base.Setup, base.Finder, base.Go, JSON test data, and TestNG XML suite composition, with Selenium MCP used for live browser exploration when needed.
---

# Willenium UI Selenium TestNG

Use this skill for browser-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.

Use `willenium-api` instead when the task is primarily API coverage, API planning, API debugging, or RestAssured/TestNG work.

Treat the checked-in tests, flows, and JSON data as starter examples of framework structure, not as the product under test. Their URLs, labels, and assertions are sample content unless the user explicitly says they are still the target application.

## Read These References

- For framework structure and file placement, read `references/framework-structure.md`.
- For test planning, plan scope, plan type, and Markdown draft workflow, read `references/planning.md`.
- For generation and update workflow after planning, read `references/generation.md`.
- For verification strategy and smallest-meaningful validation, read `references/verification.md`.
- For Jira MCP workflows and issue-to-test linkage, read `references/jira-mcp.md` when the task involves Jira.
- For the Selenium MCP contract and when to use it, read `references/selenium-mcp.md`.

## Core Workflow

1. Inspect the existing example feature, test data, suite wiring, and plan conventions before editing anything.
2. Route the work by stage and load the matching reference:
   - planning or plan updates -> `references/planning.md`
   - Jira-linked work -> `references/jira-mcp.md`
   - generation or automation updates -> `references/generation.md`
   - live browser clarification -> `references/selenium-mcp.md`
   - verification -> `references/verification.md`
3. Keep the plan as the source of truth for later generation and update work.
4. Keep the framework split intact:
   - setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
   - reusable browser actions in `base.Go`
   - reusable element lookup helpers in `base.Finder`
   - page/feature helpers in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
5. When the user asks for the first real test for a new product, create app-specific test data, helper classes, test classes, flows, and plans instead of building on the sample app names and sample URLs.
6. If keeping the examples in place would make the real project confusing, move or rename the sample assets so they are clearly marked as examples or starter content before adding the first real coverage.
7. Add or update JSON-backed test data before hardcoding user-facing strings, URLs, credentials, or inputs.
8. Register new or changed coverage in the appropriate TestNG XML suite under `flows/steps/...` or a composed suite under `flows/...`.
9. When a new top-level flow/profile is added, add or regenerate the matching manually triggered GitHub Actions workflow under `.github/workflows/`.
10. When a plan already exists for the target, update the linked plan and tests instead of creating duplicates.
11. Run the smallest relevant verification path that matches the plan type and request.

## Jira MCP Rules

Jira-linked work should follow the Jira reference and still remain plan-first.

## Framework Rules

- Do not create standalone Selenium scripts in JavaScript, Python, or raw MCP actions as the deliverable.
- Do not bypass `base.Setup`; UI tests assume shared static state (`driver`, `wait`, `testData`) is initialized by setup suites.
- Treat the checked-in WE WILL example flows as examples of structure and conventions, not as the required product namespace to extend.
- Prefer plan-first delivery when the user asks for planning, link inspection, or test generation from a target.
- Use `test-plans/` as the canonical planning area unless the user explicitly asks for a flow-local blueprint.
- When the user asks for a plan, always create or update the actual Markdown file. A chat-only plan is insufficient.
- Treat plan scope and plan type as required planning inputs, not optional polish.
- Do not default every plan to smoke coverage. Ask whether the user wants smoke, regression, or full coverage depth.
- For plan-first work, write the Markdown draft for user review before large generation steps.
- Keep plan names and generated assets related through the same stable slug or `plan_id` so later updates remain deterministic.
- For bug-driven work, prefer extending the smallest existing plan/flow/test that already owns the behavior before creating new artifacts.
- Prefer existing helper patterns over introducing a new page-object architecture.
- Reuse `Finder` and `Go` before adding direct `driver.findElement(...)` or custom wait code.
- Prefer the higher-level shared helpers such as `Finder.get(...)`, `Finder.getClickable(...)`, `Go.click(...)`, `Go.type(...)`, and `Go.clickAndWait...` before writing one-off synchronization or interaction fallback logic.
- Keep assertions in `*Test.java`; helper classes should expose actions, locators, and small state checks.
- Keep browser, wait, retry, and reporting toggles property-driven through `configs.pipeline.PipelineConfig` rather than hardcoding them in helper or test classes.
- Put expected UI text, URLs, credentials, and other user-facing values in JSON test data rather than hardcoding them in assertions.
- If the target app supports both English and Arabic, keep both language variants in test data and have assertions read from the active language file.
- If only one language is known today, still preserve the JSON structure so the second language can be added cleanly later.
- Use the existing JSON files only as examples of shape; do not treat their sample website links or labels as canonical values for a new client project.
- Do not commit personal Jira credentials, cloud IDs, account IDs, or customer tenant URLs into this public template; Jira tenant details should come from the user's authorized MCP client at runtime.
