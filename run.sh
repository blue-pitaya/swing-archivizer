#!/bin/sh

set -euo pipefail
cd "$(dirname "$0")"

mvn package
java -jar target/swingarchivizer-1.0.jar
