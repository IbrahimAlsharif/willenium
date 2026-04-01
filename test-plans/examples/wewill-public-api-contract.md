---
plan_id: examples_wewill_public_api_contract
target_name: WE WILL Public API Contract Journey
target_url: https://jsonplaceholder.typicode.com/posts
target_slug: wewill-public-api-contract
app: examples
quality_canvas: quality/plans/examples/wewill-starter-quality-canvas.md
plan_scope: contract
plan_type: smoke
business_goal: preserve a small public API contract example that shows how Willenium protects stable response expectations
user_value: help a starter-framework user see how API contract assertions map to business-aware planning
confidence_target: demonstrate a lightweight executable API plan that links back to a quality canvas and JSON-backed expectations
unacceptable_outcomes:
  - Public contract fields change silently while the example still appears healthy
  - The create example no longer echoes the expected payload fields
scenario_map:
  - get a stable public post contract
  - create a public post example and verify echoed fields
intensity_level: light
jira_issue_key:
jira_issue_url:
affected_flows:
  - flows/examples/wewill/ProtectExamplePublicApiContractEnglish.xml
  - flows/examples/wewill/ProtectExamplePublicApiContractArabic.xml
affected_tests:
  - src/test/java/tests/examples/wewill/api/WeWillPublicApi.java
  - src/test/java/tests/examples/wewill/api/WeWillPublicApiTest.java
affected_testdata:
  - src/test/java/configs/testdata/exampleProductionEnglish.json
  - src/test/java/configs/testdata/exampleProductionArabic.json
  - src/test/java/configs/testdata/exampleStagingEnglish.json
  - src/test/java/configs/testdata/exampleStagingArabic.json
flow_xml: flows/examples/steps/wewill/public_api_journey.xml
java_helper: tests.examples.wewill.api.WeWillPublicApi
java_test: tests.examples.wewill.api.WeWillPublicApiTest
testdata_sections:
  - api.posts.getOne
  - api.posts.createOne
status: draft
---

# Test Plan: WE WILL Public API Contract Journey

## Summary

This starter plan covers a small public API contract slice for the bundled example API coverage.
Its purpose is to show how even a lightweight public API example can stay linked to business intent, measurable expectations, and reusable JSON-backed data.

## Business Goal

- Preserve a stable, understandable contract example for starter API coverage in the repo.

## User Value

- A starter-framework user can see how request and response expectations become clear API assertions.

## Confidence Target

- Provide a simple API planning example that is linked to the starter Quality Canvas and executable API coverage.

## Business Questions Answered

- Who is the primary user or actor?
  A public API consumer in the example domain.
- What outcome are they trying to achieve?
  Read one public record and submit one example record with predictable contract behavior.
- What outcome does the business need from this journey?
  Demonstrate stable contract verification in a minimal starter slice.
- What failure would be unacceptable?
  Status, content type, or key payload fields change without the example catching it.
- What decision should this coverage support?
  Whether the starter API example still demonstrates meaningful contract protection.

## Scope

- Example `GET /posts/1` contract
- Example `POST /posts` contract
- Status code, content type, and key returned fields

## Plan Type

- smoke
- This depth is enough because the starter goal is to teach contract structure, not build full negative-path API coverage yet.

## Out Of Scope

- Authentication
- Rate limiting
- Negative-path validation
- Multi-endpoint workflow coverage

## Assumptions

- JSONPlaceholder remains stable enough for starter contract checks.
- The example is intentionally public and low-risk.

## Unacceptable Outcomes

- Expected contract fields change without detection
- The create response no longer echoes the expected title or user id

## Open Questions

- Whether the starter API slice should later add a negative-path example
- Whether one environment-specific divergence example should be added later to the JSON files

## Jira Linkage

- No Jira issue is linked to this starter example plan.

## Impact Analysis

- Keep this starter example concentrated in one simple owned contract journey instead of duplicating the same behavior across multiple example assets.
- Keep the current example owner and strengthen or split coverage there if the contract checkpoints grow.

## Environment And Setup

- Base URL source: example JSON files
- Language coverage: English and Arabic starter executions
- Auth requirements: none
- Contract specification path: `src/test/resources/openapi/jsonplaceholder-posts-v1.json`

## Test Data

- Endpoint values
- Expected status codes
- Expected content types
- Expected id, title, and user id fields
- Example request body values

## Test Cases

1. Primary path:
   Read one example post and confirm the core contract needed for a consumer to trust the response.
2. Alternate path:
   Create one example post and confirm the service echoes the configured business data clearly enough for a consumer to rely on it.
3. Negative path:
   A contract field changes while the endpoint still returns a technically successful response.
4. Edge case:
   The response keeps a successful status while one important payload field drifts from the expected contract.

## Localization

- The API contract itself is language-agnostic.
- Keep the same example structure across English and Arabic execution profiles for consistency with the broader starter framework.

## Scenario Map

- Read contract example
- Create contract example
- Detect field drift

## Intensity Level

- light

## Secondary Implementation Notes

- Flow XML:
  `flows/examples/steps/wewill/public_api_journey.xml`
- Java helper:
  `src/test/java/tests/examples/wewill/api/WeWillPublicApi.java`
- Java test:
  `src/test/java/tests/examples/wewill/api/WeWillPublicApiTest.java`
- Test data sections:
  `api.posts.getOne`
  `api.posts.createOne`

## Verification Strategy

- Smallest verification path:
  `mvn test -PProtectExamplePublicApiContractEnglish`
- Follow-up verification:
  `mvn test -PProtectExamplePublicApiContractArabic`

## Review Status

- Ready as the starter API example plan for the bundled public contract slice.
