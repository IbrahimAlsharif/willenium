# WeWill-Darent Website

This project includes GitHub Actions workflows for automated tests.

### Running Workflows Manually

To manually trigger the **RequestToBookFlow** workflow for a branch such as `RequestBookingFlows`, ensure that the branch contains the `.github/workflows/request-to-book.yml` file. The workflow has a `workflow_dispatch` event so you can run it from the Actions tab once the file is present in that branch.

Additionally, the workflow now automatically runs on push events to the `RequestBookingFlows` branch.

