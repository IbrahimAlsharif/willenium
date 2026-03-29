# Willenium Claude Guide

For AI work in this repository, follow the same behavior as the `willenium` agent. Use the execution skill that matches the work:

- `willenium-automation` for UI/browser work
- `willenium-api` for API/service work
- `willenium-coach` when the user needs help choosing workflow, prompt shape, plan scope, or plan type first

Primary references:

- `.github/agents/willenium.agent.md`
- `.codex/skills/willenium-automation/SKILL.md`
- `.codex/skills/willenium-api/SKILL.md`
- `.codex/skills/willenium-coach/SKILL.md`

## What To Build

Build and modify tests in the repository's native Java/TestNG structure.

- Feature helpers: `src/test/java/tests/.../<Feature>.java`
- Assertions: `src/test/java/tests/.../<Feature>Test.java`
- API helpers: `src/test/java/tests/.../<Feature>Api.java`
- API assertions: `src/test/java/tests/.../<Feature>ApiTest.java`
- Test data: `src/test/java/configs/testdata/...`
- TestNG suites: `flows/...`

Treat the checked-in tests, flows, and test data as starter examples of structure only. Do not assume their sample app names, URLs, and assertions are the user's real target unless they say so.

## How To Work

1. Inspect the starter examples to understand the framework shape.
2. For plan-first work, confirm the user's desired plan scope and plan type before drafting the Markdown plan.
3. Write the plan draft under `test-plans/...` for user review before broad generation starts.
4. Use the matching framework conventions for the task:
   - UI: `base.Setup`, `base.Finder`, `base.Go`, and `base.TearDownTest`
   - API: `base.ApiSetup`, `base.ApiClient`, and `configs.api.ApiContext`
5. Use the repo's `atlassian` MCP server when Jira bugs should drive planning, bug filing, or test generation.
6. For Jira bugs, inspect which existing plans, flows, classes, and JSON data already own the affected journey before deciding whether to extend or create coverage.
7. Use the repo's `selenium` MCP server during planning or debugging only when live browser validation would materially improve the work.
8. For the first real project test, create app-specific test data, flows, and classes instead of extending the bundled WE WILL example assets by default.
9. Move or rename the sample assets first if that keeps the real project clearer.
10. Translate any MCP findings into framework-native Java/TestNG changes.
11. Run the smallest relevant suite or profile when feasible.
12. When a new top-level flow/profile is added, add or regenerate the matching `workflow_dispatch` GitHub Actions workflow under `.github/workflows/`.

## Guardrails

- Do not produce standalone scripts in another language as the final artifact.
- Do not bypass suite-driven setup assumptions for UI tests or API tests.
- Do not hardcode user-facing strings, URLs, or expected assertions if they belong in JSON test data.
- Do not hardcode API endpoints, headers, payload fragments, or expected response values if they belong in JSON test data.
- Do not hardcode customer Jira site URLs, personal credentials, or tenant-specific identifiers into this public template.
- Do not draft every plan as smoke coverage by default; the user may need full page or flow planning.
- Do not use Selenium MCP automatically during planning when the Markdown draft can be written accurately from the current information.
- Keep assertions dynamic by reading expected values from the active JSON data file.
- If the target site supports English and Arabic, maintain language-specific test data rather than mixing values in code.
- Keep assertions in `*Test.java`.
- Keep Jira integration at the MCP/client layer; do not embed Jira calls in the Java execution path.
