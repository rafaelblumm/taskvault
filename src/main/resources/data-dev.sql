-- Cria usuário inicial da aplicação
MERGE IGNORE INTO app_user (id, name, email, role, password)
VALUES (
  'sudo',
	'Super User',
	'',
	'SYSADMIN',
	'$argon2id$v=19$m=16384,t=2,p=1$mEP/6VDYws4iQb5P9UwNSPukkBlVEZz87LwJm9N4ZZA$BcNWaZ9PIEVL2k0PKtaP1XGabViAgfRr/3ir5etv/wOIaZ0s42X6m+2/W629ZqQogpSyuj9796RjaeUS3SllEjBIwEKr/lFW'
);
