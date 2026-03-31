# Quality Layer

This folder is the strategic layer for Willenium.

Its job is to keep business intent, risk thinking, and decision confidence visible before that context gets flattened into flows, classes, and pass/fail output.

## Why This Exists

Willenium should not optimize only for easier automation.
It should optimize for higher-value, risk-aware, decision-useful automation.

That means quality work should begin with:

- business goal
- user value
- operational risk
- unacceptable outcomes
- scenario map
- confidence target
- reporting expectations

## Folder Roles

- `quality/plans/`
  Use for broader quality strategy that may inform several executable plans under `test-plans/`.
- `quality/contracts/`
  Reserved for future behavior contracts such as golden, red, and edge expectations. This is intentionally deferred for now.
- `quality/reports/`
  Use for templates or examples of risk-based reporting that explain what was covered, what was not, and what release exposure remains.

## Relationship To `test-plans/`

`test-plans/` remains the canonical automation-linked planning area.

Use `quality/` when the work is asking:

- what matters most
- what failure would hurt most
- what confidence level is required
- which scenarios deserve automation intensity across more than one owned journey

Use `test-plans/` when the work is asking:

- what business goal, user value, confidence target, and scenario map belong to one owned journey
- which flows, classes, JSON sections, and suites should be created or updated
- how the executable coverage should be wired in this repository

The two layers should reinforce each other:

1. A quality plan explains why a broader investment matters.
2. A `test-plans/...` file explains how the framework should implement it.
