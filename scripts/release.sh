#!/bin/bash

# PerfScout Release Script
# Usage: ./scripts/release.sh [version]

set -e

VERSION=${1:-"1.1.4"}
TAG="v$VERSION"

echo "🚀 Preparing PerfScout release $VERSION"

# Check if we're on main branch
if [[ $(git branch --show-current) != "main" ]]; then
    echo "❌ Error: Must be on main branch to release"
    exit 1
fi

# Check if working directory is clean
if [[ -n $(git status --porcelain) ]]; then
    echo "❌ Error: Working directory is not clean. Please commit or stash changes."
    exit 1
fi

# Update version in build.gradle.kts
echo "📝 Updating version to $VERSION in build.gradle.kts..."
sed -i '' "s/version = \"[^\"]*\"/version = \"$VERSION\"/" app/build.gradle.kts

# Update version in README
echo "📝 Updating version in README..."
sed -i '' "s/implementation(\"io\.github\.deekshasinghal326:perfscout:[^\"]*\")/implementation(\"io.github.deekshasinghal326:perfscout:$VERSION\")/g" README.md

# Update version in docs
echo "📝 Updating version in documentation..."
sed -i '' "s/implementation(\"io\.github\.deekshasinghal326:perfscout:[^\"]*\")/implementation(\"io.github.deekshasinghal326:perfscout:$VERSION\")/g" docs/index.html

# Commit changes
echo "📝 Committing version changes..."
git add .
git commit -m "chore: bump version to $VERSION"

# Create and push tag
echo "🏷️ Creating tag $TAG..."
git tag -a "$TAG" -m "Release $VERSION"
git push origin main
git push origin "$TAG"

echo "✅ Release $VERSION prepared successfully!"
echo "📦 The GitHub Actions workflow will automatically publish to Maven Central"
echo "🔗 Check the Actions tab for progress: https://github.com/deekshasinghal326/perfScout/actions" 