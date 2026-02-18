# Use Case Narrative: Add New Business

## Use Case Information

| Field | Description |
|-------|-------------|
| **Use Case Name** | Add New Business |
| **Use Case ID** | UC-BUS-001 |
| **Actor** | User (Site Administrator) |
| **Description** | A user adds a new business to the Patriot Thanks system so that veterans, active military, and first responders can discover its discounts and incentives. |
| **Preconditions** | 1. The user is on the Business List page (`/businesses`). 2. At least one Business Type exists in the database. |
| **Postconditions** | A new Business record is saved to the database and appears in the paginated business list. |
| **Trigger** | The user clicks the "Add Business" button on the Business List page. |

## Main Success Scenario (Happy Path)

| Step | Actor | System |
|------|-------|--------|
| 1 | Clicks the "Add Business" button. | |
| 2 | | Receives `GET /businesses/new` request. |
| 3 | | Creates a new, empty `Business` object. |
| 4 | | Retrieves all active `BusinessType` records from the database. |
| 5 | | Returns the `createOrUpdateBusinessForm` view with the empty business and the list of business types. |
| 6 | Fills in the form fields: Name (required), Business Type (required), Website (optional), and Description (optional). | |
| 7 | Clicks the "Add Business" submit button. | |
| 8 | | Receives `POST /businesses/new` request with form data. |
| 9 | | Validates the submitted data against the model constraints (`@NotBlank` on name, `@NotNull` on businessType). |
| 10 | | Validation passes. Saves the new `Business` to the database via the repository. |
| 11 | | Redirects the user to `GET /businesses` (the business list page). |
| 12 | | Displays the paginated business list, which now includes the newly added business. |

## Alternate Scenario: Validation Fails

| Step | Actor | System |
|------|-------|--------|
| 9a | | Validation fails (e.g., name is blank or business type is not selected). |
| 9b | | Returns the `createOrUpdateBusinessForm` view with error messages displayed next to the invalid fields. |
| 9c | Corrects the invalid fields and resubmits the form. | Returns to Step 8. |
