# API Framework Structure

## Runtime Shape

Willenium API execution is suite-driven rather than centered on isolated test classes.

1. `base.ApiSetup` loads the right test data file and initializes the shared RestAssured request specification.
2. Feature-level `*ApiTest.java` classes run inside that initialized state.
3. `configs.api.ApiContext` captures request and response details for reporting.

Because `ApiSetup.testData` and `ApiSetup.apiSpecification` are shared static state, API test classes should run through TestNG XML suites that include API setup.

## Key Files

- `src/test/java/base/ApiSetup.java`
  Loads JSON test data and builds the shared request specification.
- `src/test/java/base/ApiClient.java`
  Shared GET, POST, PUT, and DELETE request execution helpers.
- `src/test/java/configs/api/ApiContext.java`
  Stores request and response details for reporting.
- `src/test/java/configs/testdata/TestDataFactory.java`
  Maps `branch` + `language` to the correct JSON file.
- `src/test/java/configs/testdata/TestData.java`
  Exposes the JSON sections used by API and UI tests.
- `src/test/java/configs/listeners/Listener.java`
  Adds request and response details to the Extent report when API tests fail.

## Test Organization

The current API pattern is:

- planning artifact:
  `test-plans/<app>/<target-slug>.md`
- service or feature helper:
  `src/test/java/tests/.../<Feature>Api.java`
- assertion-focused API test:
  `src/test/java/tests/.../<Feature>ApiTest.java`

Examples already in the repo:

- WE WILL public API example:
  `tests.examples.wewill.api.WeWillPublicApi` + `WeWillPublicApiTest`

These checked-in classes are examples of framework structure, not the required business domain for future work.

## Suite Composition

TestNG XML files remain the orchestration layer.

- API setup suites:
  `flows/SetupApiEnglish.xml`
- feature step suites:
  `flows/examples/steps/...`
- composed API suites:
  `flows/examples/wewill/RunExamplePublicApiEnglish.xml`

If a new scenario needs a dedicated path, create a new XML suite that includes the appropriate API setup suite.

## Test Data Rules

Use `TestDataFactory` rules when choosing JSON files:

- production + english -> `exampleProductionEnglish.json`
- production + arabic -> `exampleProductionArabic.json`
- non-production + english -> `exampleStagingEnglish.json`
- non-production + arabic -> `exampleStagingArabic.json`

Put API data under the `api` section of the selected JSON file.

Real tests should:

- store endpoints, headers, payload fragments, expected status codes, and expected response values in JSON rather than embedding them in test methods
- keep staging and production values separate when the target service differs by environment
- create new app-appropriate values for the real system under test instead of reusing the sample public endpoint content

## Execution Entry Points

- Maven profile in `pom.xml`:
  - `RunExamplePublicApiEnglish`

## Practical Conventions

- Favor explicit helper methods that encode API intent, then assert in `*ApiTest.java`.
- Preserve suite-driven execution.
- Keep new files in ASCII and match the repo's direct, utility-first style.
