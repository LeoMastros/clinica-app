import type { ReactNode } from 'react';

import { AuthPageLayout } from '../../../shared/templates/AuthPageLayout';

export function AuthCard({
  title,
  subtitle,
  children,
  footer,
}: {
  title: string;
  subtitle?: string;
  children: ReactNode;
  footer?: ReactNode;
}) {
  return (
    <AuthPageLayout title={title} subtitle={subtitle} footer={footer}>
      {children}
    </AuthPageLayout>
  );
}
