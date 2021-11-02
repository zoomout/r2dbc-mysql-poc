#!/bin/bash
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

DOCKER_DIR=${DIR=}/..

echo "     * Stopping ..."
docker_files="-f ${DOCKER_DIR}/docker-compose.yml"
docker-compose $docker_files down -v;
docker system prune -f --volumes
echo "     * Stopped"
