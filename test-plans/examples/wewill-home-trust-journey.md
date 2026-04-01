---
plan_id: examples_wewill_home_trust_journey
target_name: WE WILL Public Home Trust Journey
target_url: https://wewill.tech/
target_slug: wewill-home-trust-journey
app: examples
quality_canvas: quality/plans/examples/wewill-starter-quality-canvas.md
plan_scope: journey
plan_type: smoke
business_goal: preserve the public homepage signals that build trust and guide a visitor toward the main call to action
user_value: help a new visitor quickly understand the offering and find the next action without confusion
confidence_target: give starter-framework users a simple example of a business-aware homepage journey plan
unacceptable_outcomes:
  - Key trust-building homepage sections disappear while the page still technically loads
  - The primary CTA is no longer visible to a visitor
scenario_map:
  - visitor sees the hero message and main CTA
  - visitor can confirm methodology and bilingual offering signals
  - visitor reaches the footer and still sees the brand promise
intensity_level: light
jira_issue_key:
jira_issue_url:
affected_flows:
  - flows/examples/wewill/ProtectExampleHomeTrustArabic.xml
  - flows/examples/wewill/ProtectExampleHomeTrustEnglish.xml
affected_tests:
  - src/test/java/tests/examples/wewill/home/WeWillHomePage.java
  - src/test/java/tests/examples/wewill/home/WeWillHomePageTest.java
affected_testdata:
  - src/test/java/configs/testdata/exampleProductionEnglish.json
  - src/test/java/configs/testdata/exampleProductionArabic.json
  - src/test/java/configs/testdata/exampleStagingEnglish.json
  - src/test/java/configs/testdata/exampleStagingArabic.json
flow_xml: flows/examples/steps/wewill/home_journey.xml
java_helper: tests.examples.wewill.home.WeWillHomePage
java_test: tests.examples.wewill.home.WeWillHomePageTest
testdata_sections:
  - wewillHome
status: draft
---

# Test Plan: WE WILL Public Home Trust Journey

## Summary

This starter plan covers the public-facing homepage trust journey for the bundled WE WILL example.
The goal is to show how Willenium keeps even a small example tied to business intent instead of only checking that a page rendered.

The owned journey is simple:

- a visitor lands on the homepage
- key trust and positioning content is visible
- the primary next action remains easy to find

## Business Goal

- Preserve the visible trust and action signals the public homepage uses to guide a first-time visitor.

## User Value

- A visitor can quickly understand the offering and find a meaningful next step.

## Confidence Target

- Provide a lightweight but concrete example of a journey plan that can later drive framework-native UI coverage.

## Business Questions Answered

- Who is the primary user or actor?
  A first-time public visitor.
- What outcome are they trying to achieve?
  Understand the offer and decide whether to continue.
- What outcome does the business need from this journey?
  Present trust signals clearly and keep the main CTA discoverable.
- What failure would be unacceptable?
  The page loads but the main trust or CTA content is missing.
- What decision should this coverage support?
  Whether the starter homepage example still demonstrates a meaningful public trust journey.

## Scope

- The public homepage example
- The hero heading
- The primary CTA
- The methodology heading
- The bilingual CTA
- The footer slogan

## Plan Type

- smoke
- This depth is enough for the starter example because the goal is to teach structure and business-aware assertions, not exhaustively model the whole public site.

## Out Of Scope

- Deep navigation after the CTA
- Form submission or lead capture behavior
- Responsive layout testing
- Negative-path validation

## Assumptions

- The public homepage remains stable enough for starter-example assertions.
- The example JSON files stay the source of truth for expected visible text.

## Unacceptable Outcomes

- Hero messaging disappears but the suite still counts the page as healthy
- The main CTA is not visible to the visitor
- Trust or positioning content disappears without the example catching it

## Open Questions

- Whether a later starter pass should add one click-through assertion for the CTA
- Whether Arabic and English example text should intentionally diverge later

## Jira Linkage

- No Jira issue is linked to this starter example plan.

## Impact Analysis

- Keep this starter example in one simple owned journey instead of spreading the same business intent across duplicate example assets.
- Keep the existing example owner and strengthen or split coverage there if the business checkpoints grow.

## Environment And Setup

- Base URL source: example JSON files
- Language coverage: English and Arabic starter executions
- Auth requirements: none
- Browser assumptions: standard shared Selenium setup

## Test Data

- Hero heading
- Primary CTA label
- Methodology heading
- Bilingual CTA label
- Footer slogan

## Test Cases

1. Primary path:
   A visitor lands on the homepage and clearly sees the hero value message.
2. Alternate path:
   A visitor can find the primary CTA without confusion after understanding the first-screen message.
3. Negative path:
   Key trust content is absent even though the page still loads.
4. Edge case:
   Trust and positioning cues remain visible deeper in the page, including methodology and bilingual signals.
5. Edge case:
   The footer still reinforces the brand promise after the visitor scrolls through the rest of the page.

## Localization

- Keep English and Arabic executions active through the example environment files.
- Preserve the same JSON structure across both languages and environments.

## Scenario Map

- Homepage arrival
- Trust and positioning confirmation
- CTA visibility
- Footer promise visibility

## Intensity Level

- light

## Secondary Implementation Notes

- Flow XML:
  `flows/examples/steps/wewill/home_journey.xml`
- Java helper:
  `src/test/java/tests/examples/wewill/home/WeWillHomePage.java`
- Java test:
  `src/test/java/tests/examples/wewill/home/WeWillHomePageTest.java`
- Test data sections:
  `wewillHome`

## Verification Strategy

- Smallest verification path:
  `mvn test -PProtectExampleHomeTrustEnglish`
- Follow-up verification:
  `mvn test -PProtectExampleHomeTrustArabic`

## Review Status

- Ready as the starter UI example plan for the bundled public homepage journey.
