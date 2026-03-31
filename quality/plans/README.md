# Quality Plans

Use this folder for broader quality planning that spans more than one executable automation plan.

This is also the default home for early strategic artifacts such as a Quality Canvas that should exist before detailed `test-plans/...` work starts.

Default canvas location:

- `quality/plans/<app>/<target-slug>-quality-canvas.md`

Fallback when the app namespace is not known yet:

- `quality/plans/general/<target-slug>-quality-canvas.md`

This is the right place for work such as:

- release-risk strategy
- multi-journey scenario prioritization
- cross-cutting trust and safety analysis
- confidence models for critical product areas
- reporting expectations for leadership or product decisions
- early Quality Canvas drafts created from a Lean Canvas, product brief, MVP description, or feature list

## Difference From `test-plans/`

`quality/plans/` answers:

- what quality posture do we need
- which risks deserve investment
- what confidence level is required before a decision

`test-plans/` answers:

- what business context belongs to a specific owned journey
- what concrete helper, test, flow, and JSON artifacts should be built

Quality plans should point to the `test-plans/...` files that implement their priorities.

## Quality Canvas

The first version of the Quality Canvas in this repo should stay lightweight:

- four fixed quadrants:
  - `Key Features`
  - `Risks & Failures`
  - `Quality Scenarios`
  - `Future Improvements`
- concise Markdown that is easy to review and easy to render later
- assumptions clearly labeled when source information is incomplete
- no generated tests
- no quality matrix

Use `TEMPLATE.md` as the base shape when creating a new canvas artifact.
