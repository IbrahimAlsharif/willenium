# Selenium MCP

## Repo Configuration

This repo already declares a project-level MCP server in `.mcp.json`:

- server name: `selenium`
- command: `npx -y @angiejones/mcp-selenium@0.1.21`

That means MCP-aware clients can pick up the Selenium server from the repository without per-user setup in the agent instructions.

## What It Is Good For

Selenium MCP is best used as a live browser probe:

- open the site and inspect the real DOM
- validate selectors before encoding them in Java
- confirm navigation, visibility, and text behavior
- reproduce UI failures interactively

It is not the final artifact. The final artifact in this repo should still be Java/TestNG code plus any needed suite and test-data updates.

When using Selenium MCP for planning or refinement, inspect the experience from a business view before dropping to locators:

- what user intent the page is serving
- what action or decision the business needs next
- which trust signals or proof points are visible
- where hesitation or drop-off is likely
- what would count as a misleading success state
- what recovery path exists when the ideal path fails

## Tool Surface

For the pinned `@angiejones/mcp-selenium` package, the public docs describe tools in four practical groups:

- session/browser control:
  `start_browser`, `close_session`
- navigation:
  `navigate`
- element lookup and interaction:
  `find_element`, `click_element`, `send_keys`, `get_element_text`, `hover`, `drag_and_drop`, `double_click`, `right_click`
- keyboard and evidence:
  `press_key`, `upload_file`, `take_screenshot`

Locator strategies documented by the package include:

- `id`
- `css`
- `xpath`
- `name`
- `tag`
- `class`

The package docs show `start_browser` examples for Chrome and Firefox. The upstream GitHub README also mentions Edge support, so treat browser availability as server-version-dependent and prefer Chrome unless there is a project-specific reason to do otherwise.

## Recommended Usage Pattern

1. Inspect the Java framework first so you know where the eventual code belongs.
2. Use Selenium MCP only for the unknown or risky part of the flow.
3. Record business checkpoints as well as UI checkpoints.
4. Translate the validated interaction back into repo-native code:
   - helper methods in `tests/.../<Feature>.java`
   - assertions in `tests/.../<Feature>Test.java`
   - JSON keys in test data
   - XML suite registration under `flows/...`
5. Close the MCP session once you have the evidence you need.

## Source Notes

This summary is based on:

- the repo-local `.mcp.json`
- the `@angiejones/mcp-selenium` public package/README
- the upstream `angiejones/mcp-selenium` GitHub README
