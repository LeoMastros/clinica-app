import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';

import type { UserFilters, UserTypeName } from '../types';

export interface UserFilterProps {
  filters: UserFilters;
  onChange: (filters: UserFilters) => void;
}

const TYPE_OPTIONS: { value: UserTypeName | ''; label: string }[] = [
  { value: '', label: 'Todos os perfis' },
  { value: 'COORDINATOR', label: 'Coordenador' },
  { value: 'SECRETARY', label: 'Secretaria' },
  { value: 'PROFESSIONAL', label: 'Psicólogo(a)' },
];

const STATUS_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'Todos os status' },
  { value: 'true', label: 'Ativos' },
  { value: 'false', label: 'Inativos' },
];

export function UserFilter({ filters, onChange }: UserFilterProps) {
  return (
    <Stack direction="row" spacing={2} sx={{ mb: 2 }}>
      <TextField
        select
        label="Perfil"
        size="small"
        sx={{ minWidth: 180 }}
        value={filters.type ?? ''}
        onChange={event =>
          onChange({
            ...filters,
            type: (event.target.value || undefined) as UserTypeName | undefined,
          })
        }
      >
        {TYPE_OPTIONS.map(option => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>

      <TextField
        select
        label="Status"
        size="small"
        sx={{ minWidth: 160 }}
        value={filters.active === undefined ? '' : String(filters.active)}
        onChange={event =>
          onChange({
            ...filters,
            active:
              event.target.value === ''
                ? undefined
                : event.target.value === 'true',
          })
        }
      >
        {STATUS_OPTIONS.map(option => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>
    </Stack>
  );
}
