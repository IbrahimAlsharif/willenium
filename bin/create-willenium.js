#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const PACKAGE_ROOT = path.resolve(__dirname, "..");
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
    return;
  }

  const entries = fs.readdirSync(destDir);
  if (entries.length === 0 && force) {
    return;
  }

  if (entries.length === 0) {
    return;
  }

  exitWithError(
    `Destination already exists and is not empty: ${destDir}. Choose another folder name.`
  );
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
      /^Willenium is a Selenium-based UI automation framework/m,
      `${projectName} is based on the Willenium Selenium UI automation framework`
    )
    .replace(/\n## Bootstrap With `npx`[\s\S]*?\n## MCP Integration\n/, "\n## MCP Integration\n");

  fs.writeFileSync(readmePath, updated);
}

function writeGitignore(projectDir) {
  const source = path.join(PACKAGE_ROOT, "template-resources", "gitignore");
  const target = path.join(projectDir, ".gitignore");
  fs.copyFileSync(source, target);
}

function main() {
  const options = parseArgs(process.argv);
  const destination = path.resolve(process.cwd(), options.projectName);
  const projectName = path.basename(destination);
  const artifactId = sanitizeArtifactId(projectName);
  const groupId = options.groupId || toGroupId(artifactId);

  ensureDestination(destination, options.force);
  copyRecursive(PACKAGE_ROOT, destination, true);
  writeGitignore(destination);
  replacePomMetadata(destination, artifactId, groupId);
  updateReadme(destination, projectName);

  console.log(`
Created ${projectName} in ${destination}

Next steps:
  cd ${destination}
  mvn test -PBrowseAssistantHomeEnglish
`);
}

main();
