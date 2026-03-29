---
name: willenium-api
description: Use when creating, updating, or debugging Java RestAssured TestNG API coverage in the Willenium framework, especially when work must follow base.ApiSetup, base.ApiClient, configs.api.ApiContext, JSON-backed test data, and TestNG XML suite composition.
---

# Willenium API TestNG

Use this skill for API-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.

Treat the checked-in tests, flows, and JSON data as starter examples of framework structure, not as the product under test. Their URLs, endpoints, labels, and assertions are sample content unless the user explicitly says they are still the target application.

## Read These References

- For API framework structure and file placement, read `references/framework-structure.md`.
- For API test planning, plan scope, plan type, and Markdown draft workflow, read `references/planning.md`.
- For generation and update workflow after planning, read `references/generation.md`.
- For verification strategy and smallest-meaningful validation, read `references/verification.md`.
- For Jira MCP workflows and issue-to-test linkage, read `../willenium-automation/references/jira-mcp.md` when the task involves Jira.

## Core Workflow

1. Inspect the existing example API feature, test data, suite wiring, and plan conventions before editing anything.
2. Route the work by stage and load the matching reference:
   - planning or plan updates -> `references/planning.md`
   - Jira-linked work -> `../willenium-automation/references/jira-mcp.md`
   - generation or automation updates -> `references/generation.md`
   - verification -> `references/verification.md`
3. Keep the plan as the source of truth for later generation and update work.
4. Keep the API framework split intact:
   - setup/shared request specification in `base.ApiSetup`
   - reusable request execution in `base.ApiClient`
   - request/response capture for reporting in `configs.api.ApiContext`
   - feature or service helpers in `tests/.../<Feature>Api.java`
   - assertions in `tests/.../<Feature>ApiTest.java`
5. When the user asks for the first real API test for a new product, create app-specific test data, helper classes, test classes, flows, and plans instead of building on the sample app names and sample endpoints.
6. If keeping the examples in place would make the real project confusing, move or rename the sample assets so they are clearly marked as examples or starter content before adding the first real coverage.
7. Add or update JSON-backed test data before hardcoding endpoints, headers, payloads, auth configuration, or expected values.
8. Register new or changed coverage in the appropriate TestNG XML suite under `flows/steps/...` or a composed suite under `flows/...`.
9. When a new top-level flow/profile is added, add or regenerate the matching manually triggered GitHub Actions workflow under `.github/workflows/`.
10. When a plan already exists for the target, update the linked plan and tests instead of creating duplicates.
11. Run the smallest relevant verification path that matches the plan type and request.

## Framework Rules

- Do not create standalone API scripts in JavaScript, Python, Postman collections, or raw curl snippets as the deliverable.
- Do not bypass `base.ApiSetup`; API tests should assume shared API state is initialized by setup suites.
- Treat the checked-in WE WILL example API flow as an example of structure and conventions, not as the required product namespace to extend.
- Prefer plan-first delivery when the user asks for planning, service inspection, endpoint coverage, or test generation from an API target.
- Use `test-plans/` as the canonical planning area unless the user explicitly asks for a flow-local blueprint.
- When the user asks for a plan, always create or update the actual Markdown file. A chat-only plan is insufficient.
- Treat plan scope and plan type as required planning inputs, not optional polish.
- Keep assertions in `*ApiTest.java`; helper classes should expose request construction and execution helpers.
- Put endpoints, headers, payload fragments, credentials, and expected values in JSON test data rather than hardcoding them in assertions.
- Preserve environment separation in test data so production and staging API values remain cleanly isolated.
- For hybrid UI plus API work, keep API setup and assertions in the API layer and only use the UI skill where browser behavior genuinely belongs.
- Do not commit personal Jira credentials, cloud IDs, account IDs, customer tenant URLs, or real API secrets into this public template; tenant and secret details should come from the user's runtime context.
