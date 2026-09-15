import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function ReportsDashboardPage() {
  // TODO: Psychologists only see data of their own patients; secretary has limited access
  // TODO: Reports aggregate patient, session and appointment data
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Laudos"
      description="Laudos e relatórios da clínica"
      actions={
        can('create', 'report') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Novo laudo
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
