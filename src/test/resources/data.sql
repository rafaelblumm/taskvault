-- Cria usuário para testes com senha integr4tionT&st
INSERT INTO app_user (id, name, email, role, password)
VALUES (
    'testuser',
    'Test Super User',
    'test@dev.com',
    'SYSADMIN',
    '$argon2id$v=19$m=16384,t=2,p=1$OhNlS4GCiwExPIkZSLBbx7yZgdoIXfSZpeJwu1EQDrM$rgMkwL55/pce1hmnZTeRpgUmVsO4sk49slYX+hr/CUrtiZGcDVBFQED3ekJqw5hfVwbYQIxPm+RLbehEa69KZyBaYiNvKaVF'
);
