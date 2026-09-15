import { MemoryRouter } from 'react-router-dom';

import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';

import { can } from '../core/permissions';
import { PermissionsContext } from '../shared/providers/PermissionsProvider';
import { ThemeProvider } from '../shared/providers/ThemeProvider';
import type { Role } from '../types/permissions';
import { NAV_ITEMS, Sidebar } from './Sidebar';

function renderSidebarFor(role: Role) {
  return render(
    <MemoryRouter>
      <ThemeProvider>
        <PermissionsContext.Provider
          value={{ can: (action, resource) => can(role, action, resource) }}
        >
          <Sidebar />
        </PermissionsContext.Provider>
      </ThemeProvider>
    </MemoryRouter>
  );
}

function visibleLabels() {
  return screen.getAllByRole('link').map(link => link.textContent);
}

describe('Sidebar', () => {
  it('declares the planned items in order', () => {
    expect(NAV_ITEMS.map(item => item.label)).toEqual([
      'Agendamentos',
      'Triagem',
      'Anamnese',
      'Sessões',
      'Laudos',
      'Pacientes',
      'Gestão de Usuários',
    ]);
  });

  it('shows every item to the admin', () => {
    renderSidebarFor('admin');
    expect(visibleLabels()).toHaveLength(NAV_ITEMS.length);
    expect(screen.getByText('Gestão de Usuários')).toBeTruthy();
  });

  it('hides triage, anamnesis and user management from the secretary', () => {
    renderSidebarFor('secretary');
    expect(visibleLabels()).toEqual([
      'Agendamentos',
      'Sessões',
      'Laudos',
      'Pacientes',
    ]);
  });

  it('hides user management from the psychologist', () => {
    renderSidebarFor('psychologist');
    expect(visibleLabels()).toEqual([
      'Agendamentos',
      'Triagem',
      'Anamnese',
      'Sessões',
      'Laudos',
      'Pacientes',
    ]);
  });
});
