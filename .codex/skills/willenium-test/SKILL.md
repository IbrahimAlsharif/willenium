---
name: willenium-test
description: Use when testing a reported UI bug from a Jira link, Jira key, or user description, especially when the goal is to reproduce or disprove the issue in a live browser with Selenium MCP, assess it like an experienced business-aware tester, and produce a concise evidence-backed report with screenshots and optional Jira updates.
---

# Willenium Test

Use this skill to investigate one reported UI bug at a time.
The first deliverable is a reproduction verdict with screenshots and a saved report, not framework-native Java automation.

Use `willenium-consultant` first when the request is vague enough that the business impact, user outcome, or failure cost is still unclear.
Use `willenium-automation` after this skill when the user wants the confirmed behavior translated into Java/TestNG coverage.

## Accepted Inputs

- Jira issue key
- Jira issue URL
- user-written bug description without Jira

Treat a non-Jira bug as `MANUAL` for reporting.

## Required Tools

- Jira MCP for Jira issue reading, screenshot attachment upload, and comment posting
- Selenium MCP for live browser reproduction in headed mode

This skill is the intentional Selenium-first exception in the repo. Use Selenium MCP here even though Playwright MCP is preferred for planning and locator discovery in `willenium-automation`.

## Save Paths

- screenshots: `reports/bug-<ISSUE_KEY>-step-<N>.png`
- report: `reports/<ISSUE_KEY>-bug-test-report.html`
- reusable template: `assets/bug-test-report-template.html`

Create `reports/` if it does not exist.

## Workflow

1. Get the bug details.
   - If the input is a Jira key or Jira URL, read the issue through Jira MCP.
   - Capture the summary, description, priority, status, assignee, expected behavior, actual behavior, and reproduction steps.
   - If there is no Jira issue, use the user description and set `ISSUE_KEY` to `MANUAL`.
2. Turn the bug into a test mission.
   - Identify the affected feature or page.
   - Identify the user intent and the business outcome at risk.
   - Write the ordered reproduction steps. If the bug does not provide them, infer the smallest reasonable path and mark that inference in the report.
3. Open the live application with Selenium MCP in headed mode.
   - Prefer the staging environment named by the user or bug context.
   - Take an immediate screenshot before interacting.
4. Handle authentication carefully.
   - If already signed in, continue.
   - If a login wall appears, tell the user exactly what is visible and wait for them to complete sign-in.
   - After the user confirms, re-check the page and take another screenshot before continuing.
5. Navigate intentionally to the affected area.
   - Use the provided reproduction steps first.
   - If the route is unclear, inspect the page from a business angle before picking a path:
     - why the user is here
     - what the business needs them to do next
     - what trust or recovery signals matter
     - where the journey can fail
6. Reproduce the issue step by step.
   - Take a screenshot at each critical step.
   - Capture exactly what was observed.
   - Record blockers such as missing data, permissions, feature flags, overlays, or environment drift.
7. Keep the investigation focused.
   - Confirm or disprove the reported behavior.
   - Record visible errors and closely related findings only when they materially affect reproduction.
   - Do not turn one bug into a broad exploratory session.
8. Assign one verdict:
   - `CONFIRMED`
   - `NOT REPRODUCED`
   - `PARTIALLY CONFIRMED`
   - `FIXED`
9. Save the HTML report.
   - Start from `assets/bug-test-report-template.html`.
   - Include the bug summary, steps executed, findings, evidence table, verdict detail, and any additional findings.
   - Use relative image paths inside the report because the images and report live in the same folder.
10. If the bug came from Jira, update Jira after saving the report.
   - Upload the key screenshots.
   - Post a concise structured comment that mirrors the report sections in order.
   - If Jira updates fail, keep the local report as the source of truth and say so clearly.

## Reporting Rules

- Never skip screenshots.
- Report what was observed, not what was expected.
- Test the reported bug, not adjacent product quality.
- If reproduction is blocked, say what blocked it.
- Keep the report concise and decision-useful.
- Write like an experienced tester with business awareness:
  - which user outcome was attempted
  - what business risk the observed behavior creates
  - whether the evidence suggests an active defect, partial defect, or likely fix

## Final User Response

End with:

- the verdict
- one short plain-English summary paragraph
- the saved report path
- the screenshot path pattern
- Jira upload status when applicable
- Jira comment status when applicable

## Boundaries

- One bug per run.
- Do not generate Java/TestNG coverage unless the user asks for that next.
- Do not mark a bug as fixed just because seed data or access was missing; use `NOT REPRODUCED` with a blocker note instead.
- Keep Jira and Selenium interactions at the agent layer, not in Java runtime code.
