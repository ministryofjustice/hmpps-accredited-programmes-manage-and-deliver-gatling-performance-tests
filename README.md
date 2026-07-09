# hmpps-accredited-programmes-manage-and-deliver-gatling-performance-tests

Performance test setup use the following:
1. Kotlin
2. Gatling
3. Gradle

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
2. Copy the `hmpps-accredited-programmes-manage-and-deliver-ui.session` Application cookie from your web browser
3. Login to your web application
  - Right-click browser > Inspect
  - Go to Application tab > Storage in left nav > Expand Cookies
  - Find the `hmpps-accredited-programmes-manage-and-deliver-ui.session` cookie in the list and copy its' value from the Value column
  - Copy the value to run load test in next step

# Running load tests
Change directory in a terminal and navigate to this repo's root folder

Run a simulation:

`./gradlew gatlingRun --simulation uk.gov.justice.digital.hmpps.team.acp.simulations.CaseListSimulation -Dprotocol=https -Ddomain=accredited-programmes-manage-and-deliver-dev.hmpps.service.justice.gov.uk -Ddb_port= DB_PORT_VALUE -Ddb_name=DB_NAME_VALUE -Ddb_username=DB_USER_NAME_VALUE -Ddb_password=DB_PASSWORD_VALUE -Dhmpps-accredited-programmes-manage-and-deliver-ui.session= VALUE_FROM_STEP_3`

DB_PORT_VALUE = You will get this from outcome of first command for Port forwarding.

DB_NAME_VALUE = You will get this from outcome of first command for Port forwarding.

DB_USER_NAME_VALUE = You will get this from outcome of first command for Port forwarding.

DB_PASSWORD_VALUE = You will get this from outcome of first command for Port forwarding.

VALUE_FROM_STEP_3 = value for key - `hmpps-accredited-programmes-manage-and-deliver-ui.session`




