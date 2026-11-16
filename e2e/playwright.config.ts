import { defineConfig } from '@playwright/test';
import path from 'node:path';

const rootDir = path.resolve(__dirname, '..');
const isWindows = process.platform === 'win32';
const mavenCommand = isWindows ? 'mvnw.cmd spring-boot:run' : 'bash ./mvnw spring-boot:run';

export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: 'http://localhost:3000',
    trace: 'retain-on-failure',
  },
  webServer: [
    {
      command: mavenCommand,
      cwd: rootDir,
      url: 'http://localhost:8080/actuator/health',
      timeout: 120_000,
      reuseExistingServer: !process.env.CI,
      env: {
        ...process.env,
        SPRING_DATA_MONGODB_URI:
          process.env.SPRING_DATA_MONGODB_URI ?? 'mongodb://127.0.0.1:27017/doneit',
        JWT_SECRET: 'test-secret-key-for-doneit-with-at-least-32-bytes',
        JWT_EXPIRATION: '86400000',
        IA_API_KEY: '',
      },
    },
    {
      command: 'npm start',
      cwd: path.join(rootDir, 'frontend'),
      url: 'http://localhost:3000',
      timeout: 120_000,
      reuseExistingServer: !process.env.CI,
      env: {
        ...process.env,
        HOST: '127.0.0.1',
        PORT: '3000',
        BROWSER: 'none',
        REACT_APP_API_URL: 'http://localhost:8080/api',
      },
    },
  ],
});
