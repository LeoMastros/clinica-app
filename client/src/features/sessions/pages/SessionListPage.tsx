import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function SessionListPage() {
  // TODO: Sessions may originate from an appointment or exist independently (walk-in)
  // TODO: Secretary sees sessions read-only; psychologists manage their own sessions
  // TODO: Past sessions are immutable
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Sessões"
      description="Sessões realizadas, agendadas e canceladas"
      actions={
        can('create', 'session') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Nova sessão
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
