#!/bin/bash

TAG_VERSION=$1

TAG_VERSION=${TAG_VERSION##*/}
echo current tag version is $TAG_VERSION


VERSION=`echo $TAG_VERSION |  tr 'A-Z' 'a-z'`

echo Ignore case tag version is $VERSION

VERSION=${VERSION/-snapshot/}
VERSION=${VERSION/v/}

echo current version is:$VERSION

mvn versions:set -DnewVersion=$VERSION
