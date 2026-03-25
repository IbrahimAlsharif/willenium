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

## Test Organization

The current UI pattern is:

- feature helper/action class:
  `src/test/java/tests/.../<Feature>.java`
- assertion-focused test class:
  `src/test/java/tests/.../<Feature>Test.java`

Examples already in the repo:

- login: `tests.assistant.login.Login` + `LoginTest`
- assistant home: `tests.assistant.home.AssistantHomePage` + `AssistantHomePageTest`
- search: `tests.assistant.search.GlobalSearch` + `GlobalSearchValidTest`

Keep new work close to the nearest existing feature folder instead of inventing a new top-level convention.

## Suite Composition

TestNG XML files are the orchestration layer.

- setup suites:
  `flows/SetupEnglish.xml`, `flows/SetupArabic.xml`
- feature step suites:
  `flows/steps/...`
- composed end-to-end suites:
  `flows/assistant/home/...`
- teardown suite:
  `flows/TearDown.xml`

Example:

- `flows/assistant/home/BrowseAssistantHomeEnglish.xml`
  includes setup, steps, and teardown.
- `flows/assistant/home/Steps.xml`
  chains login, open home, search, and modal flows via nested suite files.

If a new scenario should run with the broader flow, wire it into the relevant step suite. If it needs a dedicated path, create a new XML suite that still includes setup and teardown.

## Test Data Rules

Use `TestDataFactory` rules when choosing JSON files:

- production + english -> `productionEnglish.json`
- production + arabic -> `productionArabic.json`
- non-production + english -> `stagingEnglish.json`
- non-production + arabic -> `stagingArabic.json`

The production JSON files are currently the richer examples. Prefer them as a structural reference when adding new keys.

## Execution Entry Points

- Maven profiles in `pom.xml`:
  - `BrowseAssistantHomeEnglish`
  - `BrowseAssistantHomeArabic`
- shortest existing smoke path:
  - `quick_path.xml`

## Practical Conventions

- Reuse `Finder`/`Go` before adding raw Selenium code.
- Favor explicit helper methods that encode intent, then assert in `*Test.java`.
- Preserve suite-driven execution.
- Keep new files in ASCII and match the repo's direct, utility-first style.
- Be conservative with Java language features because `pom.xml` mixes Java 11 properties with a Java 17 compiler plugin; follow the existing code style unless a newer feature is clearly warranted.
