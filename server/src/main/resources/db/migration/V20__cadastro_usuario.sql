-- Tabela de usuarios do sistema (profissionais e pessoal administrativo).
-- Faixa V20-V39 reservada para cadastro e prontuarios, conforme proposto no PR #13.

CREATE TABLE usuario (
    id_usuario         INT           NOT NULL AUTO_INCREMENT,
    nome               VARCHAR(150)  NOT NULL,
    email              VARCHAR(255)  NOT NULL,
    senha_hash         VARCHAR(100)  NOT NULL,
    perfil             VARCHAR(20)   NOT NULL,
    tipo_profissional  VARCHAR(20)   NULL,
    data_fim_registro  DATE          NULL,
    ativo              BOOLEAN       NOT NULL DEFAULT TRUE,
    data_criacao       DATETIME      NOT NULL,
    data_atualizacao   DATETIME      NULL,

    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuario_email UNIQUE (email),

    -- Os tres perfis sao os mesmos de client/src/types/permissions.ts.
    -- Aqui ficam em maiusculo (nome da constante Java); o formato que o front
    -- consome e definido no proprio enum, em Perfil.java.
    CONSTRAINT ck_usuario_perfil
        CHECK (perfil IN ('ADMIN', 'PSYCHOLOGIST', 'SECRETARY')),

    -- Nulo para quem nao e profissional de psicologia (ex: secretaria).
    CONSTRAINT ck_usuario_tipo_profissional
        CHECK (tipo_profissional IS NULL
               OR tipo_profissional IN ('STUDENT_INTERN', 'RECENT_GRADUATE', 'STAFF'))
);

-- A tela de gestao de usuarios lista filtrando por situacao e perfil.
CREATE INDEX ix_usuario_ativo_perfil ON usuario (ativo, perfil);
