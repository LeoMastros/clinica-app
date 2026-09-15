import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function TriageListPage() {
  // TODO: List triage records for the patients the current psychologist is assigned to
  // TODO: Only psychologists (and admin) can create or edit triage records
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Triagem"
      description="Avaliação inicial e classificação de pacientes"
      actions={
        can('create', 'triage') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Nova triagem
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
