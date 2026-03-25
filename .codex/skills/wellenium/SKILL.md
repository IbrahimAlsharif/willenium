---
name: wellenium
description: Use when creating, updating, or debugging Java Selenium TestNG coverage in the Willenium framework, especially when work must follow base.Setup, base.Finder, base.Go, JSON test data, and TestNG XML suite composition, with Selenium MCP used for live browser exploration when needed.
---

# Willenium Selenium TestNG

Use this skill for browser-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.

## Read These References

- For framework structure and file placement, read `references/framework-structure.md`.
- For the Selenium MCP contract and when to use it, read `references/selenium-mcp.md`.

## Core Workflow

1. Inspect the nearest existing feature in `src/test/java/tests/...` and the suite wiring in `flows/...` before editing anything.
2. Keep the framework split intact:
   - setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
   - reusable browser actions in `base.Go`
   - reusable element lookup helpers in `base.Finder`
   - page/feature helpers in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
3. Add or update JSON-backed test data before hardcoding user-facing strings or inputs.
4. Register new or changed coverage in the appropriate TestNG XML suite under `flows/steps/...` or a composed suite under `flows/...`.
5. Run the smallest relevant suite or Maven profile when feasible.

## Framework Rules

- Do not create standalone Selenium scripts in JavaScript, Python, or raw MCP actions as the deliverable.
- Do not bypass `base.Setup`; UI tests assume shared static state (`driver`, `wait`, `testData`) is initialized by setup suites.
- Prefer existing helper patterns over introducing a new page-object architecture.
- Reuse `Finder` and `Go` before adding direct `driver.findElement(...)` or custom wait code.
- Keep assertions in `*Test.java`; helper classes should expose actions, locators, and small state checks.
- Prefer the existing production JSON files as the richer reference for UI labels and expected text, while only editing the environment/language files that the scenario really needs.

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

- For existing top-level profiles, prefer `mvn test -PBrowseAssistantHomeEnglish` or `mvn test -PBrowseAssistantHomeArabic`.
- For focused smoke validation, `quick_path.xml` is the shortest existing setup -> login -> teardown path.
- If you add a new top-level suite, follow the same Surefire profile pattern already used in `pom.xml`.
