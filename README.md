# Willenium

Willenium is a Java Selenium TestNG starter framework built for suite-driven UI automation.
It gives you reusable setup and helper layers, JSON-backed test data, XML flow composition, and AI guidance files that match the framework structure.

## Quick Start

Create a new project without cloning this repository.

Package: `create-willenium`

```bash
npx create-willenium@latest my-ui-tests
```

Run the starter suite:

```bash
cd my-ui-tests
mvn test -PBrowseExampleWeWillEnglish
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

The generated project includes a simple public WE WILL homepage example so the framework shape is runnable immediately, but that example is starter content and should be replaced with your real product coverage.

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
- ready-to-run example XML suites
- example JSON test data setup
- a simple public WE WILL homepage example
- shared browser helpers
- AI agent and skill files
- MCP configuration for Selenium-aware and Atlassian-aware clients

The checked-in tests, flows, and JSON files are starter examples of framework structure. They are intended to be renamed, moved, or replaced when you begin real project coverage.

## Running Tests

Use the included Maven profiles:

```bash
mvn test -PBrowseExampleWeWillEnglish
mvn test -PBrowseExampleWeWillArabic
```

Useful suite entry points:

- `flows/examples/wewill/BrowseExampleWeWillEnglish.xml`
- `flows/examples/wewill/BrowseExampleWeWillArabic.xml`
- `example_quick_path.xml`

`example_quick_path.xml` is the shortest smoke path in the repo. It runs setup, a single public homepage example, and teardown.

## MCP Integration

Willenium includes a root `.mcp.json` that registers both a browser MCP server and an Atlassian MCP server for MCP-aware clients.

- `selenium`
  Local browser automation and DOM inspection through `npx -y @angiejones/mcp-selenium@0.1.21`
- `atlassian`
  Atlassian Rovo MCP endpoint at `https://mcp.atlassian.com/v1/mcp`

Use them for different jobs:

- `selenium`: inspect a real page, validate a locator, or reproduce a UI issue before translating the result into Java/TestNG code
- `atlassian`: read Jira bugs before planning automation, create Jira bugs from failures, or update Jira issues with generated test artifact paths

Requirements:

- Node.js and npm must be installed locally so the client can launch the `selenium` server through `npx`.
- Atlassian workflows require an Atlassian Cloud site with Jira access and an MCP-aware client authorized to use Atlassian Rovo MCP.
- The first Atlassian Rovo MCP connection may require site or org admin approval depending on your Atlassian setup.
- Some MCP clients may still ask for a one-time workspace trust or server approval when the repo is opened.

Template safety notes:

- Do not commit personal credentials, API tokens, account IDs, cloud IDs, or site-specific Jira URLs into this template.
- Do not hardcode a customer Jira site or project into framework files.
- The public template should keep only the generic Atlassian MCP endpoint; the actual Jira site and permissions should come from the user's own MCP client authorization flow at usage time.

## AI Support

The project includes framework-aware agent files for Codex and Claude:

- Execution skill: `.codex/skills/willenium-automation/SKILL.md`
- Coaching skill: `.codex/skills/willenium-coach/SKILL.md`
- Agent: `.github/agents/willenium.agent.md`
- Bridge files: `AGENTS.md`, `CLAUDE.md`

Use them when you want AI assistance that follows the framework conventions:

- follow the current `base.Setup` / `base.Finder` / `base.Go` conventions
- keep assertions in `*Test.java`
- update JSON-backed test data instead of hardcoding values
- wire new coverage through TestNG XML suites under `flows/...`
- ask for test plan scope and plan type before drafting a new plan when those are not already clear
- when work starts from a Jira bug, read the issue first and keep the resulting plan linked to that bug
- for Jira bugs, analyze which existing plans, flows, tests, and JSON sections should be updated before deciding to add new coverage
- use the `selenium` MCP server only when live browser exploration or locator validation is actually needed
- treat the bundled tests and data as examples, not the default product namespace to extend
- keep Jira tenant configuration user-provided at runtime rather than committed in the template

Use `willenium-coach` when you want help deciding what to ask for, what inputs to provide, or which workflow to use before the implementation work starts.
Use `willenium-automation` when you want the framework work done through the execution skill.
Use `willenium` when you want to reference the repo agent directly.
For plan-first work, the expected flow is: confirm scope and plan type -> write the Markdown draft under `test-plans/` -> let the user review -> then generate or update tests.
Selenium MCP is optional during planning and is most useful when the plan needs live page inspection rather than just the user's description and existing local artifacts.

Typical prompts:

```text
Use `willenium-automation` to create my first real product test and move the starter examples out of the way if needed.
```

```text
Follow `.github/agents/willenium.agent.md` and update the bundled WE WILL example flow.
```

```text
Use `willenium-automation` to read Jira bug ABC-123, write the linked test plan under test-plans/, and generate the framework-native automation from it.
```

```text
Use `willenium-automation` to read Jira bug ABC-123, decide which existing flows and plans it affects, update those plans, then update the linked tests instead of creating duplicates.
```

```text
Use `willenium-coach` to help me choose the best next prompt for planning, generation, debugging, or Jira-driven automation in this repo.
```

```text
Use `willenium-coach` to help me choose the right test plan scope and whether I need a smoke, regression, or full plan before you draft the Markdown plan.
```

```text
Use `willenium-coach` to decide whether this plan should inspect the live target with Selenium MCP first or whether the Markdown draft can be written from the current information.
```

Selenium MCP should be used for discovery and debugging only. Final deliverables should remain framework-native Java and TestNG code.
Atlassian MCP should be used for Jira issue access and bug filing only. Final deliverables should still be plans, Java tests, JSON data, and XML flows in this repo.

## Project Structure

```text
willenium/
|- pom.xml
|- example_quick_path.xml
|- flows/
|  |- SetupEnglish.xml
|  |- SetupArabic.xml
|  |- TearDown.xml
|  `- examples/
|     |- wewill/
|     `- steps/
`- src/test/java/
   |- base/
   |- configs/
   |  |- testdata/
   |  |- listeners/
   |  `- pipeline/
   `- tests/
      `- examples/
         `- wewill/
```

## Execution Model

UI flows follow the same pattern:

1. `base.Setup` loads the right test data file, starts the browser, creates the shared `driver`, `wait`, `Go`, and `Finder` helpers, and opens the application.
2. One or more feature suites run the actual tests.
3. `base.TearDownTest` closes the browser.

Example:

- `flows/examples/wewill/BrowseExampleWeWillEnglish.xml`
  calls `flows/SetupEnglish.xml`
- then `flows/examples/steps/wewill/home_journey.xml`
- then `flows/TearDown.xml`

The bundled public example uses the stable WE WILL homepage only.
The English and Arabic files are kept separate even though the current public landing page content is shared, so future language-specific values can diverge cleanly.

UI test classes are intended to run through suites. Running a UI class by itself usually will not work unless `base.Setup` has already initialized the shared state.

The bundled suites are examples only. When you begin real automation for a new product, create app-specific suites and test data rather than extending the WE WILL example by default.

## Reference Tests

Good examples to follow when adding or updating coverage:

- `src/test/java/tests/examples/wewill/home/WeWillHomePageTest.java`

The page and flow helper pattern is visible in:

- `src/test/java/tests/examples/wewill/home/WeWillHomePage.java`

Shared Selenium utilities live in:

- `src/test/java/base/Finder.java`
- `src/test/java/base/Go.java`

## Test Data

Willenium selects test data through `configs.testdata.TestDataFactory` using:

- `branch`: `production` or non-production
- `language`: `english` or `arabic`

That mapping resolves to one of these files:

- `src/test/java/configs/testdata/exampleProductionEnglish.json`
- `src/test/java/configs/testdata/exampleProductionArabic.json`
- `src/test/java/configs/testdata/exampleStagingEnglish.json`
- `src/test/java/configs/testdata/exampleStagingArabic.json`

The example production JSON files are the starter references used by the bundled public WE WILL homepage example.

Typical structure:

```json
{
  "base-url": {
    "env": {
      "english": "https://example/en"
    }
  },
  "wewillHome": {
    "heroHeading": "We don’t just test software.",
    "primaryCta": "Book a Clarity Session",
    "methodologyHeading": "GenAI Feature Quality & Decision Governance"
  }
}
```

The English and Arabic example files currently share the same homepage expectations because that public landing page is shared, but they remain split into separate JSON files so a future Arabic-specific version can be modeled without changing the framework shape.

For real projects, keep expected text, URLs, credentials, and other assertion inputs in app-specific JSON files and have assertions read from the active language file instead of hardcoding them in test methods.

## Browser Support

`base.Setup` currently supports local execution with:

- Chrome
- Firefox
- Safari

The checked-in setup suites default to local Chrome:

- `flows/SetupEnglish.xml`
- `flows/SetupArabic.xml`

Remote execution through LambdaTest also exists in `base.Setup#setUpRemoteDriver`, but the checked-in XML setup files use local driver startup by default.

If Chrome or Firefox fails before the browser opens with a message about `localhost`, `bind`, or finding a free port, the environment is blocking the local WebDriver service. Headless mode does not fix that. Run the suite from a normal local terminal session or switch the suite to remote driver execution.

## Configuration

- Headless mode is controlled in `src/test/java/configs/pipeline/PipelineConfig.java`.
- TestRail reporting is also toggled in `PipelineConfig`.
- Extent report auto-open is controlled in `PipelineConfig`. Local runs try to open `extent-reports/extent-report.html`; when auto-open is unavailable, Willenium prints an exact command you can run to open it.
- Browser-specific capabilities are set in `src/test/java/configs/BrowserOptions.java`.
- Screenshots are cleaned and recreated in the `screenshots/` directory at setup time.

## Adding New Tests

Follow the current framework conventions:

1. Add or extend app-specific test data first in the correct JSON file.
2. Create or update a page/helper class under `src/test/java/tests/...`.
3. Add assertions in a `*Test.java` class.
4. Register the class in a feature XML suite under `flows/...`.
5. Run the flow through a suite that includes setup and teardown.

If the bundled example assets would make the real project confusing, rename or move them first and then add the real app-specific coverage.
