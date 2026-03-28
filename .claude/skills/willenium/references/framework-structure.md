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
  Controls report auto-open behavior and other pipeline-level flags.

## Test Organization

The current UI pattern is:

- feature helper/action class:
  `src/test/java/tests/.../<Feature>.java`
- assertion-focused test class:
  `src/test/java/tests/.../<Feature>Test.java`

Examples already in the repo:

- homepage helper: `tests.examples.wewill.home.WeWillHomePage`
- homepage assertions: `tests.examples.wewill.home.WeWillHomePageTest`

Keep new work close to the nearest existing feature folder instead of inventing a new top-level convention.

## Suite Composition

TestNG XML files are the orchestration layer.

- setup suites:
  `flows/SetupEnglish.xml`, `flows/SetupArabic.xml`
- feature step suites:
  `flows/examples/steps/...` in the starter example, or app-specific `flows/steps/...` areas for real projects
- composed end-to-end suites:
  `flows/examples/wewill/...` in the starter example, or app-specific `flows/...` suites for real projects
- teardown suite:
  `flows/TearDown.xml`

Example:

- `flows/examples/wewill/BrowseExampleWeWillEnglish.xml`
  includes setup, example journey steps, and teardown.
- `flows/examples/steps/wewill/home_journey.xml`
  runs the sample public homepage check inside the initialized suite flow.

If a new scenario should run with the broader flow, wire it into the relevant step suite. If it needs a dedicated path, create a new XML suite that still includes setup and teardown.

## Test Data Rules

Use `TestDataFactory` rules when choosing JSON files:

- production + english -> `exampleProductionEnglish.json`
- production + arabic -> `exampleProductionArabic.json`
- non-production + english -> `exampleStagingEnglish.json`
- non-production + arabic -> `exampleStagingArabic.json`

The production JSON files are currently the richer examples. Prefer them as a structural reference when adding new keys.

## Planning Area

- canonical Markdown plans:
  `test-plans/<app>/<target-slug>.md`
- starter references:
  `test-plans/README.md`, `test-plans/TEMPLATE.md`

When the user asks for a plan, the task is not complete until the Markdown file exists on disk.

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
