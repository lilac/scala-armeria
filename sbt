#!/usr/bin/env sh

sbt -Dsocks.proxyHost=localhost -Dsocks.proxyPort=1090
#sbt -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=1090
