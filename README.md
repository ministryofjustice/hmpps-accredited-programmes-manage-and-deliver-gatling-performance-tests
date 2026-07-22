# hmpps-accredited-programmes-manage-and-deliver-gatling-performance-tests

Performance test setup use the following:

1. Kotlin
2. Gatling
3. Gradle

Requires JDK 25 (the Gradle Java toolchain is pinned to 25).

# Environment pre-requisites

Before running load tests, consider your target environment's configuration:

- Should emails be disabled during load testing?

- Should domain events be disabled during load testing?

- Is the environment scaled the same as production, and does it matter? Scaling includes
  Size of RDS instance
  Number of pods for API/UI
  Memory allocated to pods for API/UI

# Prep for running load test

To kick off the load tests you will need to do the following:

1. Port forward to Access the TEST RDS Database. Follow guide here -
   https://github.com/ministryofjustice/hmpps-accredited-programmes-manage-and-deliver-api/blob/main/docs/how-to/access-dev-database-remotely.md
2. Populate the`auth_username` and `auth_password` values in `local.properties` with the appropriate test user for the
   environment.

# Configuration

Every setting (target environment, database connection, session cookie, load profile) is resolved
in this order of precedence:

1. JVM system property — e.g. `-Ddb_port=5433`
2. Environment variable — the key in UPPER_SNAKE_CASE, e.g. `db_port` → `DB_PORT`
3. `local.properties` file in the repo root (gitignored)

For local development, copy the template and fill in the values from the prep steps above:

```
cp local.properties.example local.properties
```

`local.properties` is gitignored because it holds secrets (database password, session cookie) —
never commit it. Anything set in the file can still be overridden per-run with `-D` flags or
environment variables.

# Running load tests

## From IntelliJ

A shared run configuration **Gatling: CaseListSimulation** is committed in the `.run/` folder and
appears automatically in the run configuration dropdown. Fill in `local.properties` (see above) and
run it. Individual values can also be set as environment variables in the run configuration's
"Environment variables" field (Edit Configuration → Environment variables), using the
UPPER_SNAKE_CASE names.

## From the command line

```
./gradlew gatlingRun --simulation uk.gov.justice.digital.hmpps.team.acp.simulations.CaseListSimulation
```

With `local.properties` in place no other arguments are needed. Individual values can be
overridden per-run, e.g.:

```
./gradlew gatlingRun --simulation uk.gov.justice.digital.hmpps.team.acp.simulations.CaseListSimulation -Dcase_list_concurrent_users=5 -Dcase_list_test_duration_minutes=10
```

## Available settings

| Key                                                         | Required             | Description                                                                                   |
|-------------------------------------------------------------|----------------------|-----------------------------------------------------------------------------------------------|
| `protocol`                                                  | yes                  | `https` (or `http` for local)                                                                 |
| `domain`                                                    | yes                  | Target host, e.g. `accredited-programmes-manage-and-deliver-dev.hmpps.service.justice.gov.uk` |
| `db_port`                                                   | yes                  | Local port from the port-forward step                                                         |
| `db_name`                                                   | yes                  | From the port-forward step                                                                    |
| `db_username`                                               | yes                  | From the port-forward step                                                                    |
| `db_password`                                               | yes                  | From the port-forward step                                                                    |
| `auth_username`                                             | yes                  | HMPPS Auth test account username, used by each virtual user to sign in                        |
| `auth_password`                                             | yes                  | HMPPS Auth test account password                                                              |
| `authBaseUrl`                                               | no (defaults to dev) | HMPPS Auth base URL, e.g. `https://sign-in-dev.hmpps.service.justice.gov.uk`                  |
| `hmpps-accredited-programmes-manage-and-deliver-ui.session` | no                   | Debug override: skip sign-in and share this browser session cookie across all virtual users   |
| `case_list_concurrent_users`                                | no (default 2)       | Number of concurrent virtual users for the case list simulation                              |
| `case_list_test_duration_minutes`                           | no (default 5)       | Case list test duration in minutes                                                            |
| `create_group_concurrent_users`                             | no (default 1)       | Number of concurrent virtual users for the create group simulation                           |
| `create_group_test_duration_minutes`                        | no (default 5)       | Create group test duration in minutes                                                         |
| `schedule_overview_concurrent_users`                        | no (default 5)       | Number of concurrent virtual users for the schedule overview read simulation                  |
| `schedule_overview_test_duration_minutes`                   | no (default 5)       | Schedule overview test duration in minutes                                                    |

# Troubleshooting

If a run fails and the report alone doesn't explain why, set the `GATLING_HTTP_LOG` environment
variable to `DEBUG` to log every failing HTTP request and response body (`TRACE` logs all
requests). Warning: logged sign-in requests include the credentials, so don't share the output.

A failed sign-in prints the URL it landed on to the console — landing on
`.../auth/sign-in?error=invalid` means HMPPS Auth rejected the username/password.

# Code style

Kotlin code style is enforced with [ktlint](https://pinterest.github.io/ktlint/) via the
same Gradle plugin our other HMPPS repos use:

```
./gradlew ktlintCheck    # report violations
./gradlew ktlintFormat   # auto-fix what can be fixed
```

To run the formatter automatically before every commit, install the git hook once:

```
./gradlew addKtlintFormatGitPreCommitHook
```




