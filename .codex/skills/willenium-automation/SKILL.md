---
name: willenium-automation
description: Use when creating, updating, or debugging Java Selenium TestNG UI coverage in the Willenium framework, especially when work must follow base.Setup, base.Finder, base.Go, JSON test data, and TestNG XML suite composition, with Selenium MCP used for live browser exploration when needed and TestRail MCP used for case linkage when relevant.
---

# Willenium UI Selenium TestNG

Use this skill for browser-test work in this repository. The implementation target is always the Java/TestNG framework in this repo, not ad hoc scripts in another language.
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
- For the Selenium MCP contract and when to use it, read `references/selenium-mcp.md`.
- Use TestRail MCP when the task starts from existing test cases, runs, or execution reporting expectations and keep that linkage at the agent/client layer rather than in Java runtime code.

## Core Workflow

1. Inspect the existing example feature, test data, suite wiring, and plan conventions before editing anything.
2. Route the work by stage and load the matching reference:
   - strategic review, request upgrade, or false-confidence check -> route first to `../willenium-consultant/SKILL.md`
   - planning or plan updates -> `references/planning.md`
   - Jira-linked work -> `references/jira-mcp.md`
   - generation or automation updates -> `references/generation.md`
   - live browser clarification -> `references/selenium-mcp.md`
   - verification -> `references/verification.md`
   - TestRail-linked work -> inspect the relevant TestRail cases or runs before creating duplicate framework assets
3. Keep the plan as the source of truth for later generation and update work.
4. Treat `flows/...` as the executable expression of a business journey, not merely a grouping of tests.
5. Keep the framework split intact:
   - setup/driver lifecycle in `base.Setup` and `base.TearDownTest`
   - reusable browser actions in `base.Go`
   - reusable element lookup helpers in `base.Finder`
   - page/feature helpers in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
6. When the user asks for the first real test for a new product, create app-specific test data, helper classes, test classes, flows, and plans instead of building on the sample app names and sample URLs.
7. If keeping the examples in place would make the real project confusing, move or rename the sample assets so they are clearly marked as examples or starter content before adding the first real coverage.
8. Add or update JSON-backed test data before hardcoding user-facing strings, URLs, credentials, or inputs.
9. Register new or changed coverage in the appropriate TestNG XML suite under `flows/steps/...` or a composed suite under `flows/...`.
10. When a new top-level flow/profile is added, add or regenerate the matching manually triggered GitHub Actions workflow under `.github/workflows/`.
11. When a plan already exists for the target, update the linked plan and tests instead of creating duplicates.
12. Run the smallest relevant verification path that matches the plan type and request.

## Jira MCP Rules

Jira-linked work should follow the Jira reference and still remain plan-first.

## Framework Rules

- Do not create standalone Selenium scripts in JavaScript, Python, or raw MCP actions as the deliverable.
- Do not bypass `base.Setup`; UI tests assume shared static state (`driver`, `wait`, `testData`) is initialized by setup suites.
- Treat the checked-in WE WILL example flows as examples of structure and conventions, not as the required product namespace to extend.
- Prefer plan-first delivery when the user asks for planning, link inspection, or test generation from a target.
- If the business objective, user outcome, risk posture, or confidence target is weak or missing, stop and route the work through `willenium-consultant` before drafting or generating.
- Use `test-plans/` as the canonical planning area unless the user explicitly asks for a flow-local blueprint.
- When the user asks for a plan, always create or update the actual Markdown file. A chat-only plan is insufficient.
- Before drafting the plan, ask or confirm the business questions that define the journey:
  - business goal
  - primary user
  - user value
  - key risk or unacceptable outcome
  - confidence target
- For every new UI test plan request, explicitly ask for or confirm the intended user journey steps or the specific feature being planned.
- Treat plan scope and plan type as required planning inputs, not optional polish.
- For every new UI test plan request, explicitly ask for or confirm the plan type such as smoke, happy-path, negative-path, edge-case-focused, regression, or full.
- Prefer asking those questions in a structured UI with short grouped prompts when the client supports it.
- Do not default every plan to smoke coverage. Ask whether the user wants smoke, regression, or full coverage depth.
- Do not treat rendered pages, clickable elements, or completed steps as sufficient proof of business success when trust, recovery, conversion, or support-cost risk still matters.
- For plan-first work, write the Markdown draft for user review before large generation steps.
- When planning through live browser navigation, behave like an expert practical test-case designer rather than a page describer.
- While exploring, convert observed journey steps, trust cues, blockers, misleading states, and recovery paths directly into concrete planned test cases.
- Let the selected `plan_type` control the shape of the test-case set so the resulting plan depth matches the user's intent.
- For `smoke`, keep only the smallest critical-path cases needed to prove the main user outcome still works.
- For `happy-path`, focus on successful-path cases with practical checkpoints and only minimal alternate-path coverage.
- For `negative-path`, focus mainly on failure, rejection, validation, and recovery-oriented cases.
- For `edge-case-focused`, focus mainly on unusual but realistic boundary and state-transition cases.
- For `regression`, focus mainly on the reported or historically risky behavior plus adjacent safeguards.
- For `full`, balance primary, alternate, negative, recovery, and edge cases with practical prioritization.
- Use business-oriented naming for flow titles, plan titles, and coverage descriptions so they read like owned journeys.
- Keep plan names and generated assets related through the same stable slug or `plan_id` so later updates remain deterministic.
- When work starts from TestRail, keep related identifiers such as case IDs or run IDs in plan metadata so later updates stay traceable.
- For bug-driven work, prefer extending the smallest existing plan/flow/test that already owns the behavior before creating new artifacts.
- For TestRail-linked work, prefer updating the existing owner plan/flow/test that already maps to the case or run instead of creating parallel coverage.
- Prefer existing helper patterns over introducing a new page-object architecture.
- Reuse `Finder` and `Go` before adding direct `driver.findElement(...)` or custom wait code.
- Prefer the higher-level shared helpers such as `Finder.get(...)`, `Finder.getClickable(...)`, `Go.click(...)`, `Go.type(...)`, and `Go.clickAndWait...` before writing one-off synchronization or interaction fallback logic.
- When selecting locators during Selenium MCP exploration or when translating MCP findings back into Java, prefer `id` first, then `name`, then stable CSS or semantic attributes, and use XPath only when a stable non-XPath locator is not available.
- Keep assertions in `*Test.java`; helper classes should expose actions, locators, and small state checks.
- Add short plain-language comments in generated or updated `*Test.java` files so low-code readers can follow what each test protects and what each assertion block is checking.
- Prefer many focused business tests over one large test with many assertions.
- Keep each test centered on one business outcome, checkpoint, or failure mode.
- Split a long journey into multiple tests when that makes failures clearer and reporting more useful.
- Keep browser, wait, retry, and reporting toggles property-driven through `configs.pipeline.PipelineConfig` rather than hardcoding them in helper or test classes.
- Put expected UI text, URLs, credentials, and other user-facing values in JSON test data rather than hardcoding them in assertions.
- Keep test plans focused on business context and focused test cases. Treat Java/XML/JSON mapping as secondary implementation detail, not the main body of the plan.
- Keep file structure and artifact mapping brief so the plan stays decision-useful for non-developers too.
- Keep planned test cases practical: each case should be executable, business-relevant, centered on one behavior or failure mode, and should avoid inflated low-value case counts.
- If the target app supports both English and Arabic, keep environment-and-language variants in test data and have assertions read from the active file selected by `branch` + `language`.
- For new flow generation, default to creating and updating four JSON files together: production arabic, production english, staging arabic, and staging english.
- When production and staging values are not separately specified, duplicate the same language's content across both environment files by default.
- Only let production and staging content diverge when the user explicitly says they differ or the inspected URLs clearly indicate environment-specific values.
- Keep all four JSON files structurally identical even when their values differ.
- If only one language is known today, still preserve the JSON structure so the second language can be added cleanly later.
- Use the existing JSON files only as examples of shape; do not treat their sample website links or labels as canonical values for a new client project.
- When using Selenium MCP, start by understanding the business goal, user intent, trust signals, and drop-off risks before drilling into technical selectors.
- When using Selenium MCP for planning, navigate the requested journey steps or feature area intentionally and align the findings back to the planned business cases and selected plan type.
- Do not commit personal Jira credentials, cloud IDs, account IDs, or customer tenant URLs into this public template; Jira tenant details should come from the user's authorized MCP client at runtime.
- Do not commit personal TestRail URLs, usernames, API keys, or project-specific identifiers into this public template; TestRail access belongs to workspace MCP config and runtime reporting configuration.
