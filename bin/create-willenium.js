#!/usr/bin/env node

const { spawnSync } = require("child_process");
const fs = require("fs");
const https = require("https");
const os = require("os");
const path = require("path");

const TEMPLATE_ARCHIVE_URL =
  "https://codeload.github.com/IbrahimAlsharif/willenium/tar.gz/refs/heads/main";
const SKIP_ROOT_ENTRIES = new Set([
  ".git",
  ".idea",
  ".vscode",
  "bin",
  "extent-reports",
  "node_modules",
  "package-lock.json",
  "package.json",
  "screenshots",
  "target",
  "template-resources"
]);
const SKIP_ANYWHERE = new Set([
  ".DS_Store",
  ".git",
  ".idea",
  ".npmignore",
  "extent-reports",
  "node_modules",
  "package-lock.json",
  "screenshots",
  "target"
]);

function printHelp() {
  console.log(`Usage:
  npx create-willenium <project-name> [--group-id com.example.tests] [--force]

Options:
  --group-id <value>  Override the generated Maven groupId
  --force             Allow creation inside an existing empty directory
  --help              Show this help message`);
}

function exitWithError(message) {
  console.error(`Error: ${message}`);
  process.exit(1);
}

function removeDirectory(targetPath) {
  if (fs.existsSync(targetPath)) {
    fs.rmSync(targetPath, { recursive: true, force: true });
  }
}

function parseArgs(argv) {
  const args = argv.slice(2);
  const options = {
    force: false,
    groupId: null,
    projectName: null
  };

  for (let index = 0; index < args.length; index += 1) {
    const arg = args[index];

    if (arg === "--help" || arg === "-h") {
      printHelp();
      process.exit(0);
    }

    if (arg === "--force") {
      options.force = true;
      continue;
    }

    if (arg === "--group-id") {
      const value = args[index + 1];
      if (!value) {
        exitWithError("Missing value for --group-id.");
      }
      options.groupId = value.trim();
      index += 1;
      continue;
    }

    if (!options.projectName) {
      options.projectName = arg.trim();
      continue;
    }

    exitWithError(`Unknown argument: ${arg}`);
  }

  if (!options.projectName) {
    printHelp();
    exitWithError("Please provide a project name.");
  }

  return options;
}

function sanitizeArtifactId(projectName) {
  return projectName
    .trim()
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/^-+|-+$/g, "") || "willenium-project";
}

function toGroupId(artifactId) {
  return `com.${artifactId.replace(/-/g, ".")}`;
}

function ensureDestination(destDir, force) {
  if (!fs.existsSync(destDir)) {
    fs.mkdirSync(destDir, { recursive: true });
    return { created: true };
  }

  const entries = fs.readdirSync(destDir);
  if (entries.length === 0 && force) {
    return { created: false };
  }

  if (entries.length === 0) {
    return { created: false };
  }

  exitWithError(
    `Destination already exists and is not empty: ${destDir}. Choose another folder name.`
  );
}

function downloadFile(url, destination) {
  return new Promise((resolve, reject) => {
    const request = https.get(
      url,
      {
        headers: {
          "User-Agent": "create-willenium"
        }
      },
      (response) => {
        if (
          response.statusCode &&
          response.statusCode >= 300 &&
          response.statusCode < 400 &&
          response.headers.location
        ) {
          response.resume();
          downloadFile(response.headers.location, destination).then(resolve).catch(reject);
          return;
        }

        if (response.statusCode !== 200) {
          response.resume();
          reject(
            new Error(
              `GitHub returned status ${response.statusCode || "unknown"} while downloading the template archive.`
            )
          );
          return;
        }

        const file = fs.createWriteStream(destination);
        response.pipe(file);

        file.on("finish", () => {
          file.close((closeError) => {
            if (closeError) {
              reject(closeError);
              return;
            }

            resolve();
          });
        });

        file.on("error", (error) => {
          file.close(() => {
            fs.rmSync(destination, { force: true });
            reject(error);
          });
        });
      }
    );

    request.on("error", (error) => {
      fs.rmSync(destination, { force: true });
      reject(error);
    });
  });
}

function extractArchive(archivePath, extractDir) {
  const result = spawnSync("tar", ["-xzf", archivePath, "-C", extractDir], {
    stdio: "pipe"
  });

  if (result.status !== 0) {
    const stderr = (result.stderr || "").toString().trim();
    const reason = stderr ? ` ${stderr}` : "";
    exitWithError(
      `Unable to extract the downloaded GitHub archive. Please ensure the \`tar\` command is available.${reason}`
    );
  }
}

function getExtractedRoot(extractDir) {
  const entries = fs
    .readdirSync(extractDir, { withFileTypes: true })
    .filter((entry) => entry.isDirectory());

  if (entries.length !== 1) {
    exitWithError("Unable to locate the extracted template directory from GitHub.");
  }

  return path.join(extractDir, entries[0].name);
}

function copyRecursive(sourceDir, targetDir, isRoot = false) {
  const entries = fs.readdirSync(sourceDir, { withFileTypes: true });

  for (const entry of entries) {
    if (isRoot && SKIP_ROOT_ENTRIES.has(entry.name)) {
      continue;
    }

    if (SKIP_ANYWHERE.has(entry.name)) {
      continue;
    }

    if (entry.name.endsWith(".tgz")) {
      continue;
    }

    const sourcePath = path.join(sourceDir, entry.name);
    const targetPath = path.join(targetDir, entry.name);

    if (entry.isDirectory()) {
      fs.mkdirSync(targetPath, { recursive: true });
      copyRecursive(sourcePath, targetPath, false);
      continue;
    }

    fs.copyFileSync(sourcePath, targetPath);
  }
}

function replacePomMetadata(projectDir, artifactId, groupId) {
  const pomPath = path.join(projectDir, "pom.xml");
  let pom = fs.readFileSync(pomPath, "utf8");

  pom = pom.replace(/<groupId>WEWILL<\/groupId>/, `<groupId>${groupId}</groupId>`);
  pom = pom.replace(/<artifactId>willenium<\/artifactId>/, `<artifactId>${artifactId}</artifactId>`);

  fs.writeFileSync(pomPath, pom);
}

function updateReadme(projectDir, projectName) {
  const readmePath = path.join(projectDir, "README.md");
  if (!fs.existsSync(readmePath)) {
    return;
  }

  const original = fs.readFileSync(readmePath, "utf8");
  const updated = original
    .replace(/^# Willenium$/m, `# ${projectName}`)
    .replace(
      /^Willenium is .*$/m,
      `${projectName} is based on the Willenium Selenium UI automation framework`
    )
    .replace(
      /## Quick Start[\s\S]*?(?=\n## Troubleshooting\n)/,
      `## Quick Start

\`\`\`bash
cd ${projectName}
mvn test -PBrowseExampleWeWillEnglish
\`\`\`
`
    )
    .replace(
      /^- If you publish the package, run `npm publish` from the repository directory, not from `~`\.\n/m,
      ""
    )
    .replace(/^- To verify the published version, run `npm view create-willenium version`\.\n/m, "");

  fs.writeFileSync(readmePath, updated);
}

function writeGitignore(projectDir, templateRoot) {
  const source = path.join(templateRoot, "template-resources", "gitignore");
  const target = path.join(projectDir, ".gitignore");
  fs.copyFileSync(source, target);
}

async function prepareTemplateSource() {
  const tempRoot = fs.mkdtempSync(path.join(os.tmpdir(), "create-willenium-"));
  const archivePath = path.join(tempRoot, "template.tar.gz");
  const extractDir = path.join(tempRoot, "extract");

  fs.mkdirSync(extractDir, { recursive: true });

  try {
    console.log("Fetching the latest template from GitHub main...");
    await downloadFile(TEMPLATE_ARCHIVE_URL, archivePath);
    extractArchive(archivePath, extractDir);
    return {
      cleanup: () => removeDirectory(tempRoot),
      templateRoot: getExtractedRoot(extractDir)
    };
  } catch (error) {
    removeDirectory(tempRoot);
    exitWithError(
      `Unable to download the latest template from GitHub. Please check your internet access to github.com and codeload.github.com. ${error.message}`
    );
  }
}

async function main() {
  const options = parseArgs(process.argv);
  const destination = path.resolve(process.cwd(), options.projectName);
  const projectName = path.basename(destination);
  const artifactId = sanitizeArtifactId(projectName);
  const groupId = options.groupId || toGroupId(artifactId);
  const templateSource = await prepareTemplateSource();
  const destinationState = ensureDestination(destination, options.force);

  try {
    copyRecursive(templateSource.templateRoot, destination, true);
    writeGitignore(destination, templateSource.templateRoot);
    replacePomMetadata(destination, artifactId, groupId);
    updateReadme(destination, projectName);
  } catch (error) {
    if (destinationState.created) {
      removeDirectory(destination);
    }
    templateSource.cleanup();
    exitWithError(error.message);
  }

  templateSource.cleanup();

  console.log(`
Created ${projectName} in ${destination}

Next steps:
  cd ${destination}
  mvn test -PBrowseExampleWeWillEnglish
`);
}

main().catch((error) => {
  exitWithError(error.message);
});
