import { expect, test, type APIRequestContext, type Page } from '@playwright/test';
import { randomUUID } from 'node:crypto';

const apiUrl = 'http://localhost:8080/api';
const password = 'Password123!';

async function registerUser(request: APIRequestContext) {
  const username = `qa${randomUUID().replace(/-/g, '').slice(0, 12)}`;
  const response = await request.post(`${apiUrl}/auth/register`, {
    data: {
      username,
      email: `${username}@example.com`,
      password,
    },
  });

  expect(response.status()).toBe(201);
  return response.json() as Promise<{
    token: string;
    userId: string;
    username: string;
    email: string;
  }>;
}

async function openTodosForUser(page: Page, request: APIRequestContext) {
  const user = await registerUser(request);
  await page.addInitScript((storedUser) => {
    localStorage.setItem('token', storedUser.token);
    localStorage.setItem(
      'user',
      JSON.stringify({
        id: storedUser.userId,
        username: storedUser.username,
        email: storedUser.email,
      }),
    );
  }, user);
  await page.goto('/');
}

test('creates a task and shows it in the list', async ({ page, request }) => {
  await openTodosForUser(page, request);
  const title = `Create task ${randomUUID()}`;

  await page.getByRole('textbox').fill(title);
  await page.getByRole('button', { name: 'Add Todo' }).click();

  await expect(page.getByText(title, { exact: true })).toBeVisible();
});

test('marks a task as done with a visible completed style', async ({ page, request }) => {
  await openTodosForUser(page, request);
  const title = `Complete task ${randomUUID()}`;

  await page.getByRole('textbox').fill(title);
  await page.getByRole('button', { name: 'Add Todo' }).click();
  const todo = page.getByRole('listitem').filter({ hasText: title });

  await todo.getByRole('button', { name: `Mark ${title} as done` }).click();

  await expect(todo).toHaveClass(/done/);
  await expect(todo.getByRole('button', { name: `Mark ${title} as not done` })).toHaveAttribute(
    'aria-pressed',
    'true',
  );
});

test('deletes a task and removes it from the list', async ({ page, request }) => {
  await openTodosForUser(page, request);
  const title = `Delete task ${randomUUID()}`;

  await page.getByRole('textbox').fill(title);
  await page.getByRole('button', { name: 'Add Todo' }).click();
  await page.getByRole('button', { name: `Delete ${title}` }).click();

  await expect(page.getByText(title, { exact: true })).toHaveCount(0);
});

test('logs in with valid credentials', async ({ page, request }) => {
  const user = await registerUser(request);

  await page.goto('/login');
  await page.getByLabel('Username or email').fill(user.username);
  await page.getByLabel('Password').fill(password);
  await page.getByRole('button', { name: 'Sign In' }).click();

  await expect(page.getByRole('heading', { name: 'Done It' })).toBeVisible();
});

test('shows an error for invalid login credentials', async ({ page }) => {
  await page.goto('/login');
  await page.getByLabel('Username or email').fill(`missing-${randomUUID()}`);
  await page.getByLabel('Password').fill(password);
  await page.getByRole('button', { name: 'Sign In' }).click();

  await expect(page.getByText('Invalid credentials', { exact: true })).toBeVisible();
});
