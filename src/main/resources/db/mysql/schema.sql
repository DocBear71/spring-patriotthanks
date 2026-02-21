CREATE TABLE IF NOT EXISTS vets (
                                  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  first_name VARCHAR(30),
                                  last_name VARCHAR(30),
                                  INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS specialties (
                                         id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(80),
                                         INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS vet_specialties (
                                             vet_id INT(4) UNSIGNED NOT NULL,
                                             specialty_id INT(4) UNSIGNED NOT NULL,
                                             FOREIGN KEY (vet_id) REFERENCES vets(id),
                                             FOREIGN KEY (specialty_id) REFERENCES specialties(id),
                                             UNIQUE (vet_id,specialty_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS types (
                                   id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(80),
                                   INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS owners (
                                    id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    first_name VARCHAR(30),
                                    last_name VARCHAR(30),
                                    address VARCHAR(255),
                                    city VARCHAR(80),
                                    telephone VARCHAR(20),
                                    INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS pets (
                                  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  name VARCHAR(30),
                                  birth_date DATE,
                                  type_id INT(4) UNSIGNED NOT NULL,
                                  owner_id INT(4) UNSIGNED,
                                  INDEX(name),
                                  FOREIGN KEY (owner_id) REFERENCES owners(id),
                                  FOREIGN KEY (type_id) REFERENCES types(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS visits (
                                    id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    pet_id INT(4) UNSIGNED,
                                    visit_date DATE,
                                    description VARCHAR(255),
                                    FOREIGN KEY (pet_id) REFERENCES pets(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS titles
(
  id            int auto_increment
    primary key,
  name          varchar(10)                        not null,
  description   varchar(256)                       null,
  display_order int                                not null,
  created_at    datetime default CURRENT_TIMESTAMP null,
  constraint name
    unique (name)
);

CREATE TABLE IF NOT EXISTS statuses
(
  id          int auto_increment
    primary key,
  name        varchar(50)                        not null,
  description varchar(256)                       null,
  created_at  datetime default CURRENT_TIMESTAMP null,
  updated_at  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
  constraint name
    unique (name)
);

CREATE TABLE IF NOT EXISTS users (
                                   id             int auto_increment
                                     primary key,
                                   first_name     varchar(256)                         not null,
                                   last_name      varchar(256)                         not null,
                                   email          varchar(255)                         not null,
                                   password_hash  varchar(255)                         not null,
                                   title_id       int                                  null,
                                   status_id      int                                  not null,
                                   phone          varchar(20)                          null,
                                   avatar_url     varchar(255)                         null,
                                   email_verified tinyint(1) default 0                 null,
                                   created_at     datetime   default CURRENT_TIMESTAMP null,
                                   updated_at     datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
                                   deleted_at     datetime                             null,
                                   constraint email
                                     unique (email),
                                   constraint users_ibfk_1
                                     foreign key (title_id) references titles (id),
                                   constraint users_ibfk_2
                                     foreign key (status_id) references statuses (id)
);

CREATE TABLE IF NOT EXISTS roles (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(50) NOT NULL UNIQUE,
                                   description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS permissions (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(100) NOT NULL UNIQUE,
                                         description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles (
                                        user_id INT NOT NULL,
                                        role_id INT NOT NULL,
                                        PRIMARY KEY (user_id, role_id),
                                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS permission_role (
                                             permission_id INT NOT NULL,
                                             role_id INT NOT NULL,
                                             PRIMARY KEY (permission_id, role_id),
                                             FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
                                             FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS schools (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     domain VARCHAR(255) NOT NULL,
                                     status_id ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     deleted_at DATETIME DEFAULT NULL,
                                     UNIQUE INDEX idx_schools_domain (domain)
);

CREATE TABLE IF NOT EXISTS locations (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       school_id INT NOT NULL,
                                       parent_location_id INT NULL,
                                       name VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       address VARCHAR(255),
                                       latitude DECIMAL(8,4),
                                       longitude DECIMAL(8,4),
                                       status_id ENUM('DRAFT', 'ACTIVE', 'CLOSED', 'COMING_SOON') DEFAULT 'ACTIVE',
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       deleted_at DATETIME DEFAULT NULL,
                                       CONSTRAINT fk_locations_school FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE,
                                       CONSTRAINT fk_locations_parent FOREIGN KEY (parent_location_id) REFERENCES locations(id) ON DELETE SET NULL
);

-- =====================================================================
-- Patriot Thanks Business Tables
-- =====================================================================

CREATE TABLE IF NOT EXISTS business_types (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            name VARCHAR(80) NOT NULL,
                                            description VARCHAR(255),
                                            display_order INT DEFAULT 0,
                                            is_active TINYINT(1) DEFAULT 1,
                                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                            INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS us_states (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       code VARCHAR(2) NOT NULL,
                                       name VARCHAR(50) NOT NULL,
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       UNIQUE INDEX idx_us_states_code (code)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS addresses (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       street_address VARCHAR(255) NOT NULL,
                                       address_line_2 VARCHAR(255),
                                       city VARCHAR(80) NOT NULL,
                                       state_id INT NOT NULL,
                                       zip_code VARCHAR(10) NOT NULL,
                                       latitude DECIMAL(10,7),
                                       longitude DECIMAL(10,7),
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       CONSTRAINT fk_addresses_state FOREIGN KEY (state_id) REFERENCES us_states(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS businesses (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        description TEXT,
                                        website VARCHAR(255),
                                        business_type_id INT NOT NULL,
                                        submitted_by_user_id INT,
                                        is_verified TINYINT(1) DEFAULT 0,
                                        is_active TINYINT(1) DEFAULT 1,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        deleted_at DATETIME DEFAULT NULL,
                                        INDEX(name),
                                        CONSTRAINT fk_businesses_type FOREIGN KEY (business_type_id) REFERENCES business_types(id),
                                        CONSTRAINT fk_businesses_user FOREIGN KEY (submitted_by_user_id) REFERENCES users(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS business_locations (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                business_id INT NOT NULL,
                                                address_id INT,
                                                location_name VARCHAR(255),
                                                phone VARCHAR(20),
                                                email VARCHAR(255),
                                                hours_of_operation VARCHAR(255),
                                                is_primary TINYINT(1) DEFAULT 0,
                                                is_active TINYINT(1) DEFAULT 1,
                                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                deleted_at DATETIME DEFAULT NULL,
                                                CONSTRAINT fk_business_locations_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
                                                CONSTRAINT fk_business_locations_address FOREIGN KEY (address_id) REFERENCES addresses(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS incentive_types (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             name VARCHAR(80) NOT NULL,
                                             description VARCHAR(255),
                                             display_order INT DEFAULT 0,
                                             is_active TINYINT(1) DEFAULT 1,
                                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS incentives (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        title VARCHAR(255) NOT NULL,
                                        description TEXT NOT NULL,
                                        discount_amount DECIMAL(10,2),
                                        discount_percentage DECIMAL(5,2),
                                        terms_and_conditions TEXT,
                                        verification_required VARCHAR(255),
                                        start_date DATE,
                                        end_date DATE,
                                        is_active TINYINT(1) DEFAULT 1,
                                        business_id INT NOT NULL,
                                        submitted_by_user_id INT,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        deleted_at DATETIME DEFAULT NULL,
                                        CONSTRAINT fk_incentives_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
                                        CONSTRAINT fk_incentives_user FOREIGN KEY (submitted_by_user_id) REFERENCES users(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS business_incentive_types (
                                                      incentive_id INT NOT NULL,
                                                      incentive_type_id INT NOT NULL,
                                                      PRIMARY KEY (incentive_id, incentive_type_id),
                                                      CONSTRAINT fk_bit_incentive FOREIGN KEY (incentive_id) REFERENCES incentives(id) ON DELETE CASCADE,
                                                      CONSTRAINT fk_bit_type FOREIGN KEY (incentive_type_id) REFERENCES incentive_types(id) ON DELETE CASCADE
) engine=InnoDB;
