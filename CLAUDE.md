# Willenium Claude Guide

For AI work in this repository, follow the same behavior as the `wellenium` agent and skill:

- `.github/agents/wellenium.agent.md`
- `.codex/skills/wellenium/SKILL.md`

## What To Build

Build and modify tests in the repository's native Java/Selenium/TestNG structure.

- Feature helpers: `src/test/java/tests/.../<Feature>.java`
- Assertions: `src/test/java/tests/.../<Feature>Test.java`
- Test data: `src/test/java/configs/testdata/...`
- TestNG suites: `flows/...`

Treat the checked-in tests, flows, and test data as starter examples of structure only. Do not assume their sample app names, URLs, and assertions are the user's real target unless they say so.

## How To Work

1. Inspect the starter examples to understand the framework shape.
2. Use `base.Setup`, `base.Finder`, `base.Go`, and `base.TearDownTest` conventions.
3. Use the repo's `atlassian` MCP server when Jira bugs should drive planning, bug filing, or test generation.
4. For Jira bugs, inspect which existing plans, flows, classes, and JSON data already own the affected journey before deciding whether to extend or create coverage.
5. Use the repo's `selenium` MCP server only when live browser validation is needed.
6. For the first real project test, create app-specific test data, flows, and classes instead of extending the bundled WE WILL example assets by default.
7. Move or rename the sample assets first if that keeps the real project clearer.
8. Translate any MCP findings into framework-native Java/TestNG changes.
9. Run the smallest relevant suite or profile when feasible.

## Guardrails

- Do not produce standalone scripts in another language as the final artifact.
- Do not bypass suite-driven setup assumptions for UI tests.
- Do not hardcode user-facing strings, URLs, or expected assertions if they belong in JSON test data.
- Do not hardcode customer Jira site URLs, personal credentials, or tenant-specific identifiers into this public template.
- Keep assertions dynamic by reading expected values from the active JSON data file.
- If the target site supports English and Arabic, maintain language-specific test data rather than mixing values in code.
- Keep assertions in `*Test.java`.
- Keep Jira integration at the MCP/client layer; do not embed Jira calls in the Java execution path.
