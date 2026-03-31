# Test Plans

This folder stores Markdown test plans that act as the source of truth for generated Selenium/TestNG coverage in this repository.

## Default Location

Create plans here by default:

- `test-plans/<app>/<target-slug>.md`

Use lowercase kebab-case for `<target-slug>` as the canonical plan filename and identifier. Other artifact names can be derived from it in the style that fits their file type.

## Why Plans Live Here

TestNG XML files under `flows/` are execution artifacts. Markdown plans are design and coverage artifacts.

Keeping plans under `test-plans/` makes it easier to:

- create a comprehensive plan before generating tests
- regenerate tests from the plan later
- update the plan and then update the linked tests deterministically
- keep planning docs out of XML execution files

Treat the plan as the business-facing source of truth and the linked XML suite as the executable form of the owned journey.

If a user explicitly asks for a flow-local blueprint, create a sibling Markdown file under `flows/...` that references the same `plan_id` as the canonical plan in this folder.

When fulfilling a planning request, the agent should actually create or update the Markdown file on disk. Returning a plan only in chat is not sufficient.

## Required Linking Metadata

Each plan should include front matter with, at minimum:

- `plan_id`
- `target_name`
- `target_url`
- `target_slug`
- `app`
- `plan_scope`
- `plan_type`
- `business_goal`
- `user_value`
- `confidence_target`
- `unacceptable_outcomes`
- `scenario_map`
- `intensity_level`
- `flow_xml`
- `java_helper`
- `java_test`
- `testdata_sections`
- `status`

These fields keep the plan, XML, Java, and JSON assets tied together so later updates can find the right files.

When the work starts from Jira, also include:

- `jira_issue_key`
- `jira_issue_url`
- `affected_flows`
- `affected_tests`
- `affected_testdata`

These fields are optional for local-only work, but recommended whenever a bug or story should stay linked to its generated automation.

## Related Artifact Naming

Derive related artifact names from the same canonical target slug:

- plan: `test-plans/acme/checkout-guest.md`
- flow step suite: `flows/acme/steps/checkout_guest.xml`
- helper class: `tests.acme.checkout.CheckoutGuest`
- test class: `tests.acme.checkout.CheckoutGuestTest`
- JSON section: `checkoutGuest`

Recommended derivation:

- plan file: kebab-case
- flow XML file: snake_case if that reads better in suite names
- Java classes: PascalCase
- JSON sections: camelCase

The relationship to the canonical target slug should remain obvious across all of them.

## Authoring Guidance

Plans should be comprehensive enough that a later "generate the tests" request can be fulfilled without rediscovering the whole target.

Before drafting the plan, confirm:

- the business goal or business outcome the journey protects
- the primary user or actor
- the user value or contract that must succeed
- the key risk or unacceptable outcome
- the decision or confidence target the coverage should support
- the desired plan scope such as page, flow, journey, feature area, or regression slice
- the desired plan type such as smoke, happy-path, regression, or full coverage

Ask the business questions first.
Prefer a clean, structured question UI with short grouped prompts when the client supports it.

Do not assume every plan is a smoke plan. A user may want a full page-level or flow-level plan even before any code is generated.

At a minimum, cover:

- target summary
- business goal
- user value
- confidence target
- explicit plan scope and plan type
- scope and out-of-scope boundaries
- assumptions and open questions
- unacceptable outcomes
- impact analysis across existing flows, tests, and JSON data
- environment and setup needs
- test data required
- actual test cases for primary, alternate, negative, and edge scenarios
- scenario map
- localization coverage when relevant
- only the minimum secondary implementation notes needed to connect the plan to the framework

Name flows and plans after the business journey they own whenever possible rather than after a generic technical grouping.
Keep the main body of the plan business-first. Do not turn it into a file-structure checklist.

The minimum business context should live directly in the plan front matter and body instead of a separate canvas layer.
Use `quality/plans/` only when a broader strategy spans more than one owned journey or more than one executable plan.

The first planning deliverable should be a Markdown draft on disk for user review. Treat that reviewable draft as the checkpoint before broad generation work.
