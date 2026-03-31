---
name: quality-canvas
description: Use when a Lean Canvas, product idea, project description, MVP description, or feature list should be turned into a concise Markdown Quality Canvas artifact before detailed test planning starts.
---

# Quality Canvas

Use this skill to create or update a concise 4-quadrant Quality Canvas artifact in this repository.

This skill is artifact-first. The deliverable is a Markdown file on disk, not only a chat response.

Use this skill before `test-plans/...` work when the starting input is still strategic or product-shaped rather than journey-shaped.

Do not use this skill to:

- generate UI or API tests
- write a detailed test plan
- create a quality matrix
- replace `test-plans/...` for executable journey planning

## When To Use

Use this skill when the input is something like:

- a Lean Canvas
- a product idea
- a project description
- an MVP brief
- a feature list
- an early initiative summary that is not yet ready for journey-level planning

Use `willenium-consultant` after the canvas exists when the next step still needs governance, prioritization, or a decision about what should be planned or automated first.
Use `willenium-automation` or `willenium-api` after the canvas exists when direction is already clear and the next step is detailed planning for one owned journey or service.

## Output Location

Create or update the artifact at:

- `quality/plans/<app>/<target-slug>-quality-canvas.md`

If the app name is not clear yet, use:

- `quality/plans/general/<target-slug>-quality-canvas.md`

If a canvas already exists for the same target, update it instead of creating a duplicate.

Use [`quality/plans/TEMPLATE.md`](../../../quality/plans/TEMPLATE.md) as the starting structure.

## Core Workflow

1. Inspect the provided source material and identify its source type:
   - `lean-canvas`
   - `product-idea`
   - `project-description`
   - `mvp-description`
   - `feature-list`
2. Extract what is explicitly known first:
   - target or product name
   - business goal
   - primary actor
   - important features
   - important failure modes
   - likely quality-sensitive scenarios
3. Infer only when the source leaves gaps that block a useful first draft.
4. Mark every non-trivial inference in the `Assumptions` section.
5. Keep the result concise and reusable so it can later inform one or more `test-plans/...` files.
6. Save the artifact to disk and report the path.

## Four-Quadrant Contract

Every Quality Canvas must keep the same four quadrants:

1. `Key Features`
2. `Risks & Failures`
3. `Quality Scenarios`
4. `Future Improvements`

Do not replace these quadrants with a different model in the first version.

## Content Rules

### Key Features

- limit to the few features that matter most for MVP quality decisions
- prefer 3 to 7 features
- include an `MVP Impact` value for each item:
  - `Core`
  - `Supporting`
  - `Deferred`
- explain why each feature matters in one short line

### Risks & Failures

- include both business-facing and implementation-facing risks when they matter
- use a clear category for each item such as:
  - `Operational`
  - `Technical`
  - `Experience`
  - `Data`
  - `Compliance`
  - `AI Behavior`
- describe the failure in business language first, not only technical language

### Quality Scenarios

- link each scenario to at least one meaningful risk
- make scenarios measurable where possible
- describe the signal or threshold that would increase confidence
- keep scenarios concrete enough that later planning can turn them into test cases
- do not expand them into a full test plan

### Future Improvements

- capture important quality investments that should not be part of the first planning pass
- keep them short and decision-useful
- note what would trigger moving the item earlier

## AI-Specific Quality Concerns

If the product includes AI-driven behavior, include relevant concerns in the risks and scenarios where they materially matter:

- emotional resonance
- contextual relevance
- ethical behavior
- behavioral transparency

Add these only when they genuinely apply. Do not force AI language into non-AI products.

## Artifact Shape

Keep the artifact suitable for later visual rendering:

- consistent front matter
- stable section ordering
- compact tables or bullets
- concise wording
- explicit assumptions

The body should stay readable by product, QA, and engineering audiences.

## Handoff To Test Planning

The Quality Canvas should help later planning answer:

- which journeys deserve deeper planning first
- which failures would be most damaging
- which scenarios need measurable confidence
- which improvements can wait

The usual handoff is:

1. `willenium-consultant` to judge business direction, risk priority, and decision usefulness when the next step is not yet obvious
2. `willenium-coach` when the user needs help choosing scope, plan type, or prompt shape
3. `willenium-automation` or `willenium-api` when execution planning is ready

The canvas may point to future `test-plans/...` files, but it must not generate them directly.
