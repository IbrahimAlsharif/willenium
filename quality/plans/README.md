# Quality Plans

Use this folder for broader quality planning that spans more than one executable automation plan.

This is the right place for work such as:

- release-risk strategy
- multi-journey scenario prioritization
- cross-cutting trust and safety analysis
- confidence models for critical product areas
- reporting expectations for leadership or product decisions

## Difference From `test-plans/`

`quality/plans/` answers:

- what quality posture do we need
- which risks deserve investment
- what confidence level is required before a decision

`test-plans/` answers:

- what business context belongs to a specific owned journey
- what concrete helper, test, flow, and JSON artifacts should be built

Quality plans should point to the `test-plans/...` files that implement their priorities.
