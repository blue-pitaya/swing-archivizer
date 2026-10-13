#!/bin/sh

set -euo pipefail
cd "$(dirname "$0")"

mvn clean package
java -Dawt.useSystemAAFontSettings=lcd -jar target/swingarchivizer-1.0.jar
