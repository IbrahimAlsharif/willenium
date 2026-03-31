# Willenium Claude Guide

For AI work in this repository, follow the same behavior as the `willenium` agent. Use the execution skill that matches the work:

- `quality-canvas` for early strategic quality framing before detailed test planning
- `willenium-automation` for UI/browser work
- `willenium-api` for API/service work
- `willenium-coach` when the user needs help choosing workflow, prompt shape, plan scope, or plan type first

Primary references:

- `.github/agents/willenium.agent.md`
- `.codex/skills/quality-canvas/SKILL.md`
- `.codex/skills/willenium-automation/SKILL.md`
- `.codex/skills/willenium-api/SKILL.md`
- `.codex/skills/willenium-coach/SKILL.md`

## What To Build

Build and modify tests in the repository's native Java/TestNG structure.

- Quality canvases: `quality/plans/...`
- Feature helpers: `src/test/java/tests/.../<Feature>.java`
- Assertions: `src/test/java/tests/.../<Feature>Test.java`
- API helpers: `src/test/java/tests/.../<Feature>Api.java`
- API assertions: `src/test/java/tests/.../<Feature>ApiTest.java`
- Test data: `src/test/java/configs/testdata/...`
- TestNG suites: `flows/...`

Treat the checked-in tests, flows, and test data as starter examples of structure only. Do not assume their sample app names, URLs, and assertions are the user's real target unless they say so.
Treat flows as business journeys first and TestNG execution assets second.

## How To Work

1. Inspect the starter examples to understand the framework shape.
2. If the work starts from a Lean Canvas, product idea, project description, MVP description, or feature list, create or update a Quality Canvas under `quality/plans/...` before detailed `test-plans/...` work starts.
3. For plan-first work, ask the business questions first, then confirm the user's desired plan scope and plan type before drafting the Markdown plan.
4. Write the plan draft under `test-plans/...` for user review before broad generation starts.
5. Use the matching framework conventions for the task:
   - UI: `base.Setup`, `base.Finder`, `base.Go`, and `base.TearDownTest`
   - API: `base.ApiSetup`, `base.ApiClient`, and `configs.api.ApiContext`
6. When writing or updating test classes, add short explanatory comments so users with limited coding experience can understand the purpose of each test and the main assertion blocks.
7. Use the repo's `atlassian` MCP server when Jira bugs should drive planning, bug filing, or test generation.
8. For Jira bugs, inspect which existing plans, flows, classes, and JSON data already own the affected journey before deciding whether to extend or create coverage.
9. Use the repo's `selenium` MCP server during planning or debugging only when live browser validation would materially improve the work.
10. When Selenium MCP is used, inspect user intent, trust signals, conversion blockers, and recovery paths before drilling down to selectors.
11. For the first real project test, create app-specific test data, flows, and classes instead of extending the bundled WE WILL example assets by default.
12. Move or rename the sample assets first if that keeps the real project clearer.
13. Translate any MCP findings into framework-native Java/TestNG changes.
14. Run the smallest relevant suite or profile when feasible.
15. When a new top-level flow/profile is added, add or regenerate the matching `workflow_dispatch` GitHub Actions workflow under `.github/workflows/`.

## Guardrails

- Do not produce standalone scripts in another language as the final artifact.
- Do not turn the Quality Canvas into a detailed test plan or a quality matrix.
- Do not bypass suite-driven setup assumptions for UI tests or API tests.
- Do not leave generated test classes uncommented; include short plain-language comments that help non-coders follow the intent.
- Do not hardcode user-facing strings, URLs, or expected assertions if they belong in JSON test data.
- Do not hardcode API endpoints, headers, payload fragments, or expected response values if they belong in JSON test data.
- Do not hardcode customer Jira site URLs, personal credentials, or tenant-specific identifiers into this public template.
- Do not draft every plan as smoke coverage by default; the user may need full page or flow planning.
- Do not name flows like generic execution buckets when the owned business journey can be expressed clearly.
- Do not let test plans collapse into file structure or implementation mapping; keep them centered on business scenarios and actual test cases.
- Do not use Selenium MCP automatically during planning when the Markdown draft can be written accurately from the current information.
- Keep assertions dynamic by reading expected values from the active JSON data file.
- If the target site supports English and Arabic, maintain environment-and-language-specific test data rather than mixing values in code, and let `branch` + `language` select the active file.
- Keep assertions in `*Test.java`.
- Keep Jira integration at the MCP/client layer; do not embed Jira calls in the Java execution path.
