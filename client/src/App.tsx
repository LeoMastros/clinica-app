import { AppRouter } from './router';
import { AuthProvider } from './shared/providers/AuthProvider';
import { PermissionsProvider } from './shared/providers/PermissionsProvider';
import { QueryProvider } from './shared/providers/QueryProvider';
import { ThemeProvider } from './shared/providers/ThemeProvider';
import { ErrorBoundary } from './shared/templates/ErrorBoundary';

export default function App() {
  return (
    <ErrorBoundary>
      <ThemeProvider>
        <QueryProvider>
          <AuthProvider>
            <PermissionsProvider>
              <AppRouter />
            </PermissionsProvider>
          </AuthProvider>
        </QueryProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}
