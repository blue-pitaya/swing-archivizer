#!/bin/sh

set -euo pipefail
cd "$(dirname "$0")"

mkdir -p download
find download -mindepth 1 -delete
mvn clean package
java -Dawt.useSystemAAFontSettings=lcd -jar target/swingarchivizer-1.0.jar
