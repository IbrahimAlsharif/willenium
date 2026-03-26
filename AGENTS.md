# Willenium Agent Guide

Use the `wellenium` agent/skill behavior for any AI-assisted browser automation work in this repository.

Primary source files:

- Codex skill: `.codex/skills/wellenium/SKILL.md`
- Agent spec: `.github/agents/wellenium.agent.md`

## Mission

Produce framework-native Java Selenium TestNG changes for this repo.

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

## Working Rules

- Inspect the sample assets to learn the framework structure, but do not assume the user wants new work built on top of the bundled WE WILL example domain.
- When the first real project test is requested, create app-specific folders, flows, and JSON data; move or rename the examples first if that will keep the real project clearer.
- Keep UI tests suite-driven; do not bypass setup/teardown assumptions.
- Keep expected text, URLs, and other assertion inputs dynamic in `src/test/java/configs/testdata/...` instead of hardcoding them in test methods.
- If the real site has English and Arabic variants, maintain language-specific JSON data and read assertions from the active language file.
- Prefer the smallest meaningful verification path after changes.
- Final output should match existing repository conventions, not generic Selenium examples.
