# Willenium

Willenium is a Selenium-based UI automation framework for the WeWill/Sentra web app. It uses Java, Maven, TestNG, and XML suite composition to run end-to-end browser flows, and it also includes a small API testing entry point with RestAssured.

The current repo is organized around reusable setup/helpers, feature-level test classes, and TestNG suite files that stitch those pieces together into runnable flows.

## Tech Stack

- Java 17
- Maven
- Selenium 4
- TestNG
- WebDriverManager
- RestAssured
- Extent Reports
- Log4j

## MCP Integration

This repo includes a root `.mcp.json` that registers the Selenium MCP server for MCP-aware clients that support project-level configuration.

- Server: `selenium`
- Command: `npx -y @angiejones/mcp-selenium@0.1.21`

That means anyone who clones the repo gets the same Selenium MCP definition from the repository itself instead of configuring it manually per machine.

Prerequisite:

- Node.js and npm must be installed locally so the client can launch the server through `npx`.
- Some MCP clients may still ask for a one-time workspace trust or server approval when the repo is opened.

## Project Structure

```text
wellenium/
|- pom.xml
|- quick_path.xml
|- flows/
|  |- SetupEnglish.xml
|  |- SetupArabic.xml
|  |- TearDown.xml
|  |- assistant/home/
|  |- steps/
|  `- api/
`- src/test/java/
   |- base/
   |- configs/
   |  |- testdata/
   |  |- listeners/
   |  `- pipeline/
   `- tests/
      |- assistant/
      |- api/
      `- common/
```

## How Willenium Runs

UI flows follow the same pattern:

1. `base.Setup` loads the right test data file, starts the browser, creates the shared `driver`, `wait`, `Go`, and `Finder` helpers, and opens the application.
2. One or more feature suites run the actual tests.
3. `base.TearDownTest` closes the browser.

Example:

- `flows/assistant/home/BrowseAssistantHomeEnglish.xml`
  calls `flows/SetupEnglish.xml`
- then `flows/assistant/home/Steps.xml`
- then `flows/TearDown.xml`

Inside `Steps.xml`, the framework chains existing feature suites:

- login
- open home
- global search
- empty search
- search modal
- empty search modal

This means UI test classes are designed to run as part of a suite. Running a UI class by itself usually will not work unless `base.Setup` has already initialized the shared state.

## Existing Test Examples

These are good examples to follow when adding or updating coverage:

- `src/test/java/tests/assistant/login/LoginTest.java`
  validates the login screen, clicks Google sign-in, and uses `validUser` from test data.
- `src/test/java/tests/assistant/home/AssistantHomePageTest.java`
  checks the assistant home UI such as search, live monitoring, chat dock, and action buttons.
- `src/test/java/tests/assistant/search/GlobalSearchValidTest.java`
  uses the configured search query and verifies banners, result cards, and the first result title.
- `src/test/java/tests/api/SystemHealthApiTest.java`
  shows the API test style used by the framework.

The page/flow helper pattern is visible in:

- `src/test/java/tests/assistant/login/Login.java`
- `src/test/java/tests/assistant/home/AssistantHomePage.java`
- `src/test/java/tests/assistant/search/GlobalSearch.java`

Shared Selenium utilities live in:

- `src/test/java/base/Finder.java`
- `src/test/java/base/Go.java`

## Test Data

Willenium selects test data through `configs.testdata.TestDataFactory` using:

- `branch`: `production` or non-production
- `language`: `english` or `arabic`

That mapping resolves to one of these files:

- `src/test/java/configs/testdata/productionEnglish.json`
- `src/test/java/configs/testdata/productionArabic.json`
- `src/test/java/configs/testdata/stagingEnglish.json`
- `src/test/java/configs/testdata/stagingArabic.json`

The production JSON files are currently the richest examples and include UI labels and assertions used by the existing tests, including sections such as:

- `login`
- `home`
- `search`
- `searchModal`
- `topic`
- analysis-related datasets

Typical structure:

```json
{
  "base-url": {
    "env": {
      "english": "https://example/en"
    }
  },
  "users": {
    "validUser": {
      "email": "<email>",
      "password": "<password>"
    }
  },
  "login": {
    "welcomeHeader": "Welcome back"
  },
  "home": {
    "sidebarSearch": "Search"
  },
  "search": {
    "query": "German-GCC Sports Relations"
  }
}
```

Note: the current `stagingEnglish.json` and `stagingArabic.json` are minimal compared with the production files, so production data is the better reference when creating new UI tests.

## Browser Execution

`base.Setup` currently supports local execution with:

- Chrome
- Firefox
- Safari

The checked-in setup suites default to local Chrome:

- `flows/SetupEnglish.xml`
- `flows/SetupArabic.xml`

Remote execution through LambdaTest also exists in `base.Setup#setUpRemoteDriver`, but the provided XML setup files currently exclude that method and use local driver startup instead.

## Running Tests

### Maven Profiles

Two ready-to-run Maven profiles are already configured in `pom.xml`:

```bash
mvn test -PBrowseAssistantHomeEnglish
mvn test -PBrowseAssistantHomeArabic
```

These profiles run the assistant home flow end to end through the XML suites.

### XML Suites

Useful suite entry points already in the repo:

- `flows/assistant/home/BrowseAssistantHomeEnglish.xml`
- `flows/assistant/home/BrowseAssistantHomeArabic.xml`
- `flows/api/APITestFlow.xml`
- `quick_path.xml`

`quick_path.xml` is the shortest existing smoke-style path in the repo. It runs:

1. setup
2. login
3. teardown

If you want to run a different suite from Maven, follow the same pattern used by the existing Maven profiles in `pom.xml` and point Surefire at the XML file you want to execute.

## Configuration Notes

- Headless mode is controlled in `src/test/java/configs/pipeline/PipelineConfig.java`.
- TestRail reporting is also toggled in `PipelineConfig`.
- Browser-specific capabilities are set in `src/test/java/configs/BrowserOptions.java`.
- Screenshots are cleaned and recreated in the `screenshots/` directory at setup time.

## Adding New Tests

Use the current tests as the framework convention:

1. Add or extend test data first in the correct JSON file.
2. Create or update a page/helper class under `src/test/java/tests/...`.
3. Add assertions in a `*Test.java` class.
4. Register the class in a feature XML suite under `flows/steps/...` or a higher-level composed suite.
5. Run the flow through a suite that includes setup and teardown.

For UI coverage, `AssistantHomePageTest` and `GlobalSearchValidTest` are the clearest starting examples in the current codebase.
