# Use Case Narrative: Register New Patriot Thanks User

## Use Case Information

| Field              | Description                                                                                                                                                                                                         |
|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Use Case Name**  | Register New Patriot Thanks User                                                                                                                                                                                    |
| **Use Case ID**    | UC-PT-001                                                                                                                                                                                                           |
| **Primary Actor**  | Unregistered Visitor                                                                                                                                                                                                |
| **Stakeholders**   | Visitor — wants a quick, simple registration process; Business Owner — wants more verified users browsing their discounts; Platform Admin — wants clean data and protection against bot registrations                |
| **Description**    | An unregistered visitor creates a new Patriot Thanks account by providing personal information, selecting a service status, and completing CAPTCHA verification. Upon success, the user is auto-logged in and redirected to the business listings. |
| **Trigger**        | Visitor clicks the "Register" link or "Register Now" button from the Patriot Thanks home page or navigation bar.                                                                                                    |
| **Preconditions**  | 1. The visitor is not currently authenticated. 2. The visitor has a valid email address not already registered in the system. 3. The Patriot Thanks application is running and accessible.                          |
| **Priority**       | High — Registration is required for all authenticated features.                                                                                                                                                     |
| **Frequency**      | Multiple times daily during active enrollment periods.                                                                                                                                                              |

---

## Main Success Scenario (Basic Flow)

| Step | Actor      | System (`PatriotAuthController` / Service Layer)                                                                                                                                         |
|------|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1    | Navigates to `/patriot/register` via the navbar "Register" link or the home page card. | |
| 2    |            | Creates a blank `PatriotUser` object, adds it and the Turnstile CAPTCHA site key to the model, and returns the `patriotRegisterForm` view.                                              |
| 3    | Fills in all required fields: First Name, Last Name, Email, Password, and selects a status (e.g., "Veteran"). Optionally enters a phone number. | |
| 4    | Completes the Cloudflare Turnstile CAPTCHA widget. | |
| 5    | Clicks "Create Account" to submit the form (`POST /patriot/register`). | |
| 6    |            | Validates the Turnstile token against the Cloudflare `siteverify` endpoint. Verification passes.                                                                                         |
| 7    |            | Validates all form fields using Jakarta Bean Validation (`@Valid`). All fields pass.                                                                                                     |
| 8    |            | Captures the raw (unhashed) password for auto-login purposes.                                                                                                                            |
| 9    |            | Calls `PatriotUserService.registerNewUser()`: hashes the password via `BCryptPasswordEncoder`, determines the default role from `statusId`, looks up the role in `PatriotRoleRepository`, assigns it, and saves the user to `patriot_users`. |
| 10   |            | Auto-logs the user in: creates a `UsernamePasswordAuthenticationToken`, authenticates via the Patriot `AuthenticationManager`, creates a new `SecurityContext`, and persists it to the HTTP session. |
| 11   |            | Sets a flash message: *"Welcome to Patriot Thanks, [firstName]! Your account has been created successfully."*                                                                            |
| 12   |            | Redirects the user to `/businesses`.                                                                                                                                                     |
| 13   |            | Business listings page renders with the Patriot Thanks layout, showing the authenticated navbar ("Edit Profile" / "Logout") and the welcome flash message.                              |

---

## Alternative Flows

### Alternative Flow A — CAPTCHA Verification Fails
> Branches from Main Flow Step 6

| Step | Description                                                                                                                    |
|------|--------------------------------------------------------------------------------------------------------------------------------|
| A1   | The Turnstile token is missing or the Cloudflare verification endpoint returns a failure response.                             |
| A2   | The system adds a `turnstileError` message to the model: *"Please complete the CAPTCHA verification."* or *"CAPTCHA verification failed. Please try again."* |
| A3   | The system re-renders the registration form with the error displayed below the CAPTCHA widget.                                 |
| A4   | The visitor's previously entered form data is preserved. Returns to Main Flow Step 3.                                          |

---

### Alternative Flow B — Form Validation Fails
> Branches from Main Flow Step 7

| Step | Description                                                                                                                    |
|------|--------------------------------------------------------------------------------------------------------------------------------|
| B1   | One or more fields fail Jakarta validation (e.g., blank first name, invalid email format, password without uppercase/lowercase/number, password fewer than 8 characters). |
| B2   | The `BindingResult` object captures the field-level errors.                                                                    |
| B3   | The system re-renders the registration form. Each invalid field displays its specific error message below the input (rendered by the `inputField` Thymeleaf fragment). |
| B4   | The visitor corrects the errors. Returns to Main Flow Step 5.                                                                  |

---

### Alternative Flow C — Duplicate Email Address
> Branches from Main Flow Step 9

| Step | Description                                                                                                                    |
|------|--------------------------------------------------------------------------------------------------------------------------------|
| C1   | `PatriotUserService.registerNewUser()` throws a `RuntimeException` because the email already exists in the `patriot_users` table. |
| C2   | The controller catches the exception and adds a field-level error to the email field: *"This email is already registered."*   |
| C3   | The system re-renders the registration form with the duplicate email error displayed.                                          |
| C4   | The visitor enters a different email address. Returns to Main Flow Step 5.                                                     |

---

### Alternative Flow D — Auto-Login Fails
> Branches from Main Flow Step 10

| Step | Description                                                                                                                    |
|------|--------------------------------------------------------------------------------------------------------------------------------|
| D1   | The `AuthenticationManager.authenticate()` call throws an exception (e.g., session issue or authentication provider error).   |
| D2   | The user's account has been successfully created and saved in the database.                                                    |
| D3   | The system sets a flash message: *"Account created, but auto-login failed. Please log in."*                                   |
| D4   | The system redirects the user to `/patriot/login` to manually log in.                                                         |

---

## Postconditions

### Success Postconditions

| # | Condition                                                                                                             |
|---|-----------------------------------------------------------------------------------------------------------------------|
| 1 | A new `PatriotUser` record exists in the `patriot_users` table with a BCrypt-hashed password.                        |
| 2 | The user has been assigned one role in the `patriot_user_roles` join table based on their selected status.            |
| 3 | The user is authenticated and has an active HTTP session with a valid `SecurityContext`.                              |
| 4 | The navbar displays "Edit Profile" and "Logout" instead of "Register" and "Login".                                   |

### Failure Postconditions

No new user record is created in the database. The visitor remains on the registration form with their previously entered data preserved and can correct any errors to try again.

---

## Business Rules

| Rule ID | Description                                                                                                                                                     |
|---------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| BR-001  | Passwords must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, and one digit.                                         |
| BR-002  | Email addresses must be unique across the `patriot_users` table.                                                                                                |
| BR-003  | All registration attempts must pass Cloudflare Turnstile CAPTCHA verification to prevent bot registrations.                                                     |
| BR-004  | Passwords are stored using BCrypt hashing and are never stored in plain text.                                                                                   |
| BR-005  | Each user is assigned exactly one default role based on their selected status: Veteran → `VETERAN`, Active Duty → `ACTIVE_DUTY`, First Responder → `FIRST_RESPONDER`, Military Spouse → `MILITARY_SPOUSE`, Business Owner → `BUSINESS_OWNER`, Supporter → `SUPPORTER`. |

---

*Patriot Thanks — Edward McKeown — Spring 2026*
