# Willenium

Willenium is a Selenium-based Java automation framework built with Maven, TestNG, and XML suite composition. It is designed for framework-native UI and API automation with reusable setup, helper layers, JSON-backed test data, and suite-driven execution.

## Quick Start

Create a new project without cloning this repository.

Package: `create-willenium`

```bash
npx create-willenium@latest my-ui-tests
```

Run the starter suite:

```bash
cd my-ui-tests
mvn test -PBrowseAssistantHomeEnglish
```

Optional examples:

```bash
npx create-willenium@latest my-ui-tests --group-id com.acme.tests
npx create-willenium@latest my-ui-tests --force
```

Requirements:

- Node.js 18+
- Java 17+
- Maven

## Troubleshooting

- If `npx` fails, confirm Node.js and npm are installed and up to date.
- If you publish the package, run `npm publish` from the repository directory, not from `~`.
- To verify the published version, run `npm view create-willenium version`.

## Tech Stack

- Java 17
- Maven
- Selenium 4
- TestNG
- WebDriverManager
- RestAssured
- Extent Reports
- Log4j

## What You Get

The `create-willenium` scaffolder:

- creates the project directory
- copies the starter framework
- restores `.gitignore`
- sets the Maven `artifactId` from the project name
- generates a default `groupId` unless you provide `--group-id`

Generated projects include:

- Selenium + TestNG framework structure
- ready-to-run XML suites
- JSON test data setup
- shared browser helpers
- AI agent and skill files
- MCP configuration for Selenium-aware clients

## Running Tests

Use the included Maven profiles:

```bash
mvn test -PBrowseAssistantHomeEnglish
mvn test -PBrowseAssistantHomeArabic
```

Useful suite entry points:

- `flows/assistant/home/BrowseAssistantHomeEnglish.xml`
- `flows/assistant/home/BrowseAssistantHomeArabic.xml`
- `flows/api/APITestFlow.xml`
- `quick_path.xml`

`quick_path.xml` is the shortest smoke path in the repo. It runs setup, login, and teardown.

## MCP Integration

Willenium includes a root `.mcp.json` that registers the Selenium MCP server for MCP-aware clients.

- Server: `selenium`
- Command: `npx -y @angiejones/mcp-selenium@0.1.21`

Requirements:

- Node.js and npm must be installed locally so the client can launch the server through `npx`.
- Some MCP clients may still ask for a one-time workspace trust or server approval when the repo is opened.

## AI Support

The project includes framework-aware agent files for Codex and Claude:

- Skill: `.codex/skills/wellenium/SKILL.md`
- Agent: `.github/agents/wellenium.agent.md`
- Bridge files: `AGENTS.md`, `CLAUDE.md`

Use them when you want AI assistance that follows the framework conventions:

- follow the current `base.Setup` / `base.Finder` / `base.Go` conventions
- keep assertions in `*Test.java`
- update JSON-backed test data instead of hardcoding values
- wire new coverage through TestNG XML suites under `flows/...`
- use the `selenium` MCP server only when live browser exploration or locator validation is actually needed

Typical prompts:

```text
Use `wellenium` to add a new assistant home test.
```

```text
Follow `.github/agents/wellenium.agent.md` and debug the failing search flow.
```

Selenium MCP should be used for discovery and debugging only. Final deliverables should remain framework-native Java and TestNG code.

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

## Execution Model

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

UI test classes are intended to run through suites. Running a UI class by itself usually will not work unless `base.Setup` has already initialized the shared state.

## Reference Tests

Good examples to follow when adding or updating coverage:

- `src/test/java/tests/assistant/login/LoginTest.java`
- `src/test/java/tests/assistant/home/AssistantHomePageTest.java`
- `src/test/java/tests/assistant/search/GlobalSearchValidTest.java`
- `src/test/java/tests/api/SystemHealthApiTest.java`

The page and flow helper pattern is visible in:

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

The production JSON files are the richest examples and include UI labels and assertions used by the existing tests, including sections such as:

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

`stagingEnglish.json` and `stagingArabic.json` are currently minimal, so the production files are the better reference when creating new UI tests.

## Browser Support

`base.Setup` currently supports local execution with:

- Chrome
- Firefox
- Safari

The checked-in setup suites default to local Chrome:

- `flows/SetupEnglish.xml`
- `flows/SetupArabic.xml`

Remote execution through LambdaTest also exists in `base.Setup#setUpRemoteDriver`, but the checked-in XML setup files use local driver startup by default.

## Configuration

- Headless mode is controlled in `src/test/java/configs/pipeline/PipelineConfig.java`.
- TestRail reporting is also toggled in `PipelineConfig`.
- Browser-specific capabilities are set in `src/test/java/configs/BrowserOptions.java`.
- Screenshots are cleaned and recreated in the `screenshots/` directory at setup time.

## Adding New Tests

Follow the current framework conventions:

1. Add or extend test data first in the correct JSON file.
2. Create or update a page/helper class under `src/test/java/tests/...`.
3. Add assertions in a `*Test.java` class.
4. Register the class in a feature XML suite under `flows/steps/...` or a higher-level composed suite.
5. Run the flow through a suite that includes setup and teardown.

For UI coverage, `AssistantHomePageTest` and `GlobalSearchValidTest` are strong starting examples.
