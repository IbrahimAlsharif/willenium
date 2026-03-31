---
plan_id: framework_testrail-reporting-hardening
target_name: TestRail Reporting Integration Hardening
target_url: current-runtime-integration
target_slug: testrail-reporting-hardening
app: framework
plan_scope: framework integration
plan_type: regression
jira_issue_key:
jira_issue_url:
testrail_case_ids: []
testrail_run_ids: []
affected_flows:
  - flows/examples/wewill/ProtectExampleHomeTrustEnglish.xml
  - flows/examples/wewill/ProtectExampleHomeTrustArabic.xml
  - flows/examples/wewill/ProtectExamplePublicApiContractEnglish.xml
  - flows/examples/wewill/ProtectExamplePublicApiContractArabic.xml
affected_tests:
  - src/test/java/base/Setup.java
  - src/test/java/base/ApiSetup.java
  - src/test/java/configs/listeners/Listener.java
  - src/test/java/configs/testRail/TestRailManager.java
  - src/test/java/configs/testRail/APIClient.java
  - src/test/java/tests/examples/wewill/home/WeWillHomePageTest.java
  - src/test/java/tests/examples/wewill/api/WeWillPublicApiTest.java
affected_testdata: []
flow_xml: flows/examples/wewill/ProtectExampleHomeTrustEnglish.xml
java_helper: configs.testRail.TestRailManager
java_test: configs.listeners.Listener
testdata_sections: []
status: draft
---

# Test Plan: TestRail Reporting Integration Hardening

## Summary

This plan covers a low-risk hardening pass for the current in-framework TestRail reporting implementation. The goal is not to replace the existing approach or move to TestRail CLI yet. The goal is to identify and sequence the smallest safe improvements that reduce reporting risk while preserving current execution behavior.

Working assumptions for this draft:

- plan scope: framework integration
- plan type: regression

These assumptions match the current request to assess and safely improve the existing TestRail implementation without broad architectural change.

## Scope

- review and harden the current Java-based TestRail reporting path
- preserve the existing `WILLENIUM_TESTRAIL_REPORT` toggle and default behavior
- preserve current suite-driven UI and API execution structure
- improve reliability of run creation, case mapping, and pass/fail publishing
- improve safety around credentials, tenant configuration, and project configuration
- define the smallest verification path for any future implementation pass

## Plan Type

- regression hardening
- this depth is the right fit because the current implementation already affects setup, listeners, UI tests, API tests, and reporting configuration
- the first pass should focus on stabilization and correctness, not feature expansion or migration to another reporting mechanism

## Out Of Scope

- replacing the Java integration with TestRail CLI
- removing the existing `configs.testRail` package
- redesigning the framework execution model
- introducing MCP-dependent runtime reporting
- broad parallel-execution changes outside the TestRail reporting path

## Assumptions

- the current Java TestRail reporting path remains the source of truth for reporting in the near term
- the framework should continue to work when `WILLENIUM_TESTRAIL_REPORT` is disabled
- real projects will need tenant-specific TestRail credentials and project metadata supplied outside source code
- both UI and API suites may need TestRail reporting, even though their failure artifacts differ

## Open Questions

- should TestRail project identifiers and run naming stay globally configured or be profile-specific
- should case IDs remain inside test methods, move to annotations, or move to metadata files in a later phase
- should API failures publish without attachments, or should they attach serialized request/response context
- should setup failures publish to TestRail when a case ID has not yet been assigned by the test method
- should this integration eventually support parallel test execution, or is sequential execution an accepted constraint

## Jira Linkage

- no Jira issue is linked to this draft yet
- this work is locally scoped for now

## Impact Analysis

- UI setup creates TestRail runs in `base.Setup`, so any run-creation change affects all UI suites that enable reporting
- API suites reuse `Setup.testCaseId` through shared listener behavior, so API reporting depends on the same shared global mapping path
- `configs.listeners.Listener` is the main pass/fail reporting owner and currently handles success and failure asymmetrically
- example UI and API tests assign TestRail case IDs directly in test methods, so reporting correctness currently depends on mutable shared state being updated in the right order
- the current `configs.testRail.TestRailManager` hardcodes tenant, credentials, project naming, and project ID, so portability is limited and source edits are required for real usage
- there are no dedicated automated tests for the reporting path, so small changes could silently break reporting even if product tests still pass

Primary hardening areas:

1. configuration hardening
2. failure-reporting correctness
3. case/run mapping reliability
4. focused verification coverage

## Environment And Setup

- reporting toggle source: `WILLENIUM_TESTRAIL_REPORT`
- current reporting owner classes:
  - `src/test/java/base/Setup.java`
  - `src/test/java/configs/listeners/Listener.java`
  - `src/test/java/configs/testRail/TestRailManager.java`
- current runtime assumptions:
  - a TestRail tenant is reachable from the execution environment
  - valid credentials are present in the Java source placeholders or later refactoring sources
  - a project ID is known and stable
  - test methods assign a case ID before listener publication happens

## Test Data

- no JSON-backed product test data is directly involved in the current TestRail reporting path
- reporting metadata that may need configuration extraction:
  - TestRail base URL
  - API path
  - username
  - API key or password
  - project ID
  - run naming pattern

## Happy Paths

1. UI suite starts with reporting enabled, creates one TestRail run, executes tests, and publishes pass/fail results against the expected case IDs.
2. API suite starts with reporting enabled, executes tests, and publishes pass/fail results without depending on browser-only artifacts.
3. Reporting disabled path leaves suite behavior unchanged and does not attempt TestRail communication.

## Negative Paths

1. invalid or missing TestRail credentials should fail in a controlled way with a clear message instead of producing confusing runtime behavior.
2. missing case ID should not publish an incorrect result to the wrong case.
3. failed API tests should still be reportable even when no screenshot artifact exists.
4. TestRail endpoint or network failure should not corrupt shared framework state for later tests in the same run.

## Edge Cases

1. setup failure before a feature test assigns `testCaseId`
2. listener success/failure callbacks firing after shared state was overwritten by another test
3. attachment upload path for failed UI tests when screenshot capture itself fails
4. mixed UI and API execution where browser artifacts are unavailable for API failures
5. future parallel or retried execution causing shared static state collisions

## Localization

- localization is not a primary concern for this framework-level integration plan
- language-specific product assertions should remain in JSON test data and are unaffected by TestRail hardening unless reporting metadata later becomes language-aware

## Automation Mapping

- Flow XML:
  `flows/examples/wewill/ProtectExampleHomeTrustEnglish.xml`
- Java helper:
  `src/test/java/configs/testRail/TestRailManager.java`
- Java test:
  `src/test/java/configs/listeners/Listener.java`
- Test data sections:
  none for the current reporting path

## Verification Strategy

- smallest code-level verification path after any implementation:
  - reporting disabled smoke run for one UI suite
  - reporting enabled smoke run for one UI suite
  - reporting enabled smoke run for one API suite
- primary suites to validate first:
  - `ProtectExampleHomeTrustEnglish`
  - `ProtectExamplePublicApiContractEnglish`
- additional focused checks:
  - invalid credential handling
  - missing case ID handling
  - API failure reporting without screenshot dependence
- no Selenium MCP is required for this plan because the risk area is framework reporting, not live DOM behavior

## Proposed Enhancement Sequence

1. Externalize TestRail configuration from `TestRailManager` into environment variables or property-driven config without changing the reporting flow shape.
2. Make failure reporting independent of screenshot availability so API and early-failure cases can still publish correctly.
3. Guard listener publication when `testCaseId` or `testRunId` is missing to avoid misreporting.
4. Reduce dependence on shared mutable globals for case mapping in a later, isolated pass.
5. Add focused verification around the reporting toggle and listener behavior before any larger migration is considered.

## Review Status

- ready for review as a low-risk hardening draft
- waiting for confirmation on whether the first implementation pass should stay strictly configuration-and-listener level
- waiting for confirmation on whether parallel execution support is a current requirement or a future concern
