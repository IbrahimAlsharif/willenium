---
canvas_id: examples_wewill-starter_quality_canvas
canvas_type: quality-canvas
target_name: WE WILL Starter Example
target_slug: wewill-starter
app: examples
source_type: project-description
source_inputs:
  - bundled starter homepage example
  - bundled starter public API contract example
primary_actor: example visitor or API consumer
business_goal: demonstrate how Willenium turns early quality intent into reusable planning artifacts before framework-native automation grows
status: draft
linked_test_plans:
  - test-plans/examples/wewill-home-trust-journey.md
  - test-plans/examples/wewill-public-api-contract.md
---

# Quality Canvas: WE WILL Starter Example

## Source Summary

This starter example demonstrates two small public-facing quality slices in the repository:

- a public homepage journey that should communicate trust and make the main call to action easy to find
- a public API contract slice that should show stable request and response expectations

The goal is not deep product coverage. The goal is to model Willenium's business-first planning workflow with lightweight example assets.

## Assumptions

- The bundled WE WILL homepage remains a stable public example rather than a customer-owned target.
- The bundled public API contract example is intended to demonstrate contract checks, not domain-specific business logic.
- The example should stay concise enough for new users to understand quickly.

## Quadrant 1: Key Features

| Feature | MVP Impact | Why It Matters |
| --- | --- | --- |
| Public homepage trust signals | Core | The starter UI example should show how to protect visible trust-building content, not just page load |
| Primary call to action discovery | Core | New users should see how a journey can protect the business path the page wants the visitor to take |
| Public API contract validation | Core | The starter API example should show stable contract checks for a public endpoint |
| Bilingual example coverage | Supporting | The starter repo should demonstrate language-aware execution structure without excessive complexity |

## Quadrant 2: Risks & Failures

| Risk Or Failure | Category | Why It Matters |
| --- | --- | --- |
| Homepage key trust content disappears or changes unexpectedly | Experience | The example would stop teaching business-aware homepage verification |
| Primary CTA is no longer visible | Operational | The starter journey would miss the business action the page wants from the visitor |
| Public API contract changes without detection | Technical | The starter API example would fail to demonstrate meaningful contract protection |
| Example naming stays technical instead of journey-oriented | Operational | New users may copy weak naming conventions into real projects |

## Quadrant 3: Quality Scenarios

| Scenario | Linked Risk | Confidence Signal |
| --- | --- | --- |
| A visitor can see the hero heading, primary CTA, methodology section, bilingual CTA, and footer slogan | Homepage key trust content disappears or changes unexpectedly | All expected trust and navigation signals are visible from JSON-backed data |
| The homepage suite name and plan make the protected visitor outcome obvious | Example naming stays technical instead of journey-oriented | Flow, workflow, and plan names read like owned journeys rather than generic execution buckets |
| A public GET request still returns the expected status, content type, and canonical post title | Public API contract changes without detection | Contract assertions pass against JSON-backed expected values |
| A public POST request still echoes the expected title and user id | Public API contract changes without detection | Response status and echoed fields match the configured contract |

## Quadrant 4: Future Improvements

| Improvement | Why Later | Pull-Forward Trigger |
| --- | --- | --- |
| Add example negative-path UI planning | The first starter pass is intentionally lightweight | The repo needs a stronger example of validation and recovery paths |
| Add example negative-path API planning | Current starter contract coverage is enough to teach the structure | The API example expands beyond one happy-path contract slice |
| Add richer cross-linking from quality artifacts into reports | Reporting is a later layer for the framework direction | Quality reporting templates become part of the starter experience |

## Planning Notes

- This canvas feeds the example journey plan at `test-plans/examples/wewill-home-trust-journey.md`.
- It also feeds the example API plan at `test-plans/examples/wewill-public-api-contract.md`.
- Keep the starter example lightweight and easy to replace when a real project begins.
