INSERT IGNORE INTO vets VALUES (1, 'James', 'Carter');
INSERT IGNORE INTO vets VALUES (2, 'Helen', 'Leary');
INSERT IGNORE INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT IGNORE INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT IGNORE INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT IGNORE INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT IGNORE INTO specialties VALUES (1, 'radiology');
INSERT IGNORE INTO specialties VALUES (2, 'surgery');
INSERT IGNORE INTO specialties VALUES (3, 'dentistry');

INSERT IGNORE INTO vet_specialties VALUES (2, 1);
INSERT IGNORE INTO vet_specialties VALUES (3, 2);
INSERT IGNORE INTO vet_specialties VALUES (3, 3);
INSERT IGNORE INTO vet_specialties VALUES (4, 2);
INSERT IGNORE INTO vet_specialties VALUES (5, 1);

INSERT IGNORE INTO types VALUES (1, 'cat');
INSERT IGNORE INTO types VALUES (2, 'dog');
INSERT IGNORE INTO types VALUES (3, 'lizard');
INSERT IGNORE INTO types VALUES (4, 'snake');
INSERT IGNORE INTO types VALUES (5, 'bird');
INSERT IGNORE INTO types VALUES (6, 'hamster');

INSERT IGNORE INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT IGNORE INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT IGNORE INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT IGNORE INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT IGNORE INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT IGNORE INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT IGNORE INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT IGNORE INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT IGNORE INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT IGNORE INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT IGNORE INTO pets VALUES (1, 'Leo', '2000-09-07', 1, 1);
INSERT IGNORE INTO pets VALUES (2, 'Basil', '2002-08-06', 6, 2);
INSERT IGNORE INTO pets VALUES (3, 'Rosy', '2001-04-17', 2, 3);
INSERT IGNORE INTO pets VALUES (4, 'Jewel', '2000-03-07', 2, 3);
INSERT IGNORE INTO pets VALUES (5, 'Iggy', '2000-11-30', 3, 4);
INSERT IGNORE INTO pets VALUES (6, 'George', '2000-01-20', 4, 5);
INSERT IGNORE INTO pets VALUES (7, 'Samantha', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets VALUES (8, 'Max', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets VALUES (9, 'Lucky', '1999-08-06', 5, 7);
INSERT IGNORE INTO pets VALUES (10, 'Mulligan', '1997-02-24', 2, 8);
INSERT IGNORE INTO pets VALUES (11, 'Freddy', '2000-03-09', 5, 9);
INSERT IGNORE INTO pets VALUES (12, 'Lucky', '2000-06-24', 2, 10);
INSERT IGNORE INTO pets VALUES (13, 'Sly', '2002-06-08', 1, 10);

INSERT IGNORE INTO visits VALUES (1, 7, '2010-03-04', 'rabies shot');
INSERT IGNORE INTO visits VALUES (2, 8, '2011-03-04', 'rabies shot');
INSERT IGNORE INTO visits VALUES (3, 8, '2009-06-04', 'neutered');
INSERT IGNORE INTO visits VALUES (4, 7, '2008-09-04', 'spayed');

INSERT IGNORE INTO titles (id, name, description, display_order) VALUES
                                                            (1, '', 'No Title Chosen', 7),
                                                            (2, 'Mr.', 'The title of Mister for either married or unmarried men.', 2),
                                                            (3, 'Mrs.', 'Mrs. is traditionally used for married women.', 3),
                                                            (4, 'Ms.', 'Ms. is a title that can be used for women regardless of their marital status, or when that status is unknown.', 4),
                                                            (5, 'Miss', 'Miss is a title traditionally used for unmarried women, particularly younger women or girls.', 5),
                                                            (6, 'Mx.', 'Mx is a gender-neutral title that is commonly used by non-binary people.', 6),
                                                            (7, 'Dr.', 'Dr. (Doctor) is an academic title used for individuals who have earned a doctoral degree.', 1);

INSERT IGNORE INTO statuses (id, name, description) VALUES
                                               (1, 'Veteran', 'A veteran of the uniformed services.'),
                                               (2, 'Active Duty', 'An active duty member of the uniformed services.'),
                                               (3, 'First Responder', 'An active Fire, Police, or Emergency personnel'),
                                               (4, 'Spouse', 'A spouse of a veteran, military, or first responder.'),
                                               (5, 'Business Owner', 'A business owner'),
                                               (6, 'Supporter', 'A person that supports veterans, military, and first responders.');

INSERT IGNORE INTO roles (name, description) VALUES
                                               ('SCHOOL_ADMIN', 'Rec Center Admin: Can manage facilities, leagues, scores, and users.'),
                                               ('STUDENT', 'Student: Can join leagues, create teams, and view schedules.'),
                                               ('PLATFORM_ADMIN','Platform Administrator: Can manage all users, businesses, incentives, chains, donations, admin codes, and view analytics dashboard.'),
                                               ('SERVICE_MEMBER','Service Member: Can search for businesses, filter by service type (Veteran/Active Duty/First Responder/Spouse), save favorites, and manage account.'),
                                               ('BUSINESS_OWNER','Business Owner: Can register business, create and manage incentives, update business information, and view listing analytics.'),
                                               ('CHAIN_REPRESENTATIVE','Chain Business Representative: Can register chain entity, manage multiple locations, create chain-wide incentives, and oversee all chain properties.'),
                                               ('DONOR','Donor/Supporter: Can make one-time or recurring donations, view donation history, and receive tax receipts.'),
                                               ('GUEST','Guest/Visitor: Can browse public information, search businesses without account, and view terms of use.'),
                                               ('VETERAN','Veteran: Can search for businesses, view discounts, save favorites, and create account to track preferred locations.'),
                                               ('ACTIVE_DUTY','Active Duty Service Member: Can search for businesses, filter by active duty discounts, and access military benefits.'),
                                               ('FIRST_RESPONDER','First Responder: Can search for businesses offering fire/police/EMS discounts and access first responder benefits.'),
                                               ('MILITARY_SPOUSE','Military Spouse: Can search for businesses, view spouse-specific discounts, and access dependent benefits.');


                                               INSERT IGNORE INTO permissions (name, description) VALUES
                                                     ('MANAGE_OWN_PROFILE', 'Allows user to update their personal info and password.'),
                                                     ('USE_MESSAGING', 'Allows user to send/receive messages with other participants.'),
                                                     ('VIEW_LEAGUES', 'Allows user to browse and search available leagues and activities.'),
                                                     ('REGISTER_FOR_LEAGUE', 'Allows user to register as an individual for a league.'),
                                                     ('CREATE_TEAM', 'Allows user to create a new team as a captain.'),
                                                     ('MANAGE_TEAM_INVITATIONS', 'Allows user to accept or decline invitations to a team.'),
                                                     ('VIEW_OWN_SCHEDULE', 'Allows user to view their personal and team game schedule.'),
                                                     ('VIEW_STANDINGS', 'Allows user to view league standings and team statistics.'),
                                                     ('MANAGE_FACILITIES', 'Allows user to C/R/U/D locations, fields, and courts.'),
                                                     ('MANAGE_SCHEDULES', 'Allows user to C/R/U/D leagues, activities, and games.'),
                                                     ('MANAGE_REGISTRATIONS', 'Allows user to view and approve team registrations.'),
                                                     ('MANAGE_SCORES', 'Allows user to enter and confirm game scores.'),
                                                     ('SEND_ANNOUNCEMENTS', 'Allows user to send messages to individuals, teams, and leagues.');

INSERT IGNORE INTO permission_role (role_id, permission_id) VALUES
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'MANAGE_OWN_PROFILE')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'USE_MESSAGING')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'VIEW_LEAGUES')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'REGISTER_FOR_LEAGUE')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'CREATE_TEAM')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'MANAGE_TEAM_INVITATIONS')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'VIEW_OWN_SCHEDULE')),
                                                              ((SELECT id FROM roles WHERE name = 'STUDENT'), (SELECT id FROM permissions WHERE name = 'VIEW_STANDINGS'));

INSERT IGNORE INTO permission_role (role_id, permission_id) VALUES
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'MANAGE_OWN_PROFILE')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'USE_MESSAGING')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'VIEW_LEAGUES')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'REGISTER_FOR_LEAGUE')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'CREATE_TEAM')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'MANAGE_TEAM_INVITATIONS')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'VIEW_OWN_SCHEDULE')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'VIEW_STANDINGS')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'MANAGE_FACILITIES')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'MANAGE_SCHEDULES')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'MANAGE_REGISTRATIONS')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'MANAGE_SCORES')),
                                                              ((SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN'), (SELECT id FROM permissions WHERE name = 'SEND_ANNOUNCEMENTS'));

INSERT IGNORE INTO users (id, first_name, last_name, email, password_hash, title_id, status_id, created_at, updated_at) VALUES
                                                                                                                          (1, 'Edward', 'McKeown', 'edward@doctormckeown.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 7, 1, NOW(), NOW()),
                                                                                                                          (2, 'Admin', 'User', 'admin@patriotthanks.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2, 1, NOW(), NOW()),
                                                                                                                          (3, 'John', 'Veteran', 'john.veteran@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2, 1, NOW(), NOW()),
                                                                                                                          (4, 'Jane', 'ActiveDuty', 'jane.active@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 4, 2, NOW(), NOW()),
                                                                                                                          (5, 'Mike', 'Firefighter', 'mike.fire@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2, 3, NOW(), NOW()),
                                                                                                                          (6, 'Sarah', 'Business', 'sarah.biz@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 4, 5, NOW(), NOW()),
                                                                                                                          (7, 'Tom', 'Supporter', 'tom.support@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2, 6, NOW(), NOW()),
                                                                                                                          (8, 'Guest', 'User', 'guest@patriotthanks.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, 6, NOW(), NOW());

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
                                                   ((SELECT id FROM users WHERE email = 'brett.baumgart@kirkwood.edu'), (SELECT id FROM roles WHERE name = 'SCHOOL_ADMIN')),
                                                   ((SELECT id FROM users WHERE email = 'alex.student@student.kirkwood.edu'), (SELECT id FROM roles WHERE name = 'STUDENT'));

INSERT IGNORE INTO schools (name, domain, status_id) VALUES
                                                       ('Kirkwood Community College', 'kirkwood.edu', 'active'),
                                                       ('University of Iowa', 'uiowa.edu', 'active'),
                                                       ('Iowa State University', 'iastate.edu', 'active'),
                                                       ('University of Northern Iowa', 'uni.edu', 'active'),
                                                       ('Coe College', 'coe.edu', 'active'),
                                                       ('Mount Mercy University', 'mtmercy.edu', 'active'),
                                                       ('Drake University', 'drake.edu', 'active'),
                                                       ('Grinnell College', 'grinnell.edu', 'active'),
                                                       ('Luther College', 'luther.edu', 'active'),
                                                       ('Simpson College', 'simpson.edu', 'inactive'), -- Testing status
                                                       ('Wartburg College', 'wartburg.edu', 'active'),
                                                       ('Cornell College', 'cornellcollege.edu', 'active'),
                                                       ('Loras College', 'loras.edu', 'active'),
                                                       ('Clarke University', 'clarke.edu', 'suspended'), -- Testing status
                                                       ('St. Ambrose University', 'sau.edu', 'active');

INSERT IGNORE INTO locations (school_id, name, description, address, status_id) VALUES
                                                                                  (1, 'Main Campus', 'The primary campus in Cedar Rapids', '6301 Kirkwood Blvd SW, Cedar Rapids, IA', 'active');

INSERT IGNORE INTO locations (school_id, name, description, address, status_id) VALUES
                                                                                  (2, 'Carver-Hawkeye Arena', 'Main sports arena', '1 Elliott Dr, Iowa City, IA', 'active');

INSERT IGNORE INTO locations (school_id, parent_location_id, name, description, status_id) VALUES
                                                                                             (1, 1, 'Michael J Gould Rec Center', 'Student recreation facility', 'active'),
                                                                                             (1, 1, 'Johnson Hall', 'Athletics building and gymnasium', 'active');

INSERT IGNORE INTO locations (school_id, parent_location_id, name, description, status_id) VALUES
                                                                                             (2, 2, 'Main Court', 'The primary basketball court', 'active'),
                                                                                             (2, 2, 'Weight Room', 'Athlete training facility', 'coming_soon');

INSERT IGNORE INTO locations (school_id, parent_location_id, name, description, status_id) VALUES
                                                                                             (1, 3, 'Basketball Court 1', 'North court', 'active'),
                                                                                             (1, 3, 'Basketball Court 2', 'South court', 'active');
