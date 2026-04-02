# Jira MCP

## Repo Configuration

This repo now declares a project-level Atlassian MCP server in `.mcp.json`:

- server name: `atlassian`
- type: `http`
- url: `https://mcp.atlassian.com/v1/mcp`

This is the Atlassian Rovo MCP endpoint for Jira, Confluence, and Compass. In this repo, the practical Jira use cases are:

- read existing bugs before writing a plan or test
- create Jira bugs from a reproduced failure or test result summary
- add comments or updates that point back to generated test artifacts

The committed template should remain tenant-neutral:

- keep only the generic Atlassian MCP endpoint in version control
- do not commit customer Jira base URLs, project keys, cloud IDs, account IDs, API tokens, or personal credentials
- expect the real Jira site context to come from the user's MCP client authorization and live tool responses at usage time

## Important Boundary

Jira MCP is an agent/client capability, not a Selenium runtime dependency.

Keep Jira interactions outside:

- `base.Setup`
- `base.Go`
- `base.Finder`
- TestNG suite execution

The Java framework should continue to own browser setup, helpers, assertions, test data, and XML suite wiring only.

## Recommended Jira-Driven Workflow

When a user wants automated coverage for a Jira bug:

1. Read the Jira issue first.
2. Analyze impact before creating anything:
   - inspect existing `test-plans/...` files for related target slugs or journeys
   - inspect existing `flows/...` suites that already cover the affected area
   - inspect existing helper and `*Test.java` classes that may already own the behavior
   - inspect existing JSON test-data sections that already hold the needed assertions or inputs
3. Decide the narrowest correct update path:
   - update an existing plan when the bug belongs to an already-covered journey
   - update an existing flow and tests when the bug is a regression in current coverage
   - create a new plan and assets only when no existing artifact owns the behavior well
4. Create or update the canonical Markdown plan in `test-plans/<app>/<target-slug>.md`.
5. Add issue linkage to the front matter:
   - `jira_issue_key`
   - `jira_issue_url`
   - `affected_flows`
   - `affected_tests`
   - `affected_testdata`
6. Translate the bug into framework-native assets:
   - helper methods in `src/test/java/tests/.../<Feature>.java`
   - assertions in `src/test/java/tests/.../<Feature>Test.java`
   - JSON-backed expectations in `src/test/java/configs/testdata/...`
   - XML suite wiring under `flows/...`
7. Run the relevant suite for the changed flow and fix failures before stopping.
8. If the user asks, update or comment on the Jira issue with the generated artifact paths, impacted flows, and verification outcome.

## Impact Analysis Heuristics

Bug-to-test decisions in this repo should be intentional rather than one-bug-one-test by default.

Prefer updating existing coverage when:

- the Jira issue is a regression in an already-automated journey
- the failing behavior belongs inside an existing helper or assertion class
- the current flow already represents the user journey, but the assertions are too weak
- the missing check is really a missing branch of an existing scenario

Prefer adding a new flow or test class when:

- the bug introduces a distinct user journey not represented in current plans
- the setup, permissions, or data shape differ enough that extending the current flow would make it confusing
- the bug needs a dedicated smoke or regression path with different XML wiring

When in doubt, update the plan first and let the plan record why an existing artifact was extended or a new one was created.

## Recommended Bug Filing Workflow

When a user wants to push a bug to Jira:

1. Gather the failure summary, repro steps, expected behavior, and actual behavior.
2. Include any environment context that matters:
   - target URL or environment
   - language
   - browser
   - suite or flow path
   - screenshots or report location when available
3. Create the Jira bug through the Atlassian MCP server.
4. Keep the Jira key available for later plan and test generation if the bug will become automation work.

## Plan Metadata Guidance

Use the normal plan metadata plus Jira linkage when applicable.

Suggested additional front matter fields:

- `jira_issue_key`
- `jira_issue_url`
- `affected_flows`
- `affected_tests`
- `affected_testdata`

This keeps later "update the tests for bug ABC-123" requests deterministic.

## When Not To Use Jira MCP

Do not use Jira MCP when:

- the task is pure local framework refactoring
- the user only wants a chat-level brainstorming answer
- the Jira site is not connected or authorized in the current client

If Jira access is unavailable, continue with a local plan and note the missing linkage instead of blocking the automation work.
