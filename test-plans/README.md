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

If a user explicitly asks for a flow-local blueprint, create a sibling Markdown file under `flows/...` that references the same `plan_id` as the canonical plan in this folder.

When fulfilling a planning request, the agent should actually create or update the Markdown file on disk. Returning a plan only in chat is not sufficient.

## Required Linking Metadata

Each plan should include front matter with, at minimum:

- `plan_id`
- `target_name`
- `target_url`
- `target_slug`
- `app`
- `flow_xml`
- `java_helper`
- `java_test`
- `testdata_sections`
- `status`

These fields keep the plan, XML, Java, and JSON assets tied together so later updates can find the right files.

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

At a minimum, cover:

- target summary
- scope and out-of-scope boundaries
- assumptions and open questions
- environment and setup needs
- test data required
- happy paths
- negative paths
- edge cases
- localization coverage when relevant
- automation mapping to XML, Java, and JSON artifacts
