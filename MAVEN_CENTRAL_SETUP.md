# Maven Central Publishing Setup

This guide explains how to publish PerfScout to Maven Central.

## Prerequisites

### 1. Sonatype OSSRH Account
- Go to [OSSRH](https://s01.oss.sonatype.org/)
- Create an account
- Request access to the `io.github.dee2604` group
- Wait for approval (usually 1-2 business days)

### 2. GPG Key Setup
```bash
# Generate GPG key
gpg --gen-key

# List your keys
gpg --list-secret-keys

# Export public key
gpg --armor --export your-email@example.com > public-key.asc

# Export private key
gpg --armor --export-secret-key your-email@example.com > private-key.asc
```

### 3. GitHub Secrets Setup

Add these secrets to your GitHub repository (Settings → Secrets and variables → Actions):

| Secret Name | Description |
|-------------|-------------|
| `OSSRH_USERNAME` | Your Sonatype username |
| `OSSRH_PASSWORD` | Your Sonatype password |
| `GPG_PRIVATE_KEY` | Your GPG private key (content of private-key.asc) |
| `GPG_PASSPHRASE` | Your GPG passphrase |
| `SIGNING_KEY_ID` | Your GPG key ID (last 8 characters) |
| `SIGNING_PASSWORD` | Your GPG passphrase |
| `SIGNING_SECRET_KEY_RING_FILE` | Path to your GPG keyring file |

## Publishing Process

### 1. Prepare Release
```bash
# Run the release script
./scripts/release.sh 1.1.4
```

### 2. Automatic Publishing
- The GitHub Actions workflow will trigger when you push a tag
- It will build, sign, and publish to Maven Central staging
- Check the Actions tab for progress

### 3. Release to Production
- Go to [OSSRH Staging](https://s01.oss.sonatype.org/)
- Find your staged repository
- Click "Close" then "Release"

## Usage

After publishing, users can include PerfScout in their projects:

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.dee2604:perfscout:1.1.8")
}
```

## Troubleshooting

### Common Issues

1. **Authentication Failed**
   - Verify OSSRH credentials
   - Check if your account has access to the group

2. **Signing Failed**
   - Verify GPG key setup
   - Check that all signing secrets are correct

3. **Version Already Exists**
   - Increment version number
   - Update all version references

### Support

- Check [OSSRH Documentation](https://central.sonatype.com/publish/)
- Review [GitHub Actions logs](https://github.com/deekshasinghal326/perfScout/actions) 