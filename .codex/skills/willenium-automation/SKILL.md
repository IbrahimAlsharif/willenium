---
name: willenium-automation
description: Use when creating, updating, or debugging Playwright TypeScript UI coverage in the Willenium framework, especially when work must follow the Playwright runtime under src/pw, JSON test data under src/pw/config/testdata, and flow ownership under flows-ts, with Playwright MCP used for live browser planning and locator validation.
---

# Willenium UI Playwright TypeScript

Use this skill for browser-test work in this repository.
The implementation target is the Playwright TypeScript runtime in this repo, not Selenium Java.
Treat flows as business journeys first and execution suites second.

Use `willenium-api` instead when the task is primarily API coverage, API planning, API debugging, or RestAssured/TestNG work.
Use `willenium-consultant` first when the request is vague, strategically weak, commercially shallow, high-risk, or likely to create false confidence before UI execution begins.

Treat the checked-in tests, flows, and JSON data as starter examples of framework structure, not as the product under test. Their URLs, labels, and assertions are sample content unless the user explicitly says they are still the target application.

In user-facing replies, stay friendly, clear, and supportive.
Use `✨` sparingly as Willenium's signature emoji when it helps the reply feel warm and inviting.
Explain recommendations in practical language, and when helpful, give the user a couple of clear next-step options so it feels easy to continue.

## Read These References

- For framework structure and file placement, read `references/framework-structure.md`.
- For test planning, plan scope, plan type, and Markdown draft workflow, read `references/planning.md`.
- For generation and update workflow after planning, read `references/generation.md`.
- For verification strategy and smallest-meaningful validation, read `references/verification.md`.
- For Jira MCP workflows and issue-to-test linkage, read `references/jira-mcp.md` when the task involves Jira.
- For Playwright MCP contract and locator strategy, read `references/playwright-mcp.md`.
- Use TestRail MCP when the task starts from existing test cases, runs, or execution reporting expectations and keep that linkage at the agent/client layer rather than in runtime test code.

## Core Workflow

1. Inspect existing plan/test/flow conventions before editing.
2. Route the work by stage and load the matching reference:
   - strategic review, request upgrade, or false-confidence check -> `../willenium-consultant/SKILL.md`
   - planning or plan updates -> `references/planning.md`
   - Jira-linked work -> `references/jira-mcp.md`
   - generation or automation updates -> `references/generation.md`
   - live browser clarification -> `references/playwright-mcp.md`
   - verification -> `references/verification.md`
3. Keep the plan as the source of truth for later generation and update work.
4. Treat `flows-ts/...` as the executable expression of a business journey.
5. Decompose each journey into reusable business checkpoints.
6. Reuse existing helper and fixture patterns in `src/pw/core` and `src/pw/fixtures` before introducing one-off page logic.
7. For real project work, create app-specific assets instead of extending sample names and URLs by default.
8. Add or update JSON-backed test data before hardcoding user-facing strings, URLs, credentials, or inputs.
9. Register new or changed coverage in `flows-ts/...` and matching Playwright-focused workflow files under `.github/workflows/...`.
10. When a plan already exists for the target, update linked assets instead of creating duplicates.
11. Run the smallest relevant verification path that matches the plan type and request.

## Framework Rules

- Do not create standalone scripts in other languages as the deliverable.
- Prefer plan-first delivery when the user asks for planning, link inspection, or test generation from a target.
- If business objective, user outcome, risk posture, or confidence target is weak or missing, route the work through `willenium-consultant` before drafting or generating.
- Use `test-plans/` as canonical planning area unless user explicitly asks for flow-local blueprint.
- When the user asks for a plan, always create/update the Markdown file on disk.
- Ask or confirm business goal, primary user, user value, key risk, and confidence target before drafting new UI plans.
- Ask or confirm journey steps/feature scope and plan type (`smoke`, `happy-path`, `negative-path`, `edge-case-focused`, `regression`, `full`) before drafting.
- Keep plans focused on business context and focused test cases; keep technical mapping secondary.
- Keep assertions in Playwright specs under `src/pw/tests/...`.
- Add short plain-language comments in generated or updated specs so low-code readers can follow what each test protects.
- Prefer many focused business tests over one assert-heavy test.
- Keep each test centered on one business outcome, checkpoint, or failure mode.
- Keep browser/runtime behavior configuration-driven through `playwright.config.ts` and runtime profile helpers.
- Put expected UI text, URLs, credentials, and user-facing values in JSON test data rather than hardcoding in tests.
- If the target app supports both English and Arabic, keep environment-and-language variants in test data and have assertions read from the active profile.
- Use Playwright locator strategies directly without Selenium compatibility constraints.
- Preferred locator order for Playwright code: `getByRole` with accessible names, then `getByLabel`, `getByPlaceholder`, `getByTestId`, stable CSS/XPath only when needed.
- Do not downgrade good Playwright locators for compatibility with other runtimes.
- When using Playwright MCP, start by understanding business goal, user intent, trust signals, and drop-off risks before drilling into selectors.
- Navigate requested journey steps intentionally and align findings to planned business cases and selected plan type.
- Do not commit personal Jira/TestRail credentials or tenant-specific secrets into this public template.
