export const protectExampleHomeTrustFlow = {
  flowId: 'protect-example-home-trust-playwright',
  planId: 'framework_selenium-java-testng-to-playwright-typescript-migration',
  targetSlug: 'example-home-trust',
  app: 'examples',
  profileProjects: [
    'production-english',
    'production-arabic',
    'staging-english',
    'staging-arabic',
  ],
  specs: ['src/pw/tests/examples/wewill/home/WeWillHomePage.spec.ts'],
  businessCheckpoints: [
    'home promise heading is visible',
    'primary CTA wording is visible',
    'methodology trust section is visible',
    'bilingual CTA is visible',
    'footer value statement is visible',
  ],
};

export type ProtectExampleHomeTrustFlow = typeof protectExampleHomeTrustFlow;
