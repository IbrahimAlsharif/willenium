import path from 'node:path';

export type ExecutionBranch = 'production' | 'staging';
export type ExecutionLanguage = 'english' | 'arabic';

export interface RuntimeProfile {
  branch: ExecutionBranch;
  language: ExecutionLanguage;
  dataFilePath: string;
  waitTimeoutMs: number;
  interactionRetryAttempts: number;
  interactionRetryDelayMs: number;
  highlightInteractions: boolean;
  verifyTypedText: boolean;
}

export interface RuntimeProfileOverrides {
  branch?: string;
  language?: string;
}

const PROFILE_DATA_FILE_MAP: Record<ExecutionBranch, Record<ExecutionLanguage, string>> = {
  production: {
    english: 'exampleProductionEnglish.json',
    arabic: 'exampleProductionArabic.json',
  },
  staging: {
    english: 'exampleStagingEnglish.json',
    arabic: 'exampleStagingArabic.json',
  },
};

export function resolveRuntimeProfile(overrides: RuntimeProfileOverrides = {}): RuntimeProfile {
  const branch = normalizeBranch(overrides.branch ?? process.env.WILLENIUM_BRANCH);
  const language = normalizeLanguage(overrides.language ?? process.env.WILLENIUM_LANGUAGE);

  return {
    branch,
    language,
    dataFilePath: getProfileDataFilePath(branch, language),
    waitTimeoutMs: positiveIntFromEnv('WILLENIUM_UI_WAIT_TIMEOUT_SECONDS', 30) * 1000,
    interactionRetryAttempts: positiveIntFromEnv('WILLENIUM_UI_INTERACTION_RETRY_ATTEMPTS', 3),
    interactionRetryDelayMs: positiveIntFromEnv('WILLENIUM_UI_INTERACTION_RETRY_DELAY_MILLIS', 250),
    highlightInteractions: booleanFromEnv('WILLENIUM_UI_HIGHLIGHT_INTERACTIONS', false),
    verifyTypedText: booleanFromEnv('WILLENIUM_UI_VERIFY_TYPED_TEXT', true),
  };
}

export function getProfileDataFilePath(branch: ExecutionBranch, language: ExecutionLanguage): string {
  const fileName = PROFILE_DATA_FILE_MAP[branch][language];
  return path.resolve(process.cwd(), 'src/pw/config/testdata', fileName);
}

export function getProfileMatrixPaths(): string[] {
  return (['production', 'staging'] as ExecutionBranch[]).flatMap((branch) =>
    (['english', 'arabic'] as ExecutionLanguage[]).map((language) => getProfileDataFilePath(branch, language)),
  );
}

function normalizeBranch(input?: string): ExecutionBranch {
  const normalized = (input ?? 'staging').trim().toLowerCase();
  return normalized === 'production' ? 'production' : 'staging';
}

function normalizeLanguage(input?: string): ExecutionLanguage {
  const normalized = (input ?? 'english').trim().toLowerCase();
  return normalized === 'arabic' ? 'arabic' : 'english';
}

function booleanFromEnv(key: string, fallback: boolean): boolean {
  const rawValue = process.env[key];
  if (!rawValue) {
    return fallback;
  }

  const normalized = rawValue.trim().toLowerCase();
  return ['true', '1', 'yes', 'on'].includes(normalized);
}

function positiveIntFromEnv(key: string, fallback: number): number {
  const rawValue = process.env[key];
  if (!rawValue) {
    return fallback;
  }

  const parsed = Number.parseInt(rawValue, 10);
  if (Number.isNaN(parsed)) {
    return fallback;
  }

  return Math.max(1, parsed);
}
