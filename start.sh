#!/usr/bin/env bash
# Las opciones adicionales al -jar, corresponde al cambio de seguridad de java 16 al 17
./gradlew shadowjar && java --add-opens java.base/java.lang=ALL-UNNAMED -jar build/libs/app.jar