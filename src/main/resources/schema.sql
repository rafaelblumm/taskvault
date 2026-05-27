-- Tabela de usuários
CREATE TABLE IF NOT EXISTS taskvault.user (
  id varchar(35) NOT NULL,
  name varchar(150) NOT NULL,
  email varchar(254) NOT NULL,
  role enum('GUEST', 'USER', 'ADMIN', 'SYSADMIN') NOT NULL DEFAULT 'GUEST',
  password varchar(180) NOT NULL,
  deleted_at datetime DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY user_email_unique (email)
);

-- Tabela de tarefas
CREATE TABLE IF NOT EXISTS taskvault.task (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  title tinytext NOT NULL,
  description text,
  status enum('pending','in_progress','done') NOT NULL DEFAULT 'pending',
  creation_datetime datetime NOT NULL,
  due_date date DEFAULT NULL,
  creator_id varchar(35) NOT NULL,
  assignee_id varchar(35) DEFAULT NULL,
  deleted_at datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY task_user_creator_FK (creator_id),
  KEY task_user_assignee_FK (assignee_id),
  CONSTRAINT task_user_assignee_FK FOREIGN KEY (assignee_id) REFERENCES user (id),
  CONSTRAINT task_user_creator_FK FOREIGN KEY (creator_id) REFERENCES user (id)
);

-- Tabela de comentários em tarefas
CREATE TABLE IF NOT EXISTS taskvault.comment (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  task_id bigint unsigned NOT NULL,
  creator_id varchar(35) NOT NULL,
  message varchar(2000) NOT NULL,
  creation_datetime datetime NOT NULL,
  deleted_at datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY comment_task_FK (task_id),
  KEY comment_user_FK (creator_id),
  CONSTRAINT comment_task_FK FOREIGN KEY (task_id) REFERENCES task (id),
  CONSTRAINT comment_user_FK FOREIGN KEY (creator_id) REFERENCES user (id)
);

-- Cria usuário inicial da aplicação
INSERT IGNORE INTO taskvault.`user` (id, name, email, role, password)
VALUES (
  'sudo',
	'Super User',
	'',
	'SYSADMIN',
	'$argon2id$v=19$m=16384,t=2,p=1$mEP/6VDYws4iQb5P9UwNSPukkBlVEZz87LwJm9N4ZZA$BcNWaZ9PIEVL2k0PKtaP1XGabViAgfRr/3ir5etv/wOIaZ0s42X6m+2/W629ZqQogpSyuj9796RjaeUS3SllEjBIwEKr/lFW'
);
