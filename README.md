# Willenium

Willenium is a Java Selenium TestNG starter framework for suite-driven UI and API automation that is evolving into a quality-governed automation framework.
It gives you reusable setup and helper layers, JSON-backed test data, XML flow composition, AI guidance files, and now a first strategic scaffold for business-aware planning, risk-aware coverage, and decision-useful reporting.

## Strategic Direction

Willenium is designed to move beyond script-first automation.

The framework direction is:

- automation should begin from business goals, user value, operational risk, and release confidence needs
- coverage should be organized around meaningful scenarios and business journeys instead of test accumulation
- planning should be treated as a first-class framework artifact
- reporting should communicate covered risks, remaining exposure, and confidence level, not only pass/fail counts
- AI should guide users toward stronger quality decisions, not only faster code generation
- flow names should describe the business-care path they own, not just the test grouping they execute

The initial scaffold for this direction now lives in two places:

- `test-plans/` for executable planning plus the minimum business context each owned journey needs
- `quality/plans/` for cross-cutting quality planning that can feed one or more executable test plans
- `quality/contracts/` for future behavior contracts
- `quality/reports/` for risk-based reporting models

`test-plans/` remains the canonical source of truth for automation-linked executable planning and now also carries the minimum business context for each owned journey.
`flows/` remains the executable layer, but each flow should represent a business journey with a clear user and business outcome.

## Quick Start

Create a new project without cloning this repository.

Package: `create-willenium`

The npm package is only the launcher. When you run it, it downloads the current `main` branch template from GitHub and then applies your project name and optional `groupId`.

```bash
npx create-willenium@latest my-ui-tests
```

Run the starter suite:

```bash
cd my-ui-tests
mvn test -PProtectExampleHomeTrustEnglish
```

Optional examples:

```bash
npx create-willenium@latest my-ui-tests --group-id com.acme.tests
npx create-willenium@latest my-ui-tests --force
```

Requirements:

- Node.js 18+
- Internet access to `github.com` and `codeload.github.com`
- Java 17+
- Maven

The generated project includes a simple public WE WILL homepage example so the framework shape is runnable immediately, but that example is starter content and should be replaced with your real product coverage.

## Troubleshooting

- If `npx` fails, confirm Node.js and npm are installed and up to date.
- If scaffolding fails during download, confirm access to `github.com` and `codeload.github.com`.
- If you publish the package, run `npm publish` from the repository directory, not from `~`.
- To verify the published version, run `npm view create-willenium version`.
- To verify the template source the launcher will fetch, inspect the latest commit on [`main`](https://github.com/IbrahimAlsharif/willenium/tree/main).

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
- downloads the latest starter framework from GitHub `main`
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
mvn test -PProtectExampleHomeTrustEnglish
mvn test -PProtectExampleHomeTrustArabic
mvn test -PProtectExamplePublicApiContractEnglish
mvn test -PProtectExamplePublicApiContractArabic
npm run sync:flow-workflows
```

Useful suite entry points:

- `flows/examples/wewill/ProtectExampleHomeTrustEnglish.xml`
- `flows/examples/wewill/ProtectExampleHomeTrustArabic.xml`
- `flows/examples/wewill/ProtectExamplePublicApiContractEnglish.xml`
- `flows/examples/wewill/ProtectExamplePublicApiContractArabic.xml`
- `example_quick_path.xml`

`example_quick_path.xml` is the shortest smoke path in the repo. It runs setup, a single public homepage example, and teardown.

## GitHub Workflows

Each top-level Maven profile that points at a TestNG suite gets a matching GitHub Actions workflow with a `workflow_dispatch` trigger under `.github/workflows/`.

When you add or rename a top-level flow/profile, regenerate those workflow files with:

```bash
npm run sync:flow-workflows
```

The generated workflow name matches the Maven profile name, so the new flow can be run manually from GitHub Actions without hand-writing another YAML file.

## MCP Integration

Willenium includes a root `.mcp.json` that registers browser, Jira, and test-management MCP servers for MCP-aware clients.

- `selenium`
  Local browser automation and DOM inspection through `npx -y @angiejones/mcp-selenium@0.1.21`
- `atlassian`
  Atlassian Rovo MCP endpoint at `https://mcp.atlassian.com/v1/mcp`
- `testrail`
  TestRail MCP server through `uvx --from testrail-api-module testrail-mcp-server`

Use them for different jobs:

- `selenium`: inspect a real page, understand the business journey, validate a locator, or reproduce a UI issue before translating the result into Java/TestNG code
- `atlassian`: read Jira bugs before planning automation, create Jira bugs from failures, or update Jira issues with generated test artifact paths
- `testrail`: read TestRail cases, plans, or runs before planning automation, map framework artifacts to TestRail IDs, or coordinate reporting expectations

Requirements:

- Node.js and npm must be installed locally so the client can launch the `selenium` server through `npx`.
- Atlassian workflows require an Atlassian Cloud site with Jira access and an MCP-aware client authorized to use Atlassian Rovo MCP.
- TestRail workflows require `uvx` and valid TestRail credentials configured in the MCP server environment.
- The first Atlassian Rovo MCP connection may require site or org admin approval depending on your Atlassian setup.
- Some MCP clients may still ask for a one-time workspace trust or server approval when the repo is opened.

Template safety notes:

- Do not commit personal credentials, API tokens, account IDs, cloud IDs, or site-specific Jira URLs into this template.
- Do not commit personal TestRail URLs, usernames, API keys, or project-specific identifiers into this template.
- Do not hardcode a customer Jira site or project into framework files.
- Do not hardcode a customer TestRail workspace or credentials into framework files.
- The public template should keep only the generic Atlassian MCP endpoint; the actual Jira site and permissions should come from the user's own MCP client authorization flow at usage time.
- The public template should keep only placeholder TestRail MCP values; the actual TestRail tenant and credentials should come from the user's own local MCP configuration at usage time.

## AI Support

The project includes framework-aware agent files for Codex and Claude:

- Consultant skill: `.codex/skills/willenium-consultant/SKILL.md`
- Quality Canvas skill: `.codex/skills/quality-canvas/SKILL.md`
- UI execution skill: `.codex/skills/willenium-automation/SKILL.md`
- API execution skill: `.codex/skills/willenium-api/SKILL.md`
- Coaching skill: `.codex/skills/willenium-coach/SKILL.md`
- Agent: `.github/agents/willenium.agent.md`
- Bridge files: `AGENTS.md`, `CLAUDE.md`

Use them when you want AI assistance that follows the framework conventions:

- follow the current `base.Setup` / `base.Finder` / `base.Go` conventions for UI work
- follow `base.ApiSetup` / `base.ApiClient` / `configs.api.ApiContext` for API work
- keep UI assertions in `*Test.java`
- keep API assertions in `*ApiTest.java`
- update JSON-backed test data instead of hardcoding values
- wire new coverage through TestNG XML suites under `flows/...`
- use `quality/plans/...` for early strategic Quality Canvas artifacts when the input is still a Lean Canvas, product brief, MVP description, or feature list
- ask the business questions before drafting a new plan:
  - business goal
  - primary user or actor
  - user value
  - key risk or unacceptable outcome
  - confidence target
- then ask for test plan scope and plan type when those are not already clear
- prefer a clean, structured question UI with short grouped prompts when the client supports it
- when work starts from a Jira bug, read the issue first and keep the resulting plan linked to that bug
- for Jira bugs, analyze which existing plans, flows, tests, and JSON sections should be updated before deciding to add new coverage
- use the `selenium` MCP server only when live browser exploration or locator validation is actually needed
- when using Selenium MCP, analyze user intent, trust signals, journey checkpoints, and drop-off risks before focusing on selectors
- treat the bundled tests and data as examples, not the default product namespace to extend
- keep Jira tenant configuration user-provided at runtime rather than committed in the template

For Codex users, the practical entry point is usually the skill, not the repo agent file.
Call the skill directly when you want a specific operating mode, and treat `.github/agents/willenium.agent.md` as the repo's governing identity layer rather than the main user command surface.

Use `willenium-consultant` when you want strategic governance before execution, such as reviewing business intent, judging whether a plan creates real release confidence, detecting false confidence, or upgrading a shallow automation ask into business-directed work.
Use `quality-canvas` when you want a Lean Canvas, product idea, project description, MVP brief, or feature list turned into a reusable four-quadrant Quality Canvas artifact under `quality/plans/`.
Use `willenium-coach` when you want help deciding what to ask for, what inputs to provide, or which workflow to use before the implementation work starts.
Use `willenium-automation` when you want UI or browser automation work done through the execution skill.
Use `willenium-api` when you want API or service automation work done through the execution skill.
Use `willenium` when you want to reference the repo agent directly.

Typical Codex prompts:

```text
Use willenium-consultant to review this request before planning and tell me whether it creates real business confidence.
```

```text
Use willenium-consultant to review this test plan, identify false confidence, and recommend what to automate now versus defer.
```

```text
Use willenium-coach to help me choose whether I should start with willenium-consultant, quality-canvas, or a direct test plan.
```

For strategy-first work, the expected flow is: create or update the Quality Canvas under `quality/plans/` -> use `willenium-consultant` to judge direction, value, risk, and decision usefulness -> ask for a short description of the target system -> confirm business context for the owned journey -> confirm scope as `journey` or `feature` -> confirm test type such as `smoke`, `regression`, or `full` including negative and edge cases -> inspect the live site first with Selenium MCP in headed mode -> write the Markdown draft under `test-plans/` -> let the user review -> then generate or update tests.
For direct plan-first work on an already-defined journey, the expected flow is: ask for a short description of the target system -> confirm business context -> confirm scope as `journey` or `feature` -> confirm test type -> inspect the live site first with Selenium MCP in headed mode -> write the Markdown draft under `test-plans/` focused on business scenarios and test cases -> let the user review -> then generate or update tests.
During UI planning and generation, Selenium MCP is a required first step. Use the live headed session to inspect the rendered experience, treat rendered state as truth, avoid brittle assumptions, account for dynamic UI such as cookie banners and skeleton loaders, and keep tests step-by-step with at most two assertions each.

When the work is strategic rather than purely technical, include quality intent up front:

- business goal
- user value
- operational risk
- unacceptable outcomes
- scenario map
- confidence target
- desired intensity level

Recommended strategy prompt:

```text
Use quality-canvas to turn this Lean Canvas or product brief into a four-quadrant Quality Canvas under quality/plans/. Then use willenium-consultant to decide what matters first and route the next step to willenium-coach or the right execution skill.
```

Typical prompts:

```text
Use `willenium-automation` to create my first real product test and move the starter examples out of the way if needed.
```

```text
Use `willenium-api` to create my first real API coverage slice, write the linked plan under test-plans/, and map it to ApiSetup, Api helpers, Api tests, JSON test data, and XML flows.
```

```text
Use `quality-canvas` to convert this product brief into a concise Quality Canvas artifact under quality/plans/ before test-planning starts.
```

Starter planning examples:

- Quality Canvas:
  `quality/plans/examples/wewill-starter-quality-canvas.md`
- UI journey plan:
  `test-plans/examples/wewill-home-trust-journey.md`
- API contract plan:
  `test-plans/examples/wewill-public-api-contract.md`

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
Use `willenium-coach` to help me choose whether this API work should be planned as endpoint, service, contract, or integration-flow coverage and whether it should be smoke, regression, negative-path, or full.
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

- `flows/examples/wewill/ProtectExampleHomeTrustEnglish.xml`
  calls `flows/SetupEnglish.xml`
- then `flows/examples/steps/wewill/home_journey.xml`
- then `flows/TearDown.xml`

The bundled public example uses the stable WE WILL homepage only.
The English and Arabic files are kept separate even though the current public landing page content is shared, so future language-specific values can diverge cleanly.

UI test classes are intended to run through suites. Running a UI class by itself usually will not work unless `base.Setup` has already initialized the shared state.

The bundled suites are examples only. When you begin real automation for a new product, create app-specific suites and test data rather than extending the WE WILL example by default.

API flows follow a parallel suite-driven pattern:

1. `base.ApiSetup` loads the right test data file and builds the shared request specification.
2. One or more `*ApiTest.java` suites run the actual API assertions.

Example:

- `flows/examples/wewill/ProtectExamplePublicApiContractEnglish.xml`
  calls `flows/SetupApiEnglish.xml`
- then `flows/examples/steps/wewill/public_api_journey.xml`

The bundled public API example uses JSONPlaceholder as a stable public contract target and includes both a GET contract check and a POST contract check.
The English and Arabic files currently share the same API expectations, but they remain split so future language or tenant-specific API data can diverge cleanly without changing the framework shape.
The shared API setup also supports OpenAPI contract validation when `api.contractValidation.enabled` is true in the selected JSON test data file.
The bundled example points at `src/test/resources/openapi/jsonplaceholder-posts-v1.json` and applies that contract automatically through `base.ApiSetup` and `base.ApiClient`.

API test classes are also intended to run through suites. Running an API test class by itself may skip the shared setup that initializes `ApiSetup.testData` and the shared request specification.

## Reference Tests

Good examples to follow when adding or updating coverage:

- `src/test/java/tests/examples/wewill/home/WeWillHomePageTest.java`
- `src/test/java/tests/examples/wewill/api/WeWillPublicApiTest.java`

The page and flow helper pattern is visible in:

- `src/test/java/tests/examples/wewill/home/WeWillHomePage.java`
- `src/test/java/tests/examples/wewill/api/WeWillPublicApi.java`

Shared Selenium utilities live in:

- `src/test/java/base/Finder.java`
- `src/test/java/base/Go.java`

Shared API utilities live in:

- `src/test/java/base/ApiSetup.java`
- `src/test/java/base/ApiClient.java`
- `src/test/java/configs/api/ApiContext.java`

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
      "arabic": "https://example/ar",
      "english": "https://example/en"
    }
  },
  "api": {
    "baseUrl": "https://api.example.com",
    "posts": {
      "getOne": {
        "endpoint": "/posts/1",
        "expectedStatus": 200
      }
    }
  },
  "wewillHome": {
    "heroHeading": "We don’t just test software.",
    "primaryCta": "Book a Clarity Session",
    "methodologyHeading": "GenAI Feature Quality & Decision Governance"
  }
}
```

The English and Arabic example files currently share the same homepage expectations because that public landing page is shared, and the staging and production example files currently share the same values because the starter content is identical across environments. Keep the four files structurally aligned so language and environment values can diverge later without changing the framework shape.

For real projects, keep expected text, URLs, credentials, and other assertion inputs in app-specific JSON files and have assertions read from the active environment-and-language file instead of hardcoding them in test methods.

## Browser Support

`base.Setup` currently supports local execution with:

- Chrome
- Firefox
- Safari

The checked-in setup suites default to local Chrome:

- `flows/SetupEnglish.xml`
- `flows/SetupArabic.xml`

Remote execution through `base.Setup#setUpRemoteDriver` also exists, and the framework now supports `WILLENIUM_EXECUTION_MODE=auto|local|remote`.

In `auto` mode, `setUpLocalDriver` tries local browser startup first. If the environment blocks localhost port binding, it automatically falls back to remote execution when `WILLENIUM_REMOTE_URL` is configured.

Supported remote environment variables:

- `WILLENIUM_EXECUTION_MODE`
- `WILLENIUM_REMOTE_URL`
- `WILLENIUM_REMOTE_PROVIDER` (`lambdatest` or leave empty for generic Selenium Grid)
- `WILLENIUM_REMOTE_USERNAME`
- `WILLENIUM_REMOTE_ACCESS_KEY`
- `WILLENIUM_REMOTE_PLATFORM`
- `WILLENIUM_REMOTE_BROWSER_VERSION`
- `WILLENIUM_REMOTE_PROJECT`
- `WILLENIUM_REMOTE_BUILD`

If Chrome or Firefox fails before the browser opens with a message about `localhost`, `bind`, or finding a free port, the environment is blocking the local WebDriver service. Headless mode does not fix that. Run the suite from a normal local terminal session, rerun the command outside the sandbox, or provide remote execution settings so auto fallback can take over.

## Configuration

- UI execution behavior is property-driven through system properties or environment variables in `src/test/java/configs/pipeline/PipelineConfig.java`.
- Common toggles include `WILLENIUM_BROWSER_HEADLESS`, `WILLENIUM_BROWSER_INCOGNITO`, `WILLENIUM_BROWSER_MAXIMIZE`, `WILLENIUM_BROWSER_WINDOW_SIZE`, and `WILLENIUM_PAGE_LOAD_STRATEGY`.
- UI synchronization and interaction safety are tunable with `WILLENIUM_UI_WAIT_TIMEOUT_SECONDS`, `WILLENIUM_UI_INTERACTION_RETRY_ATTEMPTS`, `WILLENIUM_UI_INTERACTION_RETRY_DELAY_MILLIS`, `WILLENIUM_UI_VERIFY_TYPED_TEXT`, and `WILLENIUM_UI_HIGHLIGHT_INTERACTIONS`.
- TestRail reporting is controlled by `WILLENIUM_TESTRAIL_REPORT`.
- Extent report auto-open is controlled by `WILLENIUM_AUTO_OPEN_EXTENT_REPORT`. Local runs try to open `extent-reports/extent-report.html`; when auto-open is unavailable, Willenium prints an exact command you can run to open it.
- Browser-specific capabilities are set in `src/test/java/configs/BrowserOptions.java`.
- Screenshots and page-source artifacts are cleaned and recreated in the `screenshots/` directory at setup time.

## Adding New Tests

Follow the current framework conventions:

1. Add or extend app-specific test data first in all required JSON files for the target `branch` + `language` shape.
   For bilingual UI work, default to updating `Production Arabic`, `Production English`, `Staging Arabic`, and `Staging English` together so the structure stays aligned across all four files.
2. Create or update a page/helper class under `src/test/java/tests/...`.
3. Add assertions in a `*Test.java` class.
4. Register the class in a feature XML suite under `flows/...`.
5. Run the flow through a suite that includes setup and teardown.

If the bundled example assets would make the real project confusing, rename or move them first and then add the real app-specific coverage.
