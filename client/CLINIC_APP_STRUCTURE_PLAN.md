---
agent: devin-local
session: jealous-comma
created: 2026-09-14T18:52:52Z
---
# Clinic App Folder Structure Plan

Create comprehensive folder structure for Vite + Material UI clinic app with FULLY FUNCTIONAL theme/layout/routing/centralized permissions, React Query + Context for state management, E2E client-side encryption (elliptic curve + enveloping, admin-only key generation), custom appointment recurrence using react-day-picker, hybrid appointment-session relationship, and EMPTY FUNCTION STUBS with domain-specific TODO comments for all other modules.

## Complete Folder Structure

```
src/
├── main.tsx                          # Entry point (keep existing)
├── index.css                         # Global CSS reset (minimal)
├── App.tsx                           # Root component with routing (refactor)
│
├── theme/                            # Theme configuration (FULLY FUNCTIONAL)
│   ├── index.ts                      # Main theme export
│   ├── colors.ts                     # Color palette
│   ├── typography.ts                 # Typography settings
│   ├── spacing.ts                    # Spacing scale
│   ├── breakpoints.ts                # Breakpoint configuration
│   ├── shadows.ts                    # Shadow definitions
│   ├── accessibility.ts               # Accessibility settings
│   └── components.ts                 # Component-specific theme overrides
│
├── layout/                           # Layout components (FULLY FUNCTIONAL)
│   ├── MainLayout.tsx                # Main layout with responsive sidebar
│   ├── AuthLayout.tsx                # Authentication layout (login/signup)
│   ├── AppBar.tsx                    # Top navigation bar with hamburger menu on mobile
│   ├── Sidebar.tsx                   # Side navigation with role-based menu items
│   ├── MobileDrawer.tsx              # Mobile drawer for sidebar (hamburger menu)
│   ├── Footer.tsx                    # Footer component
│   └── index.ts                      # Layout exports
│
├── core/                             # Core functionality
│   ├── crypto/                       # E2E Client-side encryption (elliptic curve + enveloping)
│   │   ├── index.ts                  # Crypto module export
│   │   ├── encryption.ts             # Encryption functions (elliptic curve)
│   │   ├── decryption.ts             # Decryption functions (enveloping)
│   │   ├── keyManagement.ts          # Key management (admin only for key generation)
│   │   ├── cryptoUtils.ts            # Crypto utilities
│   │   └── types.ts                  # Crypto types
│   ├── api/                          # API client
│   │   ├── index.ts                  # API client export
│   │   ├── client.ts                 # Axios client configuration
│   │   ├── interceptors.ts           # Request/response interceptors
│   │   ├── endpoints.ts              # API endpoints
│   │   └── types.ts                  # API types
│   ├── auth/                         # Authentication
│   │   ├── index.ts                  # Auth module export
│   │   ├── service.ts                # Auth service (email/password + Google OAuth)
│   │   ├── context.tsx               # Auth context
│   │   ├── hooks.ts                  # Auth hooks
│   │   └── types.ts                  # Auth types
│   ├── permissions/                  # CENTRALIZED PERMISSIONS SYSTEM (FULLY FUNCTIONAL)
│   │   ├── index.ts                  # Permissions export
│   │   ├── permissions.ts            # Permission checking functions
│   │   ├── roles.ts                  # Role definitions
│   │   ├── accessControl.ts          # Access control logic
│   │   └── types.ts                  # Permission types
│   ├── businessRules/                # ABSTRACTED BUSINESS RULES (testable)
│   │   ├── index.ts                  # Business rules export
│   │   ├── appointmentRules.ts       # Appointment recurrence rules
│   │   ├── sessionRules.ts           # Session-appointment dependency rules
│   │   ├── patientRules.ts           # Patient-psychologist assignment rules
│   │   ├── registrationRules.ts      # Professional registration rules
│   │   └── types.ts                  # Business rule types
│   └── utils/                        # Shared utilities
│       ├── index.ts                  # Utils export
│       ├── formatters.ts             # Data formatters (CPF, dates, etc.)
│       ├── validators.ts             # Input validators
│       ├── dateCalculators.ts        # Date calculations (semester ends, year ends)
│       ├── constants.ts              # App constants
│       └── helpers.ts                # Helper functions
│
├── features/                         # Feature modules
│   ├── auth/                         # Authentication feature
│   │   ├── pages/
│   │   │   ├── LoginPage.tsx         # Login page (email/password + Google OAuth)
│   │   │   └── SignupPage.tsx        # Signup page
│   │   ├── components/
│   │   │   ├── LoginForm.tsx         # Login form component
│   │   │   ├── SignupForm.tsx        # Signup form component
│   │   │   ├── GoogleLoginButton.tsx # Google OAuth button
│   │   │   └── AuthCard.tsx          # Auth card wrapper
│   │   ├── hooks/
│   │   │   ├── useLogin.ts           # Login hook
│   │   │   ├── useSignup.ts          # Signup hook
│   │   │   └── useGoogleAuth.ts      # Google OAuth hook
│   │   ├── services/
│   │   │   └── authService.ts        # Auth API service
│   │   └── index.ts                  # Auth feature export
│   │
│   ├── users/                        # Users management (admin only - users are professionals, no delete)
│   │   ├── pages/
│   │   │   └── UserListPage.tsx      # List all users (professionals)
│   │   ├── components/
│   │   │   ├── UserTable.tsx         # Users table with edit and reset password buttons (no delete)
│   │   │   ├── UserModal.tsx         # Create/edit user modal
│   │   │   ├── UserForm.tsx          # User form content with role selection
│   │   │   ├── ResetPasswordModal.tsx # Reset password modal
│   │   │   ├── RoleSelector.tsx      # Role selection component
│   │   │   └── UserFilter.tsx        # User filter controls
│   │   ├── hooks/
│   │   │   ├── useUsers.ts           # Users hook
│   │   │   ├── useUserMutation.ts    # User mutation hook (create/edit only)
│   │   │   └── usePermissions.ts     # Permission checking hook
│   │   ├── services/
│   │   │   └── userService.ts        # Users API service (no delete endpoints)
│   │   └── index.ts                  # Users feature export
│   │
│   ├── patients/                     # Patient management (no delete)
│   │   ├── pages/
│   │   │   ├── PatientListPage.tsx   # List patients
│   │   │   ├── PatientCreatePage.tsx # Register new patient
│   │   │   ├── PatientDetailPage.tsx # Patient details
│   │   │   └── PatientEditPage.tsx   # Edit patient
│   │   ├── components/
│   │   │   ├── PatientTable.tsx      # Patients table (no delete buttons)
│   │   │   ├── PatientForm.tsx       # Patient registration form
│   │   │   ├── PatientCard.tsx       # Patient card
│   │   │   ├── PatientSearch.tsx     # Patient search
│   │   │   ├── PatientFilter.tsx     # Patient filter controls
│   │   │   ├── PsychologistSelector.tsx # Psychologist assignment (one per patient)
│   │   │   └── StatusEditor.tsx      # Status editor (secretary access)
│   │   ├── hooks/
│   │   │   ├── usePatients.ts        # Patients hook
│   │   │   ├── usePatientMutation.ts # Patient mutation hook (create/edit only)
│   │   │   └── usePatientPsychologist.ts # Patient-psychologist relationship hook
│   │   ├── services/
│   │   │   └── patientService.ts     # Patients API service (no delete endpoints)
│   │   └── index.ts                  # Patients feature export
│   │
│   ├── appointments/                 # Appointments and scheduling (admin and secretary only, custom recurrence)
│   │   ├── pages/
│   │   │   └── SchedulerPage.tsx           # Custom scheduler view with recurrence support
│   │   ├── components/
│   │   │   ├── CustomScheduler.tsx          # Custom scheduler (react-day-picker + recurrence)
│   │   │   ├── AppointmentModal.tsx      # Create/Edit appointment modal
│   │   │   ├── AppointmentForm.tsx        # Appointment form content
│   │   │   ├── RecurrenceConfig.tsx       # Recurrence configuration component
│   │   │   ├── AppointmentDetails.tsx    # Appointment details view
│   │   │   ├── TimeSlotPicker.tsx         # Time slot selection
│   │   │   ├── LocationSelector.tsx      # Location selection (Apasem/Clínica)
│   │   │   └── CancellationDialog.tsx     # Cancellation with reason (in-person)
│   │   ├── hooks/
│   │   │   ├── useAppointments.ts         # Appointments hook (React Query)
│   │   │   ├── useRecurrence.ts           # Recurrence logic hook
│   │   │   └── useAppointmentMutation.ts  # Appointment mutation hook (React Query)
│   │   ├── services/
│   │   │   └── appointmentService.ts      # Appointments API service
│   │   └── index.ts                       # Appointments feature export
│   │
│   ├── sessions/                     # Session management (hybrid relationship with appointments)
│   │   ├── pages/
│   │   │   ├── SessionListPage.tsx        # List sessions for patient
│   │   │   ├── SessionCreatePage.tsx      # Create new session (with/without appointment)
│   │   │   └── SessionDetailPage.tsx       # Session details
│   │   ├── components/
│   │   │   ├── SessionTable.tsx           # Sessions table
│   │   │   ├── SessionForm.tsx            # Session form
│   │   │   ├── SessionCard.tsx            # Session card
│   │   │   ├── SessionNotes.tsx           # Session notes component
│   │   │   ├── SessionCancelDialog.tsx    # Cancel session dialog (psychologist only)
│   │   │   └── AppointmentLink.tsx        # Link session to existing appointment
│   │   ├── hooks/
│   │   │   ├── useSessions.ts             # Sessions hook (React Query)
│   │   │   ├── useSessionDependency.ts    # Session-appointment dependency hook
│   │   │   └── useSessionMutation.ts      # Session mutation hook (React Query, create/edit/cancel, no delete)
│   │   ├── services/
│   │   │   └── sessionService.ts          # Sessions API service (no delete endpoints)
│   │   └── index.ts                       # Sessions feature export
│   │
│   ├── anamnesis/                    # Patient anamnesis and forms (psychologist only, no delete)
│   │   ├── pages/
│   │   │   ├── AnamnesisListPage.tsx      # List anamnesis records
│   │   │   ├── AnamnesisCreatePage.tsx    # Create anamnesis
│   │   │   └── AnamnesisDetailPage.tsx    # Anamnesis details
│   │   ├── components/
│   │   │   ├── AnamnesisForm.tsx          # Anamnesis form
│   │   │   ├── AnamnesisCard.tsx          # Anamnesis card
│   │   │   └── AnamnesisTemplate.tsx      # Anamnesis template
│   │   ├── hooks/
│   │   │   ├── useAnamnesis.ts            # Anamnesis hook
│   │   │   └── useAnamnesisMutation.ts   # Anamnesis mutation hook (create/edit only)
│   │   ├── services/
│   │   │   └── anamnesisService.ts         # Anamnesis API service (no delete endpoints)
│   │   └── index.ts                       # Anamnesis feature export
│   │
│   ├── triage/                       # Patient triage (psychologist only, no delete)
│   │   ├── pages/
│   │   │   ├── TriageListPage.tsx         # List triage records
│   │   │   ├── TriageCreatePage.tsx       # Create triage
│   │   │   └── TriageDetailPage.tsx       # Triage details
│   │   ├── components/
│   │   │   ├── TriageForm.tsx             # Triage form
│   │   │   ├── TriageCard.tsx             # Triage card
│   │   │   └── TriageAssessment.tsx       # Triage assessment
│   │   ├── hooks/
│   │   │   ├── useTriage.ts               # Triage hook
│   │   │   └── useTriageMutation.ts      # Triage mutation hook (create/edit only)
│   │   ├── services/
│   │   │   └── triageService.ts           # Triage API service (no delete endpoints)
│   │   └── index.ts                       # Triage feature export
│   │
│   └── reports/                      # Reports and analytics
│   │   ├── pages/
│   │   │   ├── ReportsDashboardPage.tsx   # Reports dashboard
│   │   │   ├── PatientReportsPage.tsx    # Patient reports
│   │   │   └── SessionReportsPage.tsx    # Session reports
│   │   ├── components/
│   │   │   ├── ReportCard.tsx             # Report card
│   │   │   ├── ReportChart.tsx            # Report chart
│   │   │   └── ReportFilters.tsx          # Report filters
│   │   ├── hooks/
│   │   │   ├── useReports.ts              # Reports hook
│   │   │   └── useReportGeneration.ts     # Report generation hook
│   │   ├── services/
│   │   │   └── reportService.ts           # Reports API service
│   │   └── index.ts                       # Reports feature export
│
├── shared/                           # Shared components (Atomic Design - starting from molecules)
│   ├── molecules/                    # Combinations of Material UI components
│   │   ├── FormField.tsx             # Form field with label
│   │   ├── SearchBar.tsx             # Search bar component
│   │   ├── Pagination.tsx            # Pagination component
│   │   ├── Breadcrumb.tsx            # Breadcrumb component
│   │   ├── Notification.tsx          # Notification component
│   │   ├── ConfirmDialog.tsx         # Confirmation dialog
│   │   ├── FilterPanel.tsx           # Filter panel
│   │   ├── ActionButtons.tsx         # Action button group
│   │   ├── StatusBadge.tsx           # Status badge with icon
│   │   └── index.ts                  # Molecules export
│   │
│   ├── organisms/                    # Complex components
│   │   ├── DataTable.tsx             # Data table with sorting/filtering
│   │   ├── FormWizard.tsx            # Multi-step form
│   │   ├── CardGrid.tsx              # Grid of cards
│   │   ├── SidebarMenu.tsx           # Sidebar navigation
│   │   ├── TopNavigation.tsx         # Top navigation bar
│   │   ├── UserProfile.tsx           # User profile component
│   │   └── index.ts                  # Organisms export
│   │
│   ├── templates/                    # Page-level components
│   │   ├── PageLayout.tsx            # Standard page layout
│   │   ├── AuthPageLayout.tsx        # Authentication page layout
│   │   ├── ErrorPage.tsx             # Error page template
│   │   ├── ErrorBoundary.tsx         # Error boundary component (FULLY FUNCTIONAL)
│   │   └── index.ts                  # Templates export
│   │
│   └── providers/                    # Context providers
│       ├── ThemeProvider.tsx         # Material UI theme provider (FULLY FUNCTIONAL)
│       ├── QueryProvider.tsx         # React Query provider (FULLY FUNCTIONAL)
│       ├── CryptoProvider.tsx        # Cryptography provider
│       ├── AuthProvider.tsx          # Authentication provider (mock initially, FULLY FUNCTIONAL)
│       ├── PermissionsProvider.tsx   # Permissions provider (FULLY FUNCTIONAL)
│       └── index.ts                  # Providers export
│
├── router/                           # Routing configuration (FULLY FUNCTIONAL)
│   ├── index.tsx                     # Router setup
│   ├── routes.tsx                    # Route definitions with role-based access
│   ├── guards.tsx                    # Route guards (auth, role-based permissions)
│   └── navigation.ts                 # Navigation helpers
│
├── hooks/                            # Global custom hooks
│   ├── index.ts                      # Hooks export
│   ├── useResponsive.ts              # Responsive hook
│   ├── useDebounce.ts                # Debounce hook
│   ├── useLocalStorage.ts            # Local storage hook
│   ├── useBreakpoints.ts             # Breakpoints hook
│   ├── usePermissions.ts             # Permission checking hook (uses centralized permissions)
│   ├── useCrypto.ts                  # Crypto operations hook
│   └── useRoleAccess.ts              # Role-based access control hook
│
├── types/                            # Global TypeScript types (functional)
│   ├── index.ts                      # Types export
│   ├── user.ts                       # User types (users are professionals with roles)
│   ├── patient.ts                    # Patient types
│   ├── appointment.ts                # Appointment types (including recurrence)
│   ├── session.ts                    # Session types
│   ├── anamnesis.ts                  # Anamnesis types
│   ├── triage.ts                     # Triage types
│   ├── report.ts                     # Report types
│   ├── permissions.ts                # Permission types
│   ├── crypto.ts                     # Crypto types
│   ├── api.ts                        # API response types
│   └── common.ts                     // Common types
│
├── constants/                       # Application constants (functional)
│   ├── index.ts                      # Constants export
│   ├── routes.ts                     # Route constants
│   ├── api.ts                        # API constants
│   ├── roles.ts                      # User roles (admin, psychologist, secretary)
│   ├── crypto.ts                     # Crypto constants
│   ├── businessRules.ts              # Business rules (locations, registration rules, etc.)
│   └── validation.ts                # Validation patterns
│
└── assets/                           # Static assets
    ├── images/                       # Images
    ├── icons/                        # Custom icons
    └── fonts/                        # Custom fonts
```

## Implementation Plan

### Phase 1: Core Structure Setup
1. Create the complete folder structure
2. Set up theme configuration files (FULLY FUNCTIONAL)
3. Create centralized permissions system (FULLY FUNCTIONAL)
4. Create React Query provider (FULLY FUNCTIONAL)
5. Create mock authentication provider (FULLY FUNCTIONAL with default admin user)
6. Create ErrorBoundary component (FULLY FUNCTIONAL)
7. Create E2E crypto module with empty function stubs and implementation comments
8. Set up API client configuration with empty function stubs and implementation comments
9. Create abstracted business rules with empty function stubs and implementation comments
10. Install additional dependencies: react-hook-form, @hookform/resolvers, date-fns, crypto libraries, @tanstack/react-query, react-day-picker

### Phase 2: Shared Components (Atomic Design)
1. Define interface contracts for shared organisms (DataTable, FormWizard) BEFORE feature work begins
2. Create molecule components with empty function stubs and implementation comments
3. Create organism components with empty function stubs and implementation comments
4. Create template components with empty function stubs and implementation comments
5. Set up context providers (Theme, Query, Permissions, Auth - FULLY FUNCTIONAL, others empty stubs)
6. Create React Hook Form integration components with empty function stubs and implementation comments

### Phase 3: Feature Modules (EMPTY FUNCTION STUBS with implementation guidance)
1. Create authentication feature with empty function stubs and implementation comments
2. Create users management feature with empty function stubs and implementation comments
3. Create patients management feature with empty function stubs and implementation comments
4. Create appointments feature with custom recurrence system (empty function stubs with implementation comments)
5. Create sessions feature with appointment dependency logic (empty function stubs with implementation comments)
6. Create anamnesis feature with empty function stubs and implementation comments
7. Create triage feature with empty function stubs and implementation comments
8. Create reports feature with empty function stubs and implementation comments

### Phase 4: Layout and Routing (FULLY FUNCTIONAL)
1. Create layout components (MainLayout with responsive sidebar, AuthLayout)
2. Implement responsive sidebar with hamburger menu for mobile
3. Set up routing configuration with role-based route access (using centralized permissions)
4. Implement route guards (auth, role-based permissions using centralized system)
5. Connect navigation to layout (role-based menu items using centralized permissions)
6. Set up React Context for global state management

### Phase 5: Integration
1. Update App.tsx with routing and providers (FULLY FUNCTIONAL)
2. Update main.tsx if needed (FULLY FUNCTIONAL)
3. Ensure responsiveness across layout components
4. Verify accessibility compliance in layout components

## File Contents Strategy

### Fully Functional Files (Theme, Layout, Routing, Centralized Permissions, React Query, Mock Auth, ErrorBoundary)
- Complete, working implementation
- No TODO comments
- Ready for immediate use
- Material UI integration
- Responsive design
- Accessibility compliance
- Type-safe with TypeScript
- Centralized permission checking functions
- Role-based access control logic
- React Query setup for server state management
- Mock authentication with default admin user for testing
- Error boundary for React error handling

### TODO-Guided Boilerplate Files (All other modules)
Each boilerplate file will include:
- **File header comment block**: Main purpose and implementation logic
- TypeScript interface/type definitions
- **Empty function stubs only** - NO functional code
- **Domain-specific function comments**: Step-by-step implementation logic with business rule references
- Material UI component structure (JSX only, no logic)
- Business logic guidance with rule references
- API integration placeholders (empty functions with React Query patterns)
- Form validation structure (empty functions)
- Permission check placeholders (using centralized permission functions)
- Responsive design considerations (comments only)
- Accessibility attributes (JSX attributes only)
- Error handling structure (empty functions)
- Data flow guidance (comments only)
- Crypto integration placeholders (encrypt/decrypt calls)
- Field-level permission guidance where applicable

### Domain-Specific TODO Examples:

**Session Cancellation (with business rules):**
```typescript
export function cancelSession(sessionId: string): void {
  // TODO: Verify currentUser.role === 'psychologist' AND session.psychologistId === currentUser.id
  //       (Business Rule: Psychologists can only cancel their own sessions - see Role-Based Permission Matrix)
  // TODO: Check if session.status !== 'completed' (past sessions cannot be cancelled)
  //       (Business Rule: Past sessions remain immutable - see Session-Appointment Dependency Rules)
  // TODO: For in-person sessions, require cancellationReason parameter
  //       (Business Rule: In-person sessions require cancellation reason - see Appointment and Session Rules)
  // TODO: For remote/phone sessions, cancellationReason is optional
  //       (Business Rule: Other session types do not require cancellation reason)
  // TODO: Encrypt session notes before API call using crypto.encrypt()
  //       (Crypto Rule: Everything except IDs and dates must be encrypted)
  // TODO: Call sessionService.cancelSession() with React Query mutation
  // TODO: Update session status to 'cancelled' in cache
  // NO FUNCTIONAL CODE - Implementation guide only
}
```

**Patient Psychologist Assignment (with field-level permissions):**
```typescript
export function assignPsychologist(patientId: string, psychologistId: string): void {
  // TODO: Check centralized permission: canAssignPsychologist(currentUser.role)
  //       (Business Rule: Only admin can assign psychologists - see User and Role Management)
  // TODO: Verify psychologist doesn't exceed patient capacity (if applicable)
  //       (Business Rule: One psychologist can have multiple patients - see Psychologist-Patient Relationship)
  // TODO: Verify patient is not already assigned to another psychologist
  //       (Business Rule: One patient can be linked to only one psychologist)
  // TODO: Encrypt patient personal data before API call
  //       (Crypto Rule: Everything except IDs and dates must be encrypted)
  // TODO: Call patientService.updatePatient() with React Query mutation
  // TODO: Invalidate patient queries cache
  // NO FUNCTIONAL CODE - Implementation guide only
}
```

**Field-Level Permission in Form:**
```typescript
export function PatientForm({ patient, mode }: PatientFormProps) {
  // TODO: Implement canEditPsychologistField using centralized permissions
  //       (Business Rule: Secretary can edit patient status but NOT psychologist assignment)
  // TODO: If mode === 'edit' and currentUser.role === 'psychologist', disable all fields
  //       (Business Rule: Psychologists can only view assigned patients, no CRUD - see Role Matrix)

  return (
    <form>
      {/* Regular fields - accessible to admin and secretary */}
      <TextField name="name" />
      <TextField name="cpf" />
      
      {/* Field-level permission check */}
      {canEditPsychologistField(currentUser.role) && (
        <PsychologistSelector name="psychologistId" />
      )}
      
      {/* Secretary can edit status, psychologist cannot */}
      {canEditPatientStatus(currentUser.role) && (
        <StatusEditor name="status" />
      )}
    </form>
  );
}
```

## Business Rules

### User and Role Management
- **Admin responsibilities**: Registers both clients (patients) and professionals, only admin can generate new crypto keys
- **Appointment scheduling**: Admin and secretary can schedule appointments, psychologists cannot
- **Psychologist-patient relationship**: One patient can be linked to only one psychologist, but one psychologist can have multiple patients
- **Triage and Anamnesis**: Only psychologists can perform triage and create anamnesis records
- **Data deletion**: NO user can delete any data - deletion is forbidden system-wide
- **Cancellation permissions**: Secretary can cancel appointments/schedules, psychologist can cancel sessions

### Professional Registration Rules
- **Student intern**: Default end date is last day of June or November (end of semester)
- **Recent graduate**: Default end date is last day of current year (1 year validity)

### Appointment and Session Rules
- **Appointment recurrence**: Appointments can be recurring or non-recurring (custom implementation using react-day-picker)
- **Session-appointment relationship**: Hybrid approach - sessions can be created from appointments but can also exist independently (walk-in sessions)
- **Appointment changes**: If appointment changes, linked session view changes but past sessions remain unchanged
- **Session management**: Past sessions remain immutable, future/present sessions can happen or be cancelled
- **In-person locations**: Two locations available - Apasem and Clínica
- **Cancellation requirements**:
  - In-person sessions require cancellation reason
  - Other session types (remote, etc.) do not require cancellation reason
- **Session types**: In-person (Apasem/Clínica), remote, phone, etc.
- **Session states**: scheduled → confirmed → completed → cancelled (defined in types/session.ts)

### Cryptography Rules
- **E2E encryption**: True client-side end-to-end encryption
- **Algorithm**: Elliptic curve cryptography + enveloping
- **Key management**: Only admin can generate new keys
- **Encryption scope**: Everything encrypted/decrypted except IDs and dates
- **Key distribution**: Admin manages key distribution for content access
- **Security note**: Key management errors are irreversible due to no-deletion policy

### Patient Management
- **Secretary permissions**: Secretary can edit patient status but NOT psychologist assignment
- **Field-level permissions**: PsychologistSelector only accessible to admin, StatusEditor accessible to admin and secretary
- **Patient status**: Active, inactive, deceased, dropout (desistente)
- **LGPD compliance**: No-deletion policy based on CRP medical record retention regulations (typically 20+ years)

### Additional Features
- **Sessions**: Track individual therapy sessions with notes, interventions, homework
- **Anamnesis**: Patient history and assessment forms
- **Triage**: Initial patient assessment and classification
- **Reports**: Analytics and reporting for patients, sessions, and clinic operations

## Role-Based Permission Matrix

### Admin
- Full access to all features
- Crypto key management (generate new keys) - ADMIN ONLY
- User management (create, edit users, assign roles, reset passwords) - NO DELETE
- Patient management (create, edit, view, including psychologist assignment) - NO DELETE
- Appointment management (create, edit, view, cancel, configure recurrence using react-day-picker) - NO DELETE
- Session management (create, edit, view) - NO DELETE
- Anamnesis management (create, edit, view) - NO DELETE
- Triage management (create, edit, view) - NO DELETE
- Reports and analytics (full access)
- Can access all pages and perform all API requests

### Psychologist
- Patient management (view assigned patients only) - NO CRUD
- Appointment management (view only - cannot schedule/edit/cancel)
- Session management (create, edit, view, cancel for their patients' sessions) - NO DELETE
- Anamnesis management (create, edit, view for their patients) - NO DELETE
- Triage management (create, edit, view for their patients) - NO DELETE
- Reports (limited to their patients' data)
- No access to user management
- No access to crypto key management
- Cannot view or edit other professionals
- Can access patient (view only), session, anamnesis, triage, and limited reports pages

### Secretary
- Patient management (create, edit, view, can edit patient status) - NO DELETE
- Appointment management (create, edit, view, cancel, configure recurrence using react-day-picker) - NO DELETE
- Session management (view only) - NO CRUD
- Anamnesis management (no access)
- Triage management (no access)
- Reports (limited access)
- No access to user management
- No access to crypto key management
- Can access patient, appointment, and limited reports pages

## Architecture Decisions

### State Management Strategy
- **Server state**: React Query (@tanstack/react-query) for all API data (patients, appointments, sessions, etc.)
- **Client state**: React Context for auth, theme, permissions, crypto
- **Benefits**: Automatic caching, loading/error states, optimistic updates, cache invalidation
- **Implementation**: All feature hooks (usePatients, useAppointments, etc.) use React Query patterns

### Permission System Architecture
- **Single source of truth**: `core/permissions/permissions.ts` contains all permission logic
- **Centralized hook**: `hooks/usePermissions.ts` is the ONLY way to check permissions in the app
- **Usage pattern**: `const { can } = usePermissions(); can('action', 'resource')`
- **Implementation**: No permission logic in feature files - all use centralized functions
- **UI guards**: Use `can()` for page-level guards AND field-level permissions in forms

### Shared Component Contracts
- **Priority**: Shared organism interfaces (DataTable, FormWizard) must be defined BEFORE Phase 3
- **Contract freezing**: Once feature work begins, shared component signatures cannot change
- **Version control**: Major shared component changes require coordination across all feature squads

### API Contract Strategy
- **Backend coordination**: API contracts to be defined in separate OpenAPI/Postman spec
- **Mock strategy**: Use MSW (Mock Service Worker) for frontend development during backend development
- **Type safety**: Generate TypeScript types from OpenAPI spec when available

### Admin Recovery
- **Scope**: Admin recovery is backend responsibility, out of frontend scope
- **Frontend assumption**: Backend provides admin recovery mechanisms
- **UI consideration**: Frontend displays appropriate error messages if admin account is inaccessible

### Field-Level Permissions
- **Implementation**: Use centralized `can()` permission checks within form components
- **Example**: PsychologistSelector only rendered when `can('assign_psychologist', 'patient')`
- **Pattern**: Conditional rendering based on field-level permissions in shared forms

### Environment Configuration
- **Strategy**: Use Vite env variables for different environments (.env.development, .env.production)
- **Configuration**: API base URL, crypto config, feature flags configured per environment
- **Security**: Never commit sensitive data to repository

### i18n Considerations
- **Current scope**: All UI strings in PT-BR, no i18n library initially
- **Future-proof**: String constants centralized in feature-level constants files
- **Approach**: Add i18n library if multi-language support becomes requirement

## Key Design Decisions

1. **Feature-based organization**: Each business domain (patients, users as professionals, appointments, sessions, anamnesis, triage, reports) is self-contained
2. **Atomic Design (starting from molecules)**: Shared components start at molecule level, using Material UI components directly as atoms
3. **E2E encryption**: True client-side end-to-end encryption using elliptic curve + enveloping, only admin generates keys
4. **Centralized permissions**: Permission logic centralized in `core/permissions/` for testability and reusability
5. **Abstracted business rules**: Business rules abstracted in `core/businessRules/` for testability
6. **State management**: React Query for server state, React Context for client state
7. **Custom appointment recurrence**: Custom implementation using react-day-picker since MUI scheduler lacks recurrence support
8. **Session-appointment relationship**: Hybrid approach - sessions can be created from appointments or exist independently
9. **Role-based access control**: Three user roles (admin, psychologist, secretary) with different permissions for pages and API requests
10. **Admin exclusivity**: Only one admin user with full access to all features, user management, and crypto key generation
11. **Admin restriction**: RoleSelector must prevent creating additional admin users if one already exists
12. **Psychologist-patient relationship**: One patient linked to one psychologist, one psychologist can have multiple patients
13. **Professional registration rules**: Student interns (end of semester dates), recent graduates (1 year validity)
14. **No data deletion**: System-wide prohibition on data deletion based on CRP retention regulations
15. **Appointment management**: Admin and secretary can schedule/cancel appointments with recurrence using react-day-picker, psychologists cannot manage appointments
16. **Session management**: Psychologists can cancel their own sessions, secretary cannot manage sessions
17. **Location management**: Two in-person locations (Apasem, Clínica) with specific cancellation requirements
18. **Secretary permissions**: Can edit patient status but NOT psychologist assignment, schedule/cancel appointments with recurrence, limited patient CRUD (no delete)
19. **Psychologist permissions**: Can only view assigned patients (no CRUD), can manage their own sessions/anamnesis/triage
20. **Field-level permissions**: Form fields like PsychologistSelector use centralized permission checks for access control
21. **Responsive-first**: All components designed mobile-first with Material UI breakpoints
22. **Responsive navigation**: Left sidebar for desktop, hamburger menu with drawer for mobile
23. **Accessibility**: WCAG compliance built into component structure
24. **TypeScript**: Full type safety across the application
25. **Material UI**: Consistent Material Design 3 implementation, using MUI components directly without wrapper atoms
26. **Authentication**: Email/password login initially (Google OAuth deferred to future implementation)
27. **Mock auth**: Initial authentication uses mock admin user for testing, real auth implemented later
28. **React Hook Form**: Form handling with React Hook Form for performance and validation
29. **Users as professionals**: Users module manages professionals with modal-based CRUD (no delete) and reset password functionality
30. **Extended features**: Sessions, anamnesis, triage, and reports modules for comprehensive clinic management
31. **Crypto integration**: All sensitive data encrypted/decrypted except IDs and dates
32. **Error handling**: ErrorBoundary component wraps App for React error handling

## Files to Create/Modify

### Files to Create (FULLY FUNCTIONAL):
- All theme configuration files (theme/)
- All layout files (layout/MainLayout, layout/AuthLayout, layout/AppBar, layout/Sidebar, layout/MobileDrawer, layout/Footer)
- All routing files (router/)
- Centralized permissions system (core/permissions/)
- React Query provider (shared/providers/QueryProvider.tsx)
- Mock authentication provider (shared/providers/AuthProvider.tsx)
- Error boundary component (shared/templates/ErrorBoundary.tsx)
- Type definitions (types/) - functional type definitions
- Constants (constants/) - functional constant definitions
- Environment configuration (.env.example, vite config updates)

### Files to Create (EMPTY FUNCTION STUBS with implementation guidance):
- All core functionality files (core/crypto/, core/api/, core/auth/, core/businessRules/, core/utils/) - empty function stubs with detailed implementation comments
- All feature module files (features/auth, features/users, features/patients, features/appointments, features/sessions, features/anamnesis, features/triage, features/reports) - empty function stubs with detailed implementation comments
- All shared component files (shared/molecules, shared/organisms, shared/templates) - empty function stubs with detailed implementation comments
- All global hooks (hooks/) - empty function stubs with detailed implementation comments
- Shared providers (shared/providers/CryptoProvider.tsx, shared/providers/AuthProvider.tsx) - empty function stubs with detailed implementation comments

### Files to Modify (FULLY FUNCTIONAL):
- `src/App.tsx` - Refactor to use routing and providers
- `src/main.tsx` - Update if needed for providers
- `src/index.css` - Simplify to global reset only

### Dependencies to Install:
- `react-hook-form` - Form handling
- `@hookform/resolvers` - Form validation resolvers
- `zod` - Schema validation (recommended for React Hook Form)
- `date-fns` - Date manipulation for business rules (semester calculations, year ends)
- `@tanstack/react-query` - Server state management
- `react-day-picker` - Custom appointment recurrence calendar
- `tweetnacl` or `elliptic` - Elliptic curve cryptography
- `msw` - Mock Service Worker for API mocking during development
- Google OAuth library (deferred - email/password auth initially)

## Implementation Order and Coordination

### Phase Dependencies
1. **Phase 1 (Core)** must complete before Phase 3 (Features) for:
   - Centralized permissions system ready for all feature permission checks
   - React Query provider ready for all feature data fetching
   - Mock auth provider ready for routing guards to function
   - Shared component contracts (DataTable, FormWizard) must be frozen

2. **Contract Freezing Priority** (must happen before Phase 3):
   - `shared/organisms/DataTable` props interface
   - `shared/organisms/FormWizard` props interface
   - `shared/providers/` provider contexts
   - `core/permissions/` permission function signatures
   - `types/` shared type definitions

3. **Parallel Development Strategy**:
   - Once shared contracts are frozen, feature squads can work in parallel
   - API mocking with MSW allows frontend development without backend dependency
   - Regular contract reviews to prevent breaking changes

### Coordination Requirements
- **Shared component changes**: Require coordination meeting with all feature leads
- **Permission logic changes**: Update centralized system first, then notify all feature squads
- **Type definition changes**: Must maintain backward compatibility or coordinate breaking changes
- **API contract changes**: Coordinate with backend team, update MSW mocks, notify all feature squads

## Risks and Considerations

1. **Team coordination**: Clear module boundaries needed for parallel development
2. **Crypto implementation**: E2E encryption with elliptic curve requires security team review before any implementation
3. **React Query adoption**: Team must be familiar with React Query patterns - may require training
4. **Custom recurrence**: Building custom recurrence system with react-day-picker requires thorough testing
5. **Session-appointment model**: Hybrid relationship needs clear API contract and data modeling
6. **Shared component contracts**: Interface changes to DataTable/FormWizard break all features - require strict coordination
7. **Permission system consistency**: Risk of duplicate permission logic if centralized system not strictly enforced
8. **Field-level permissions**: Easy to forget field-level restrictions in forms - need code review focus
9. **Performance**: Consider code splitting for feature modules with React Query
10. **Testing**: Structure should support unit, integration, and E2E testing, especially for business rules
11. **Documentation**: Each module should include README for other teams
12. **Role-based access**: Centralized permission logic must be thoroughly tested to prevent unauthorized access
13. **Admin management**: RoleSelector must enforce single admin rule in UI
14. **Permission propagation**: Ensure centralized permissions are consistently enforced - no duplicate permission checks
15. **Business rule complexity**: Date calculations, psychologist-patient relationships, location rules, and session-appointment dependencies need thorough testing
16. **No-deletion policy**: System-wide prohibition of deletion requires careful UI design (no delete buttons) and API enforcement
17. **Cancellation permissions**: Secretary can cancel appointments, psychologist can cancel sessions - needs clear distinction in UI
18. **Psychologist patient access**: Psychologists can only view assigned patients - requires proper filtering and access control
19. **Feature scope**: Additional modules (sessions, anamnesis, triage, reports) increase complexity and development time
20. **Crypto key management**: Admin-only key generation requires secure key distribution mechanisms
21. **Encryption scope**: Encrypting everything except IDs and dates requires careful data modeling
22. **API contract dependency**: Frontend development blocked without clear API contracts - MSW mocking critical
23. **TODO comment quality**: Risk of generic TODO comments not conveying actual business rules - need code review enforcement
24. **Mock auth dependency**: If mock auth not realistic enough, routing guards may not be properly testable
25. **Environment configuration**: Crypto keys and sensitive configs must never be committed to repository
