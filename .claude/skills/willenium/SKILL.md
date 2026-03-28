---
name: willenium
description: Use when creating, updating, or debugging Java Selenium TestNG coverage in the Willenium framework, especially when work must follow base.Setup, base.Finder, base.Go, JSON test data, and TestNG XML suite composition, with Selenium MCP used for live browser exploration when needed.
---

# Willenium Selenium TestNG

Use this skill for browser-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.

Treat the checked-in tests, flows, and JSON data as starter examples of framework structure, not as the product under test. Their URLs, labels, and assertions are sample content unless the user explicitly says they are still the target application.

## Read These References

- For framework structure and file placement, read `references/framework-structure.md`.
- For the Selenium MCP contract and when to use it, read `references/selenium-mcp.md`.

## Core Workflow

1. Inspect the existing example feature, test data, suite wiring, and plan conventions before editing anything.
2. Keep the framework split intact:
   - setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
   - reusable browser actions in `base.Go`
   - reusable element lookup helpers in `base.Finder`
   - page/feature helpers in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
3. Follow a plan-first workflow when the user asks to inspect a link, write a plan, generate tests from a target, or update generated coverage:
   - confirm plan scope and plan type first
   - create or update the Markdown file under `test-plans/<app>/<target-slug>.md`
   - treat the saved Markdown plan as a draft for user review before broad generation
4. Add or update JSON-backed test data before hardcoding user-facing strings or inputs.
5. Register new or changed coverage in the appropriate TestNG XML suite under `flows/examples/steps/...`, another `flows/steps/...` area, or a composed suite under `flows/...`.
6. When the user asks for the first real test for a new product, create app-specific test data, helper classes, test classes, flows, and plans instead of building on the WE WILL example assets by default.
7. Run the smallest relevant suite or Maven profile when feasible.

## Framework Rules

- Do not create standalone Selenium scripts in JavaScript, Python, or raw MCP actions as the deliverable.
- Do not bypass `base.Setup`; UI tests assume shared static state (`driver`, `wait`, `testData`) is initialized by setup suites.
- Treat the checked-in WE WILL example flows as examples of structure and conventions, not as the required product namespace to extend.
- Use `test-plans/` as the canonical planning area unless the user explicitly asks for a flow-local blueprint.
- When the user asks for a plan, always create or update the actual Markdown file. A chat-only plan is insufficient.
- Treat plan scope and plan type as required planning inputs, not optional polish.
- Do not default every plan to smoke coverage. Ask whether the user wants smoke, regression, or full coverage depth.
- For bug-driven work, prefer extending the smallest existing plan, flow, or test that already owns the behavior before creating new artifacts.
- Prefer existing helper patterns over introducing a new page-object architecture.
- Reuse `Finder` and `Go` before adding direct `driver.findElement(...)` or custom wait code.
- Keep assertions in `*Test.java`; helper classes should expose actions, locators, and small state checks.
- Put expected UI text, URLs, credentials, and other user-facing values in JSON test data rather than hardcoding them in assertions.
- If the target app supports both English and Arabic, keep both language variants in test data and have assertions read from the active language file.
- Do not commit personal Jira credentials, cloud IDs, account IDs, or customer tenant URLs into this public template.

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

- For existing top-level profiles, prefer `mvn test -PBrowseExampleWeWillEnglish` or `mvn test -PBrowseExampleWeWillArabic`.
- For focused smoke validation, `example_quick_path.xml` is the shortest existing setup -> example flow -> teardown path.
- If you add a new top-level suite, follow the same Surefire profile pattern already used in `pom.xml`.
