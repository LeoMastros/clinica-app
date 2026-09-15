import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function SchedulerPage() {
  // TODO: Render CustomScheduler (react-day-picker) with expanded recurrences from expandRecurrence()
  // TODO: Only admin and secretary can create/edit/cancel; psychologists see a read-only agenda
  // TODO: Fetch appointments with useAppointments() (React Query) filtered by the visible range
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Agendamentos"
      description="Agenda da clínica com recorrência personalizada"
      actions={
        can('create', 'appointment') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Novo agendamento
          </Button>
        ) : undefined
      }
    >
      <Alert severity="info">
        Módulo em construção: estrutura e permissões prontas, regras de negócio
        pendentes.
      </Alert>
    </PageLayout>
  );
}
