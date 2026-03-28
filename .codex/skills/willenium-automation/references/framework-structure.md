# Framework Structure

## Runtime Shape

Willenium is organized around suite-driven execution rather than isolated test classes.

1. `base.Setup` loads test data, starts the browser, initializes shared helpers, and opens the app.
2. Feature-level `*Test.java` classes run inside that initialized state.
3. `base.TearDownTest` closes the driver.

Because `Setup.driver`, `Setup.wait`, and `Setup.testData` are static shared state, UI test classes are expected to run through TestNG XML suites that include setup and teardown.

## Key Files

- `src/test/java/base/Setup.java`
  Starts local or remote browsers, creates `WebDriverWait`, and initializes `Go` and `Finder`.
- `src/test/java/base/Finder.java`
  Shared waits and element lookup helpers.
- `src/test/java/base/Go.java`
  Shared browser interactions and utility actions.
- `src/test/java/base/TearDownTest.java`
  Closes the browser session.
- `src/test/java/configs/testdata/TestDataFactory.java`
  Maps `branch` + `language` to the correct JSON file.
- `src/test/java/configs/pipeline/PipelineConfig.java`
  Controls headless mode and TestRail reporting flags.
- `test-plans/TEMPLATE.md`
  Default template for plan-first coverage authoring.
- `test-plans/README.md`
  Naming and linkage rules for plans, flows, Java classes, and JSON data.

## Test Organization

The current UI pattern is:

- planning artifact:
  `test-plans/<app>/<target-slug>.md`
- feature helper/action class:
  `src/test/java/tests/.../<Feature>.java`
- assertion-focused test class:
  `src/test/java/tests/.../<Feature>Test.java`

Examples already in the repo:

- WE WILL public home journey: `tests.examples.wewill.home.WeWillHomePage` + `WeWillHomePageTest`

These checked-in classes are examples of framework structure, not the required business domain for future work.

For a real project:

- create app-specific plans before generating app-specific automation
- create app-specific folders, helpers, tests, JSON data, and flows
- avoid carrying forward sample names, URLs, or assertions unless the user confirms they are still relevant
- feel free to move or rename the starter examples so they are clearly separated from the real project baseline

Plans should be the source of truth when a user asks to:

- inspect a target link and write a test plan
- generate full tests from an approved plan
- update a plan and then update the related tests

Each plan should include stable metadata pointing to:

- the target slug
- the related flow XML file
- the helper and test classes
- the JSON data sections that support the tests

## Suite Composition

TestNG XML files are the orchestration layer.

- setup suites:
  `flows/SetupEnglish.xml`, `flows/SetupArabic.xml`
- feature step suites:
  `flows/examples/steps/...`
- composed end-to-end suites:
  `flows/examples/wewill/...`
- teardown suite:
  `flows/TearDown.xml`

Example:

- `flows/examples/wewill/BrowseExampleWeWillEnglish.xml`
  includes setup, steps, and teardown.
- `flows/examples/steps/wewill/home_journey.xml`
  runs the bundled public-site example test class.

If a new scenario should run with the broader flow, wire it into the relevant step suite. If it needs a dedicated path, create a new XML suite that still includes setup and teardown.

When the first real client scenario is added, prefer creating dedicated app-specific suites instead of extending the WE WILL sample flow unless the user explicitly wants to keep the examples as the main path.

Because XML files are execution artifacts, keep comprehensive planning in `test-plans/` by default. Only add a Markdown blueprint under `flows/...` when the user explicitly asks for a flow-local plan file.

## Test Data Rules

Use `TestDataFactory` rules when choosing JSON files:

- production + english -> `exampleProductionEnglish.json`
- production + arabic -> `exampleProductionArabic.json`
- non-production + english -> `exampleStagingEnglish.json`
- non-production + arabic -> `exampleStagingArabic.json`

The current JSON files are starter examples of the expected shape. Use them as a structural reference only.

Real tests should:

- store expected assertions, URLs, credentials, and inputs in JSON rather than embedding them in test methods
- keep English and Arabic values in their respective files when the target app supports both
- create new app-appropriate values for the real system under test instead of reusing the sample site content

## Execution Entry Points

- Maven profiles in `pom.xml`:
  - `BrowseExampleWeWillEnglish`
  - `BrowseExampleWeWillArabic`
- shortest existing smoke path:
  - `example_quick_path.xml`

## Practical Conventions

- Reuse `Finder`/`Go` before adding raw Selenium code.
- Favor explicit helper methods that encode intent, then assert in `*Test.java`.
- Preserve suite-driven execution.
- Keep new files in ASCII and match the repo's direct, utility-first style.
- Be conservative with Java language features because `pom.xml` mixes Java 11 properties with a Java 17 compiler plugin; follow the existing code style unless a newer feature is clearly warranted.
