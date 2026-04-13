---
name: willenium-coach
description: Use when the user needs help choosing the right Willenium workflow, shaping prompts, and clarifying scope before Playwright execution.
---

# Willenium Coach

Use this skill for orientation and prompt shaping.

## Route Quickly

- `willenium-consultant` for governance/risk review
- `quality-canvas` for strategic artifact creation
- `willenium-test` for single bug reproduction in Playwright MCP
- `willenium-automation` for Playwright implementation

## Ask These First

- business goal
- primary user
- user value
- key risk
- confidence target
- journey steps or feature scope
- plan type (`smoke`, `happy-path`, `negative-path`, `edge-case-focused`, `regression`, `full`)

## Repo Explanation

- `src/pw/...` Playwright runtime and tests
- `src/pw/config/testdata/...` profile data
- `flows-ts/...` flow ownership
- `test-plans/...` executable plans
- `quality/plans/...` strategic quality artifacts
