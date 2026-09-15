import Alert from '@mui/material/Alert';

import { PageLayout } from '../../../shared/templates/PageLayout';

export function SessionCreatePage() {
  // TODO: Optionally link to an appointment via AppointmentLink
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  return (
    <PageLayout
      title="Nova sessão"
      description="Criação de sessão com ou sem agendamento"
    >
      <Alert severity="info">Módulo em construção.</Alert>
    </PageLayout>
  );
}
