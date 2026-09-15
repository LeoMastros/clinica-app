import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function AnamnesisListPage() {
  // TODO: List anamnesis records for the assigned patients only
  // TODO: Only psychologists (and admin) can create or edit anamnesis records
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Anamnese"
      description="Histórico e avaliação dos pacientes"
      actions={
        can('create', 'anamnesis') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Nova anamnese
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
