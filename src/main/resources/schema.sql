-- Tabela de usuários
CREATE TABLE IF NOT EXISTS app_user (
  id VARCHAR(35) NOT NULL,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(254) NOT NULL UNIQUE,
  role ENUM('GUEST', 'USER', 'ADMIN', 'SYSADMIN') NOT NULL DEFAULT 'GUEST',
  password VARCHAR(180) NOT NULL,
  deleted_at TIMESTAMP DEFAULT NULL,
  PRIMARY KEY (id)
);

-- Tabela de tarefas
CREATE TABLE IF NOT EXISTS task (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title TINYTEXT NOT NULL,
  description TEXT,
  status ENUM('PENDING','IN_PROGRESS','DONE') NOT NULL DEFAULT 'PENDING',
  creation_datetime TIMESTAMP NOT NULL,
  due_date DATE DEFAULT NULL,
  creator_id VARCHAR(35) NOT NULL,
  assignee_id VARCHAR(35) DEFAULT NULL,
  deleted_at TIMESTAMP DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (assignee_id) REFERENCES app_user (id),
  FOREIGN KEY (creator_id) REFERENCES app_user (id)
);

-- Tabela de comentários em tarefas
CREATE TABLE IF NOT EXISTS comment (
  id BIGINT NOT NULL AUTO_INCREMENT,
  task_id BIGINT NOT NULL,
  creator_id VARCHAR(35) NOT NULL,
  message VARCHAR(2000) NOT NULL,
  creation_datetime TIMESTAMP NOT NULL,
  deleted_at TIMESTAMP DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (task_id) REFERENCES task (id),
  FOREIGN KEY (creator_id) REFERENCES app_user (id)
);
