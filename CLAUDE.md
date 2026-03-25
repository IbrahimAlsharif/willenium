# Wellenium Claude Guide

For AI work in this repository, follow the same behavior as the `wellenium` agent and skill:

- `.github/agents/wellenium.agent.md`
- `.codex/skills/wellenium/SKILL.md`

## What To Build

Build and modify tests in the repository's native Java/Selenium/TestNG structure.

- Feature helpers: `src/test/java/tests/.../<Feature>.java`
- Assertions: `src/test/java/tests/.../<Feature>Test.java`
- Test data: `src/test/java/configs/testdata/...`
- TestNG suites: `flows/...`

## How To Work

1. Inspect the nearest existing feature implementation and its XML suite wiring.
2. Use `base.Setup`, `base.Finder`, `base.Go`, and `base.TearDownTest` conventions.
3. Use the repo's `selenium` MCP server only when live browser validation is needed.
4. Translate any MCP findings into framework-native Java/TestNG changes.
5. Run the smallest relevant suite or profile when feasible.

## Guardrails

- Do not produce standalone scripts in another language as the final artifact.
- Do not bypass suite-driven setup assumptions for UI tests.
- Do not hardcode user-facing strings if they belong in JSON test data.
- Keep assertions in `*Test.java`.
