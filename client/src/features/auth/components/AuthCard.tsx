import type { ReactNode } from 'react';

import { AuthPageLayout } from '../../../shared/templates/AuthPageLayout';

export function AuthCard({
  title,
  subtitle,
  children,
}: {
  title: string;
  subtitle?: string;
  children: ReactNode;
}) {
  return (
    <AuthPageLayout title={title} subtitle={subtitle}>
      {children}
    </AuthPageLayout>
  );
}
