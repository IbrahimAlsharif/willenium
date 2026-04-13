import fs from 'node:fs';

import {
  getProfileMatrixPaths,
  type ExecutionLanguage,
  type RuntimeProfile,
} from '../runtime/RuntimeProfile';

export interface WeWillHomeData {
  heroHeading: string;
  primaryCta: string;
  methodologyHeading: string;
  bilingualOfferingCta: string;
  footerSlogan: string;
}

export interface TestDataModel {
  'base-url': {
    env: Record<ExecutionLanguage, string>;
  };
  wewillHome: WeWillHomeData;
  [section: string]: unknown;
}

let cachedShapeSignature: string | null = null;

export class TestDataFactory {
  static load(profile: RuntimeProfile): TestDataModel {
    this.ensureProfileShapeConsistency();

    const rawJson = fs.readFileSync(profile.dataFilePath, 'utf8');
    const parsed = JSON.parse(rawJson) as TestDataModel;

    this.validateRequiredSections(parsed, profile.dataFilePath);
    return parsed;
  }

  private static ensureProfileShapeConsistency(): void {
    if (cachedShapeSignature !== null) {
      return;
    }

    const profileFiles = getProfileMatrixPaths();
    const parsedFiles = profileFiles.map((filePath) => ({
      filePath,
      payload: JSON.parse(fs.readFileSync(filePath, 'utf8')) as unknown,
    }));

    const [{ payload: referencePayload, filePath: referenceFilePath }] = parsedFiles;
    const referenceSignature = this.computeShapeSignature(referencePayload);

    for (const { filePath, payload } of parsedFiles.slice(1)) {
      const currentSignature = this.computeShapeSignature(payload);
      if (currentSignature !== referenceSignature) {
        throw new Error(
          `Test-data profile structure mismatch. ${filePath} does not match baseline shape from ${referenceFilePath}.`,
        );
      }
    }

    cachedShapeSignature = referenceSignature;
  }

  private static computeShapeSignature(payload: unknown, prefix = ''): string {
    if (Array.isArray(payload)) {
      const arrayItems = payload.map((item, index) => this.computeShapeSignature(item, `${prefix}[${index}]`));
      return arrayItems.join('|');
    }

    if (payload !== null && typeof payload === 'object') {
      const entries = Object.entries(payload as Record<string, unknown>)
        .sort(([left], [right]) => left.localeCompare(right))
        .map(([key, value]) => {
          const objectPath = prefix ? `${prefix}.${key}` : key;
          return this.computeShapeSignature(value, objectPath);
        });
      return entries.join('|');
    }

    const primitiveType = payload === null ? 'null' : typeof payload;
    return `${prefix}:${primitiveType}`;
  }

  private static validateRequiredSections(testData: TestDataModel, filePath: string): void {
    if (!testData['base-url']?.env?.english || !testData['base-url']?.env?.arabic) {
      throw new Error(`Missing base-url.env.english/arabic in ${filePath}`);
    }

    if (!testData.wewillHome) {
      throw new Error(`Missing wewillHome section in ${filePath}`);
    }
  }
}
