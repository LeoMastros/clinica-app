import Box from '@mui/material/Box';
import FormControlLabel from '@mui/material/FormControlLabel';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import Typography from '@mui/material/Typography';

import type { CreatableRole } from '../types';

export interface RoleSelectorProps {
  value: CreatableRole | null;
  onChange: (role: CreatableRole) => void;
}

const OPTIONS: { value: CreatableRole; label: string; description: string }[] =
  [
    {
      value: 'SECRETARY',
      label: 'Secretaria',
      description: 'Recepção, cadastro de pacientes e agendamentos.',
    },
    {
      value: 'PROFESSIONAL',
      label: 'Psicólogo(a)',
      description:
        'Atendimento clínico — a conta inicia inativa até aprovação.',
    },
  ];

/** Step 1 of the create-user modal: pick the account kind. */
export function RoleSelector({ value, onChange }: RoleSelectorProps) {
  return (
    <RadioGroup
      value={value ?? ''}
      onChange={event => onChange(event.target.value as CreatableRole)}
      aria-label="Tipo de usuário"
      sx={{ gap: 1.5, pt: 1 }}
    >
      {OPTIONS.map(option => (
        <Box
          key={option.value}
          sx={{
            border: 1,
            borderColor: value === option.value ? 'primary.main' : 'divider',
            borderRadius: 1,
            px: 1,
            bgcolor:
              value === option.value ? 'action.selected' : 'background.paper',
          }}
        >
          <FormControlLabel
            value={option.value}
            control={<Radio />}
            label={
              <Box>
                <Typography variant="subtitle2">{option.label}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {option.description}
                </Typography>
              </Box>
            }
            sx={{ width: '100%', m: 0, py: 1, alignItems: 'flex-start' }}
          />
        </Box>
      ))}
    </RadioGroup>
  );
}
