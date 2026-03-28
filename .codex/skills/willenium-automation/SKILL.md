---
name: willenium-automation
description: Use when creating, updating, or debugging Java Selenium TestNG coverage in the Willenium framework, especially when work must follow base.Setup, base.Finder, base.Go, JSON test data, and TestNG XML suite composition, with Selenium MCP used for live browser exploration when needed.
---

# Willenium Selenium TestNG

Use this skill for browser-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.

Treat the checked-in tests, flows, and JSON data as starter examples of framework structure, not as the product under test. Their URLs, labels, and assertions are sample content unless the user explicitly says they are still the target application.

## Read These References

- For framework structure and file placement, read `references/framework-structure.md`.
- For Jira MCP workflows and issue-to-test linkage, read `references/jira-mcp.md` when the task involves Jira.
- For the Selenium MCP contract and when to use it, read `references/selenium-mcp.md`.

## Core Workflow

1. Inspect the existing example feature, test data, suite wiring, and plan conventions before editing anything.
2. For a new target, create or update a comprehensive Markdown test plan first. Use `test-plans/<app>/<target-slug>.md` by default. Only place a Markdown blueprint near `flows/...` when the user explicitly asks for a flow-local planning artifact.
   - The planning task is incomplete until the `.md` file is written to disk.
   - Do not stop at a conversational summary; persist the plan file and mention its path in the final response.
   - If the target comes from a Jira bug, keep the plan linked to the issue through metadata such as `jira_issue_key` and `jira_issue_url`.
   - For Jira bugs, inspect existing plans, flows, tests, and test data first so you can update the right artifacts instead of creating duplicates.
3. Keep each plan linked to its generated automation through stable metadata such as:
   - `plan_id`
   - `target_name`
   - `target_url`
   - `target_slug`
   - Jira issue key and URL when the work starts from a bug
   - related flow XML path
   - related Java helper and test class names
   - related test-data section names
4. Treat the plan as the source of truth when the user later asks to generate tests or update existing generated coverage.
5. Keep the framework split intact:
   - setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
   - reusable browser actions in `base.Go`
   - reusable element lookup helpers in `base.Finder`
   - page/feature helpers in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
6. When the user asks for the first real test for a new product, create app-specific test data, helper classes, test classes, flows, and plans instead of building on the sample app names and sample URLs.
7. If keeping the examples in place would make the real project confusing, move or rename the sample assets so they are clearly marked as examples or starter content before adding the first real coverage.
8. Add or update JSON-backed test data before hardcoding user-facing strings, URLs, credentials, or inputs.
9. Register new or changed coverage in the appropriate TestNG XML suite under `flows/steps/...` or a composed suite under `flows/...`.
10. When a plan already exists for the target, update the linked plan and tests instead of creating duplicates.
11. Run the smallest relevant suite or Maven profile when feasible.

## Jira MCP Rules

If Jira work is part of the request and an Atlassian MCP-compatible client is available:

- use the repo's `atlassian` MCP server to read or create Jira issues
- treat Jira issue details as inputs to the plan, not as the final deliverable
- persist Jira linkage in the plan front matter so later updates can find the right automation assets
- analyze which existing flows, helper classes, test classes, and JSON sections the bug should affect before deciding to add new coverage
- keep bug filing and bug reading outside `base.Setup` and the Java runtime; the framework code should stay focused on tests

Typical Jira-driven flows:

1. Read a Jira bug.
2. Identify which existing plans, flows, tests, and test-data sections already own the affected journey.
3. Update the linked plan under `test-plans/...` with the impacted artifacts and the chosen coverage strategy.
4. Generate or update the Java helper, `*Test.java`, JSON data, and XML suite from that plan.
5. Optionally comment back on the Jira issue with the generated artifact paths or verification result.

## Framework Rules

- Do not create standalone Selenium scripts in JavaScript, Python, or raw MCP actions as the deliverable.
- Do not bypass `base.Setup`; UI tests assume shared static state (`driver`, `wait`, `testData`) is initialized by setup suites.
- Treat the checked-in WE WILL example flows as examples of structure and conventions, not as the required product namespace to extend.
- Prefer plan-first delivery when the user asks for planning, link inspection, or test generation from a target.
- Use `test-plans/` as the canonical planning area unless the user explicitly asks for a flow-local blueprint.
- When the user asks for a plan, always create or update the actual Markdown file. A chat-only plan is insufficient.
- Keep plan names and generated assets related through the same stable slug or `plan_id` so later updates remain deterministic.
- For bug-driven work, prefer extending the smallest existing plan/flow/test that already owns the behavior before creating new artifacts.
- Prefer existing helper patterns over introducing a new page-object architecture.
- Reuse `Finder` and `Go` before adding direct `driver.findElement(...)` or custom wait code.
- Keep assertions in `*Test.java`; helper classes should expose actions, locators, and small state checks.
- Put expected UI text, URLs, credentials, and other user-facing values in JSON test data rather than hardcoding them in assertions.
- If the target app supports both English and Arabic, keep both language variants in test data and have assertions read from the active language file.
- If only one language is known today, still preserve the JSON structure so the second language can be added cleanly later.
- Use the existing JSON files only as examples of shape; do not treat their sample website links or labels as canonical values for a new client project.
- Do not commit personal Jira credentials, cloud IDs, account IDs, or customer tenant URLs into this public template; Jira tenant details should come from the user's authorized MCP client at runtime.

## Selenium MCP Rules

Use Selenium MCP only when live browser interaction materially helps:

- exploring an unknown page flow
- validating a locator before encoding it in Java
- confirming dynamic text, visibility, or navigation behavior
- debugging a failing test that is hard to reason about from source alone

Do not use Selenium MCP as a substitute for the framework. After using MCP, translate the result back into:

- Java helper methods
- TestNG assertions
- JSON test data
- XML suite wiring

When using Selenium MCP:

1. Start from the repo's configured server name: `selenium`.
2. Prefer quick, targeted interactions over long exploratory sessions.
3. Close the MCP browser session when done.
4. Mirror validated locators in Java using the framework's existing style.

## Verification

- For existing top-level example profiles, prefer `mvn test -PBrowseExampleWeWillEnglish` or `mvn test -PBrowseExampleWeWillArabic`.
- For focused smoke validation, `example_quick_path.xml` is the shortest existing setup -> home-journey-check -> teardown path.
- If you add a new top-level suite, follow the same Surefire profile pattern already used in `pom.xml`.

## Plan Template

Use `test-plans/TEMPLATE.md` as the default starting point for new target plans.

Each plan should include:

- front matter with stable identifiers and linked artifact paths
- scope and out-of-scope boundaries
- assumptions and open questions
- environment and setup needs
- test data requirements
- happy-path, negative-path, and edge-case coverage
- localization notes when relevant
- explicit mapping to XML, Java, and JSON artifacts
