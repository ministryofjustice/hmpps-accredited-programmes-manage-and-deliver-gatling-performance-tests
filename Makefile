.PHONY: local-up down clean logs ps seed teardown reseed restart-wiremock run-group-allocation

API_DIR ?= ../hmpps-accredited-programmes-manage-and-deliver-api
# Number of referrals to seed.
SEED_COUNT ?= 50
# Number of groups to seed (referrals are allocated onto these).
GROUP_COUNT ?= 5
# Simulation to run against the local stack.
SIMULATION ?= uk.gov.justice.digital.hmpps.team.acp.simulations.GroupAllocationSimulation

# Ensure files the api writes into the shared wiremock volume are owned by the host user.
export UID := $(shell id -u)
export GID := $(shell id -g)

# Bring the whole local stack up and wait until healthchecks pass.
local-up:
	docker compose up -d --wait --wait-timeout 300
	@echo ""
	@echo "Local stack ready:"
	@echo "  UI    http://localhost:3000"
	@echo "  API   http://localhost:8080"
	@echo "  AUTH  http://localhost:8090/auth"
	@echo ""
	@echo "Next: 'make seed' to generate referrals + groups, then 'make run-group-allocation'."

down:
	docker compose down

clean:
	docker compose down -v

logs:
	docker compose logs -f

ps:
	docker compose ps

# Seed referrals AND groups via the api script (its `seed` does both), then restart wiremock so it
# serves the new person stubs.
seed:
	cd $(API_DIR) && ./scripts/seed-data.sh seed $(SEED_COUNT) $(GROUP_COUNT)
	$(MAKE) restart-wiremock

# Remove all seeded data (groups then referrals). Wipes the previous run's allocations too.
teardown:
	cd $(API_DIR) && ./scripts/seed-data.sh teardown

# Clean slate: remove the previous run's data, then seed a fresh set of referrals and groups.
# This is what makes local runs repeatable — every run starts from the same unmutated data.
reseed: teardown seed

restart-wiremock:
	docker compose restart wiremock

# Reset to a clean referrals+groups set, then run the simulation. Running the sim locally always
# starts from fresh data, so the mutating journey doesn't accumulate state across runs.
run-group-allocation: reseed
	./gradlew gatlingRun --simulation $(SIMULATION)
