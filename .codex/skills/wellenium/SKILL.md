---
name: wellenium
description: Use when creating, updating, or debugging Java Selenium TestNG coverage in the Willenium framework, especially when work must follow base.Setup, base.Finder, base.Go, JSON test data, and TestNG XML suite composition, with Selenium MCP used for live browser exploration when needed.
---

# Willenium Selenium TestNG

Use this skill for browser-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.

Treat the checked-in tests, flows, and JSON data as starter examples of framework structure, not as the product under test. Their URLs, labels, and assertions are sample content unless the user explicitly says they are still the target application.

## Read These References

- For framework structure and file placement, read `references/framework-structure.md`.
- For the Selenium MCP contract and when to use it, read `references/selenium-mcp.md`.

## Core Workflow

1. Inspect the existing example feature, test data, and suite wiring to learn the framework shape before editing anything.
2. Keep the framework split intact:
   - setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
   - reusable browser actions in `base.Go`
   - reusable element lookup helpers in `base.Finder`
   - page/feature helpers in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
3. When the user asks for the first real test for a new product, create app-specific test data, helper classes, test classes, and flows instead of building on the sample app names and sample URLs.
4. If keeping the examples in place would make the real project confusing, move or rename the sample assets so they are clearly marked as examples or starter content before adding the first real coverage.
5. Add or update JSON-backed test data before hardcoding user-facing strings, URLs, credentials, or inputs.
6. Register new or changed coverage in the appropriate TestNG XML suite under `flows/steps/...` or a composed suite under `flows/...`.
7. Run the smallest relevant suite or Maven profile when feasible.

## Framework Rules

- Do not create standalone Selenium scripts in JavaScript, Python, or raw MCP actions as the deliverable.
- Do not bypass `base.Setup`; UI tests assume shared static state (`driver`, `wait`, `testData`) is initialized by setup suites.
- Treat the checked-in WE WILL example flows as examples of structure and conventions, not as the required product namespace to extend.
- Prefer existing helper patterns over introducing a new page-object architecture.
- Reuse `Finder` and `Go` before adding direct `driver.findElement(...)` or custom wait code.
- Keep assertions in `*Test.java`; helper classes should expose actions, locators, and small state checks.
- Put expected UI text, URLs, credentials, and other user-facing values in JSON test data rather than hardcoding them in assertions.
- If the target app supports both English and Arabic, keep both language variants in test data and have assertions read from the active language file.
- If only one language is known today, still preserve the JSON structure so the second language can be added cleanly later.
- Use the existing JSON files only as examples of shape; do not treat their sample website links or labels as canonical values for a new client project.

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
