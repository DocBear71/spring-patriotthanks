-- mysql/data.sql

-- =====================================================================
-- COMPLETE MySQL SEED DATA
-- PetClinic + AthLeagues + Patriot Thanks + Patriot Users
-- =====================================================================

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================================
-- PETCLINIC DATA
-- =====================================================================

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

-- =====================================================================
-- ATHLEAGUES LOOKUP DATA
-- =====================================================================

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

-- =====================================================================
-- ATHLEAGUES RBAC DATA
-- =====================================================================

INSERT IGNORE INTO roles (id, name, description) VALUES
                                                   (1, 'SCHOOL_ADMIN', 'Rec Center Admin: Can manage facilities, leagues, scores, and users.'),
                                                   (2, 'STUDENT', 'Student: Can join leagues, create teams, and view schedules.'),
                                                   (3, 'PLATFORM_ADMIN', 'Platform Administrator: Full system access.'),
                                                   (4, 'SERVICE_MEMBER', 'Service Member: Can search for businesses and filter by service type.'),
                                                   (5, 'BUSINESS_OWNER', 'Business Owner: Can register business and manage incentives.'),
                                                   (6, 'CHAIN_REPRESENTATIVE', 'Chain Rep: Can manage multiple locations and chain-wide incentives.'),
                                                   (7, 'DONOR', 'Donor/Supporter: Can make donations and view donation history.'),
                                                   (8, 'GUEST', 'Guest/Visitor: Can browse public information.'),
                                                   (9, 'VETERAN', 'Veteran: Can search for businesses and view discounts.'),
                                                   (10, 'ACTIVE_DUTY', 'Active Duty: Can search for businesses and filter by active duty discounts.'),
                                                   (11, 'FIRST_RESPONDER', 'First Responder: Can search for fire/police/EMS discounts.'),
                                                   (12, 'MILITARY_SPOUSE', 'Military Spouse: Can search for spouse-specific discounts.');

INSERT IGNORE INTO permissions (id, name, description) VALUES
                                                         (1, 'MANAGE_OWN_PROFILE', 'Allows user to update their personal info and password.'),
                                                         (2, 'USE_MESSAGING', 'Allows user to send/receive messages with other participants.'),
                                                         (3, 'VIEW_LEAGUES', 'Allows user to browse and search available leagues and activities.'),
                                                         (4, 'REGISTER_FOR_LEAGUE', 'Allows user to register as an individual for a league.'),
                                                         (5, 'CREATE_TEAM', 'Allows user to create a new team as a captain.'),
                                                         (6, 'MANAGE_TEAM_INVITATIONS', 'Allows user to accept or decline invitations to a team.'),
                                                         (7, 'VIEW_OWN_SCHEDULE', 'Allows user to view their personal and team game schedule.'),
                                                         (8, 'VIEW_STANDINGS', 'Allows user to view league standings and team statistics.'),
                                                         (9, 'MANAGE_FACILITIES', 'Allows user to C/R/U/D locations, fields, and courts.'),
                                                         (10, 'MANAGE_SCHEDULES', 'Allows user to C/R/U/D leagues, activities, and games.'),
                                                         (11, 'MANAGE_REGISTRATIONS', 'Allows user to view and approve team registrations.'),
                                                         (12, 'MANAGE_SCORES', 'Allows user to enter and confirm game scores.'),
                                                         (13, 'SEND_ANNOUNCEMENTS', 'Allows user to send messages to individuals, teams, and leagues.');

-- Student permissions
INSERT IGNORE INTO permission_role (permission_id, role_id) VALUES
                                                              (1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2);

-- School Admin permissions (all)
INSERT IGNORE INTO permission_role (permission_id, role_id) VALUES
                                                              (1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1), (11, 1), (12, 1), (13, 1);

-- =====================================================================
-- ATHLEAGUES USERS (password for all: password123)
-- =====================================================================

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
                                                   (1, 3), (2, 3), (3, 9), (4, 10), (5, 11), (6, 5), (7, 7), (8, 8);

-- =====================================================================
-- ATHLEAGUES SCHOOLS & LOCATIONS
-- =====================================================================

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
                                                       ('Simpson College', 'simpson.edu', 'inactive'),
                                                       ('Wartburg College', 'wartburg.edu', 'active'),
                                                       ('Cornell College', 'cornellcollege.edu', 'active'),
                                                       ('Loras College', 'loras.edu', 'active'),
                                                       ('Clarke University', 'clarke.edu', 'suspended'),
                                                       ('St. Ambrose University', 'sau.edu', 'active');

INSERT IGNORE INTO locations (school_id, name, description, address, status_id) VALUES
                                                                                  (1, 'Main Campus', 'The primary campus in Cedar Rapids', '6301 Kirkwood Blvd SW, Cedar Rapids, IA', 'active'),
                                                                                  (2, 'Carver-Hawkeye Arena', 'Main sports arena', '1 Elliott Dr, Iowa City, IA', 'active');

INSERT IGNORE INTO locations (school_id, parent_location_id, name, description, status_id) VALUES
                                                                                             (1, 1, 'Michael J Gould Rec Center', 'Student recreation facility', 'active'),
                                                                                             (1, 1, 'Johnson Hall', 'Athletics building and gymnasium', 'active'),
                                                                                             (2, 2, 'Main Court', 'The primary basketball court', 'active'),
                                                                                             (2, 2, 'Weight Room', 'Athlete training facility', 'coming_soon'),
                                                                                             (1, 3, 'Basketball Court 1', 'North court', 'active'),
                                                                                             (1, 3, 'Basketball Court 2', 'South court', 'active');

-- =====================================================================
-- PATRIOT THANKS: US STATES (all 50 + DC)
-- =====================================================================

INSERT IGNORE INTO us_states (id, code, name) VALUES
                                                (1, 'AL', 'Alabama'), (2, 'AK', 'Alaska'), (3, 'AZ', 'Arizona'), (4, 'AR', 'Arkansas'),
                                                (5, 'CA', 'California'), (6, 'CO', 'Colorado'), (7, 'CT', 'Connecticut'), (8, 'DE', 'Delaware'),
                                                (9, 'FL', 'Florida'), (10, 'GA', 'Georgia'), (11, 'HI', 'Hawaii'), (12, 'ID', 'Idaho'),
                                                (13, 'IL', 'Illinois'), (14, 'IN', 'Indiana'), (15, 'IA', 'Iowa'), (16, 'KS', 'Kansas'),
                                                (17, 'KY', 'Kentucky'), (18, 'LA', 'Louisiana'), (19, 'ME', 'Maine'), (20, 'MD', 'Maryland'),
                                                (21, 'MA', 'Massachusetts'), (22, 'MI', 'Michigan'), (23, 'MN', 'Minnesota'), (24, 'MS', 'Mississippi'),
                                                (25, 'MO', 'Missouri'), (26, 'MT', 'Montana'), (27, 'NE', 'Nebraska'), (28, 'NV', 'Nevada'),
                                                (29, 'NH', 'New Hampshire'), (30, 'NJ', 'New Jersey'), (31, 'NM', 'New Mexico'), (32, 'NY', 'New York'),
                                                (33, 'NC', 'North Carolina'), (34, 'ND', 'North Dakota'), (35, 'OH', 'Ohio'), (36, 'OK', 'Oklahoma'),
                                                (37, 'OR', 'Oregon'), (38, 'PA', 'Pennsylvania'), (39, 'RI', 'Rhode Island'), (40, 'SC', 'South Carolina'),
                                                (41, 'SD', 'South Dakota'), (42, 'TN', 'Tennessee'), (43, 'TX', 'Texas'), (44, 'UT', 'Utah'),
                                                (45, 'VT', 'Vermont'), (46, 'VA', 'Virginia'), (47, 'WA', 'Washington'), (48, 'WV', 'West Virginia'),
                                                (49, 'WI', 'Wisconsin'), (50, 'WY', 'Wyoming'), (51, 'DC', 'District of Columbia');

-- =====================================================================
-- PATRIOT THANKS: BUSINESS TYPES (25 categories)
-- =====================================================================

INSERT IGNORE INTO business_types (id, name, description, display_order, is_active) VALUES
                                                                                      (1, 'Automotive', 'Vehicles, parts, repairs, and transportation services.', 1, 1),
                                                                                      (2, 'Beauty', 'Cosmetics, skincare, salon services, and personal care products.', 2, 1),
                                                                                      (3, 'Bookstore', 'Books, magazines, educational materials, and reading accessories.', 3, 1),
                                                                                      (4, 'Clothing', 'Apparel, footwear, accessories, and fashion items.', 4, 1),
                                                                                      (5, 'Convenience Store', 'Quick-access groceries, snacks, beverages, and everyday essentials.', 5, 1),
                                                                                      (6, 'Department Store', 'Multi-category retail offering clothing, home goods, and general merchandise.', 6, 1),
                                                                                      (7, 'Electronics', 'Consumer electronics, appliances, and electronic accessories.', 7, 1),
                                                                                      (8, 'Entertainment', 'Media, events, performances, and recreational activities.', 8, 1),
                                                                                      (9, 'Furniture', 'Home and office furniture, fixtures, and decor items.', 9, 1),
                                                                                      (10, 'Fuel Station', 'Gasoline, diesel, vehicle fluids, and convenience items.', 10, 1),
                                                                                      (11, 'Gift Shop', 'Gifts, cards, novelties, and specialty presentation items.', 11, 1),
                                                                                      (12, 'Grocery', 'Food, beverages, household supplies, and perishable goods.', 12, 1),
                                                                                      (13, 'Hardware', 'Tools, building materials, and home improvement supplies.', 13, 1),
                                                                                      (14, 'Health', 'Health services, medical supplies, and wellness products.', 14, 1),
                                                                                      (15, 'Hotel/Motel', 'Lodging, accommodations, and hospitality services.', 15, 1),
                                                                                      (16, 'Jewelry', 'Fine jewelry, watches, precious metals, and gemstones.', 16, 1),
                                                                                      (17, 'Other', 'Miscellaneous businesses not fitting standard categories.', 17, 1),
                                                                                      (18, 'Pharmacy', 'Prescription medications, over-the-counter drugs, and health consultations.', 18, 1),
                                                                                      (19, 'Restaurant', 'Food preparation, dining services, and culinary experiences.', 19, 1),
                                                                                      (20, 'Retail', 'General consumer goods sold directly to customers.', 20, 1),
                                                                                      (21, 'Service', 'Professional services, repairs, and skilled labor.', 21, 1),
                                                                                      (22, 'Specialty', 'Niche products and specialized goods for specific markets.', 22, 1),
                                                                                      (23, 'Sporting Goods', 'Athletic equipment, sportswear, and recreational gear.', 23, 1),
                                                                                      (24, 'Technology', 'Computer hardware, software, IT services, and digital solutions.', 24, 1),
                                                                                      (25, 'Toys', 'Children\'s toys, games, educational play items, and hobbies.', 25, 1);

-- =====================================================================
-- PATRIOT THANKS: INCENTIVE TYPES
-- =====================================================================

INSERT IGNORE INTO incentive_types (id, name, description, display_order, is_active) VALUES
                                                                                       (1, 'Veteran', 'Veterans special pricing and benefits.', 1, 1),
                                                                                       (2, 'Active Duty', 'Active duty military personnel discount.', 2, 1),
                                                                                       (3, 'First Responder', 'Discounts for emergency services professionals.', 3, 1),
                                                                                       (4, 'Spouse', 'Benefits extended to military/responder spouses.', 4, 1),
                                                                                       (5, 'Other', 'Alternative special discounts or promotional offers.', 5, 1);

-- =====================================================================
-- PATRIOT THANKS: BUSINESSES (43 real businesses)
-- =====================================================================

INSERT IGNORE INTO businesses (id, name, slug, business_type_id, website, description, submitted_by_user_id, is_verified, is_active, created_at, updated_at) VALUES
                                                                                                                                                               (1, 'Olive Garden', 'olive-garden', 19, NULL, 'Italian restaurant chain', 6, 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                                               (2, 'Taco Bell', 'taco-bell', 19, NULL, 'Fast food Mexican restaurant', 6, 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                                               (3, 'Hy-Vee Fast & Fresh', 'hy-vee-fast-and-fresh', 5, NULL, 'Convenience store and gas station', 6, 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                                               (4, 'Red Lobster', 'red-lobster', 19, NULL, 'Seafood restaurant chain', 6, 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                                               (5, 'The Home Depot', 'the-home-depot', 13, NULL, 'Home improvement retailer', 6, 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                                               (6, 'Texas Roadhouse', 'texas-roadhouse', 19, NULL, 'Steakhouse restaurant chain', 6, 1, 1, '2025-04-21 00:05:01', '2025-04-21 00:05:01'),
                                                                                                                                                               (7, 'Milex Complete Car Care', 'milex-complete-car-care', 1, NULL, 'Automotive service and repair', 6, 1, 1, '2025-04-21 02:19:10', '2025-04-21 02:19:10'),
                                                                                                                                                               (8, 'Wendy\'s', 'wendys', 19, NULL, 'Fast food hamburger restaurant', 6, 1, 1, '2025-04-24 23:48:29', '2025-04-24 23:48:29'),
                                                                                                                                                               (9, 'El Viejo Mexican Restaurant', 'el-viejo-mexican-restaurant', 19, NULL, 'Authentic Mexican restaurant', 6, 1, 1, '2025-04-24 23:51:29', '2025-04-24 23:51:29'),
                                                                                                                                                               (10, 'China Inn Restaurant', 'china-inn-restaurant', 19, NULL, 'Chinese cuisine restaurant', 6, 1, 1, '2025-04-25 00:01:09', '2025-04-25 00:01:09'),
                                                                                                                                                               (11, 'Kirkwood Community College', 'kirkwood-community-college', 17, NULL, 'Educational institution', NULL, 1, 1, '2025-04-27 00:06:22', '2025-04-27 00:06:22'),
                                                                                                                                                               (12, 'Taco Bell Marion', 'taco-bell-marion', 19, NULL, 'Fast food Mexican restaurant', 6, 1, 1, '2025-05-03 00:06:34', '2025-05-03 00:06:34'),
                                                                                                                                                               (13, 'Biaggi\'s Ristorante Italiano', 'biaggis-ristorante-italiano', 19, NULL, 'Italian fine dining restaurant', 6, 1, 1, '2025-05-03 02:57:12', '2025-05-03 02:57:12'),
                                                                                                                                                               (14, 'Taco John\'s', 'taco-johns', 19, NULL, 'Fast food Mexican restaurant', 6, 1, 1, '2025-05-03 02:58:41', '2025-05-03 02:58:41'),
                                                                                                                                                               (15, 'Perkins American Food Co.', 'perkins-american-food-co', 19, NULL, 'Family dining restaurant', 6, 1, 1, '2025-05-03 03:13:34', '2025-05-03 03:13:34'),
                                                                                                                                                               (16, 'Culver\'s', 'culvers', 19, NULL, 'Fast casual restaurant', 6, 1, 1, '2025-05-04 17:54:39', '2025-05-04 17:54:39'),
                                                                                                                                                               (17, 'The Home Depot Bronx', 'the-home-depot-bronx', 13, NULL, 'Home improvement retailer', 6, 1, 1, '2025-05-21 12:58:19', '2025-05-21 12:58:19'),
                                                                                                                                                               (18, 'The Home Depot Las Vegas', 'the-home-depot-las-vegas', 13, NULL, 'Home improvement retailer', 6, 1, 1, '2025-05-23 00:58:38', '2025-05-23 00:58:38'),
                                                                                                                                                               (19, 'Olive Garden Las Vegas', 'olive-garden-las-vegas', 19, NULL, 'Italian restaurant chain', 6, 1, 1, '2025-05-24 03:44:58', '2025-05-24 03:44:58'),
                                                                                                                                                               (20, 'The Home Depot Henderson', 'the-home-depot-henderson', 13, NULL, 'Home improvement retailer', 6, 1, 1, '2025-05-24 15:32:31', '2025-05-24 15:32:31'),
                                                                                                                                                               (21, 'Olive Garden Pensacola', 'olive-garden-pensacola', 19, NULL, 'Italian restaurant chain', 6, 1, 1, '2025-05-25 04:18:50', '2025-05-25 04:18:50'),
                                                                                                                                                               (22, 'Taylor\'s Breakfast and Lunch', 'taylors-breakfast-and-lunch', 19, NULL, 'Breakfast and lunch restaurant', 6, 1, 1, '2025-05-26 14:00:01', '2025-05-26 14:00:01'),
                                                                                                                                                               (23, 'Applebee\'s Grill + Bar', 'applebees-grill-and-bar', 19, NULL, 'Casual dining restaurant', 6, 1, 1, '2025-05-30 20:34:42', '2025-05-30 20:34:42'),
                                                                                                                                                               (24, 'Red Robin Gourmet Burgers', 'red-robin-gourmet-burgers', 19, NULL, 'Burger restaurant chain', 6, 1, 1, '2025-05-31 01:31:03', '2025-05-31 01:31:03'),
                                                                                                                                                               (25, 'The Home Depot Lafayette', 'the-home-depot-lafayette', 13, NULL, 'Home improvement retailer', 6, 1, 1, '2025-05-31 01:32:04', '2025-05-31 01:32:04'),
                                                                                                                                                               (26, 'Golden Corral Buffet & Grill', 'golden-corral-buffet-and-grill', 19, NULL, 'Buffet restaurant chain', 6, 1, 1, '2025-05-31 01:32:33', '2025-05-31 01:32:33'),
                                                                                                                                                               (27, 'Outback Steakhouse', 'outback-steakhouse', 19, NULL, 'Steakhouse restaurant chain', 6, 1, 1, '2025-05-31 01:33:03', '2025-05-31 01:33:03'),
                                                                                                                                                               (28, 'Red Lobster Lafayette', 'red-lobster-lafayette', 19, NULL, 'Seafood restaurant chain', 6, 1, 1, '2025-05-31 01:34:10', '2025-05-31 01:34:10'),
                                                                                                                                                               (29, 'Lowe\'s Home Improvement', 'lowes-home-improvement', 13, NULL, 'Home improvement retailer', 6, 1, 1, '2025-05-31 01:34:42', '2025-05-31 01:34:42'),
                                                                                                                                                               (30, 'Sonic Drive-In', 'sonic-drive-in', 19, NULL, 'Fast food drive-in restaurant', 6, 1, 1, '2025-05-31 01:35:21', '2025-05-31 01:35:21'),
                                                                                                                                                               (31, 'Shag Cedar Rapids', 'shag-cedar-rapids', 22, NULL, 'Specialty carpet store', 6, 1, 1, '2025-06-02 23:02:19', '2025-06-02 23:02:19'),
                                                                                                                                                               (32, 'Shag Wiley Blvd', 'shag-wiley-blvd', 22, NULL, 'Specialty carpet store', 6, 1, 1, '2025-06-02 23:03:21', '2025-06-02 23:03:21'),
                                                                                                                                                               (33, 'Shag Marshalltown', 'shag-marshalltown', 22, NULL, 'Specialty carpet store', 6, 1, 1, '2025-06-02 23:50:03', '2025-06-02 23:50:03'),
                                                                                                                                                               (34, 'Shag West Des Moines', 'shag-west-des-moines', 22, NULL, 'Specialty carpet store', 6, 1, 1, '2025-06-02 23:51:39', '2025-06-02 23:51:39'),
                                                                                                                                                               (35, 'Raising Cane\'s Chicken Fingers', 'raising-canes-chicken-fingers', 19, NULL, 'Fast food chicken restaurant', 6, 1, 1, '2025-06-03 00:46:18', '2025-06-03 00:46:18'),
                                                                                                                                                               (36, 'Holt Adventure Co.', 'holt-adventure-co', 22, NULL, 'Outdoor adventure specialty store', 6, 1, 1, '2025-06-03 01:37:15', '2025-06-03 01:37:15'),
                                                                                                                                                               (37, 'Dairy Queen Grill & Chill', 'dairy-queen-grill-and-chill', 19, NULL, 'Fast food ice cream restaurant', 6, 1, 1, '2025-06-03 01:39:00', '2025-06-03 01:39:00'),
                                                                                                                                                               (38, 'Hy-Vee Grocery Store', 'hy-vee-grocery-store', 12, NULL, 'Full service grocery store', 6, 1, 1, '2025-09-24 17:05:15', '2025-09-24 17:05:15'),
                                                                                                                                                               (39, 'OfficeMax', 'officemax', 22, NULL, 'Office supplies retailer', 6, 1, 1, '2025-10-07 16:39:41', '2025-10-07 16:39:41'),
                                                                                                                                                               (40, 'Taste Of India', 'taste-of-india', 19, NULL, 'Indian cuisine restaurant', 6, 1, 1, '2025-10-24 22:43:16', '2025-10-24 22:43:16'),
                                                                                                                                                               (41, 'Marcus Cedar Rapids Cinema', 'marcus-cedar-rapids-cinema', 8, NULL, 'Movie theater', 6, 1, 1, '2025-10-24 22:53:53', '2025-10-24 22:53:53'),
                                                                                                                                                               (42, 'The Grand White Rabbit Tea Co', 'the-grand-white-rabbit-tea-co', 19, NULL, 'Tea house and cafe', 6, 1, 1, '2025-10-24 23:22:46', '2025-10-24 23:22:46'),
                                                                                                                                                               (43, 'Pak Mail', 'pak-mail', 22, NULL, 'Mail and Postal Services', 6, 1, 1, '2025-10-24 23:22:46', '2025-10-24 23:22:46');

-- =====================================================================
-- PATRIOT THANKS: ADDRESSES
-- =====================================================================

INSERT IGNORE INTO addresses (id, street_address, address_line_2, city, state_id, zip_code, created_at, updated_at) VALUES
                                                                                                                      (1001, '367 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-07 16:26:29', '2025-10-25 00:23:55'),
                                                                                                                      (1002, '750 Marion Blvd', NULL, 'Marion', 15, '52302', '2025-05-07 16:26:29', '2025-10-25 00:21:10'),
                                                                                                                      (1003, '3935 Blairs Ferry Rd', NULL, 'Cedar Rapids', 15, '52402', '2025-05-07 16:26:29', '2025-10-25 00:04:22'),
                                                                                                                      (1004, '163 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-07 16:26:29', '2025-10-25 00:04:27'),
                                                                                                                      (1005, '4501 1st Ave SE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-07 16:26:29', '2025-10-25 00:04:33'),
                                                                                                                      (1006, '2425 Edgewood Rd SW', NULL, 'Cedar Rapids', 15, '52404', '2025-04-21 00:05:01', '2025-10-25 00:04:56'),
                                                                                                                      (1007, '2010 Sylvia Ave NE', NULL, 'Cedar Rapids', 15, '52402', '2025-04-21 02:19:10', '2025-10-25 00:05:07'),
                                                                                                                      (1008, '190 Collins Rd NE', 'By Hobby Lobby', 'Cedar Rapids', 15, '52402', '2025-04-24 23:48:29', '2025-10-25 00:05:12'),
                                                                                                                      (1009, '90 Twixt Town Rd', NULL, 'Cedar Rapids', 15, '52402', '2025-04-24 23:51:29', '2025-10-25 00:05:19'),
                                                                                                                      (1010, '1401 1st Ave SE', NULL, 'Cedar Rapids', 15, '52402', '2025-04-25 00:01:09', '2025-10-25 00:05:24'),
                                                                                                                      (1011, '6301 Kirkwood Blvd SW', NULL, 'Cedar Rapids', 15, '52404', '2025-04-27 00:06:22', '2025-10-25 00:05:31'),
                                                                                                                      (1012, '3045 Williams Blvd SW', 'Next to SSA', 'Cedar Rapids', 15, '52404', '2025-05-03 00:06:34', '2025-10-25 00:05:38'),
                                                                                                                      (1013, '320 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-03 02:57:12', '2025-10-25 00:05:44'),
                                                                                                                      (1014, '223 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-03 02:58:41', '2025-10-25 00:05:50'),
                                                                                                                      (1015, '315 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-03 03:13:34', '2025-10-25 00:05:55'),
                                                                                                                      (1016, '2405 Edgewood Rd SW', NULL, 'Cedar Rapids', 15, '52404', '2025-05-04 17:54:39', '2025-10-25 00:06:00'),
                                                                                                                      (1017, '2560 Bruckner Blvd', NULL, 'New York', 32, '10465', '2025-05-21 12:58:19', '2025-10-25 00:06:07'),
                                                                                                                      (1018, '6025 S Pecos Rd', NULL, 'Las Vegas', 28, '89120', '2025-05-23 00:58:38', '2025-10-25 00:06:48'),
                                                                                                                      (1019, '1545 E Flamingo Rd', NULL, 'Las Vegas', 28, '89119', '2025-05-24 03:44:58', '2025-10-25 00:06:53'),
                                                                                                                      (1020, '1030 W Sunset Rd', NULL, 'Henderson', 28, '89014', '2025-05-24 15:32:31', '2025-10-25 00:06:57'),
                                                                                                                      (1021, '5037 Bayou Blvd', NULL, 'Pensacola', 9, '32503', '2025-05-25 04:18:50', '2025-10-25 00:07:03'),
                                                                                                                      (1022, '7175 N Davis Hwy', NULL, 'Pensacola', 9, '32504', '2025-05-26 14:00:01', '2025-10-25 00:07:08'),
                                                                                                                      (1023, '303 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-30 20:34:42', '2025-10-25 00:07:12'),
                                                                                                                      (1024, '4625 1st Ave SE', NULL, 'Cedar Rapids', 15, '52402', '2025-05-31 01:31:03', '2025-10-25 00:07:17'),
                                                                                                                      (1025, '311 Sagamore Pkwy N', NULL, 'Lafayette', 14, '47904', '2025-05-31 01:32:04', '2025-10-25 00:07:22'),
                                                                                                                      (1026, '79 Shenandoah Dr', NULL, 'Lafayette', 14, '47905', '2025-05-31 01:32:33', '2025-10-25 00:07:28'),
                                                                                                                      (1027, '3660 State Road 26', NULL, 'Lafayette', 14, '47905', '2025-05-31 01:33:03', '2025-10-25 00:07:34'),
                                                                                                                      (1028, '120 S Creasy Ln', NULL, 'Lafayette', 14, '47905', '2025-05-31 01:34:10', '2025-10-25 00:07:38'),
                                                                                                                      (1029, '100 N Creasy Ln', NULL, 'Lafayette', 14, '47905', '2025-05-31 01:34:42', '2025-10-25 00:07:43'),
                                                                                                                      (1030, '150 S Creasy Ln', NULL, 'Lafayette', 14, '47905', '2025-05-31 01:35:21', '2025-10-25 00:07:47'),
                                                                                                                      (1031, '4444 1st Ave NE Suite #66', NULL, 'Cedar Rapids', 15, '52402', '2025-06-02 23:02:19', '2025-10-25 00:08:00'),
                                                                                                                      (1032, '3005 Wiley Blvd SW #115', NULL, 'Cedar Rapids', 15, '52404', '2025-06-02 23:03:21', '2025-10-25 00:08:09'),
                                                                                                                      (1033, '2501 S Center St Ste. B', NULL, 'Marshalltown', 15, '50158', '2025-06-02 23:50:03', '2025-10-25 00:08:14'),
                                                                                                                      (1034, '2600 University Ave #200', NULL, 'West Des Moines', 15, '50266', '2025-06-02 23:51:39', '2025-10-25 00:08:18'),
                                                                                                                      (1035, '230 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-06-03 00:46:18', '2025-10-25 00:08:23'),
                                                                                                                      (1036, '48333 E 1st St', NULL, 'Oakridge', 37, '97463', '2025-06-03 01:37:15', '2025-10-25 00:38:35'),
                                                                                                                      (1037, '47720 OR-58', NULL, 'Oakridge', 37, '97463', '2025-06-03 01:39:00', '2025-10-25 00:08:38'),
                                                                                                                      (1038, '3235 Oakland Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-09-24 17:05:15', '2025-10-25 00:08:44'),
                                                                                                                      (1039, '327 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-10-07 16:39:41', '2025-10-25 00:08:48'),
                                                                                                                      (1040, '1060 Old Marion Rd NE', NULL, 'Cedar Rapids', 15, '52402', '2025-10-24 22:43:16', '2025-10-25 00:09:00'),
                                                                                                                      (1041, '5340 Council St NE', NULL, 'Cedar Rapids', 15, '52402', '2025-10-24 22:53:53', '2025-10-25 00:09:06'),
                                                                                                                      (1042, '4444 1st Ave NE Kiosk 3519', NULL, 'Cedar Rapids', 15, '52402', '2025-10-24 23:22:46', '2025-10-24 23:22:47'),
                                                                                                                      (1043, '5249 N Park Place NE', NULL, 'Cedar Rapids', 15, '52402', '2025-10-24 23:22:46', '2025-10-24 23:22:47');

-- =====================================================================
-- PATRIOT THANKS: BUSINESS LOCATIONS
-- =====================================================================

INSERT IGNORE INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active, created_at, updated_at) VALUES
                                                                                                                                           (1, 1, 1001, NULL, '319-378-6401', 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                           (2, 2, 1002, NULL, '319-377-8747', 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                           (3, 3, 1003, NULL, '319-378-0684', 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                           (4, 4, 1004, NULL, '319-395-0450', 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                           (5, 5, 1005, NULL, '319-294-0480', 1, 1, '2025-05-07 16:26:29', '2025-05-07 16:26:29'),
                                                                                                                                           (6, 6, 1006, NULL, '319-396-3300', 1, 1, '2025-04-21 00:05:01', '2025-04-21 00:05:01'),
                                                                                                                                           (7, 7, 1007, NULL, '319-382-0090', 1, 1, '2025-04-21 02:19:10', '2025-04-21 02:19:10'),
                                                                                                                                           (8, 8, 1008, 'By Hobby Lobby', '319-373-4188', 1, 1, '2025-04-24 23:48:29', '2025-04-24 23:48:29'),
                                                                                                                                           (9, 9, 1009, NULL, '319-200-4447', 1, 1, '2025-04-24 23:51:29', '2025-04-24 23:51:29'),
                                                                                                                                           (10, 10, 1010, NULL, '319-247-1888', 1, 1, '2025-04-25 00:01:09', '2025-04-25 00:01:09'),
                                                                                                                                           (11, 11, 1011, NULL, '319-999-9999', 1, 1, '2025-04-27 00:06:22', '2025-04-27 00:06:22'),
                                                                                                                                           (12, 12, 1012, 'Next to SSA', '319-366-0607', 1, 1, '2025-05-03 00:06:34', '2025-05-03 00:06:34'),
                                                                                                                                           (13, 13, 1013, NULL, '319-393-6593', 1, 1, '2025-05-03 02:57:12', '2025-05-03 02:57:12'),
                                                                                                                                           (14, 14, 1014, NULL, '319-450-0532', 1, 1, '2025-05-03 02:58:41', '2025-05-03 02:58:41'),
                                                                                                                                           (15, 15, 1015, NULL, '319-382-2122', 1, 1, '2025-05-03 03:13:34', '2025-05-03 03:13:34'),
                                                                                                                                           (16, 16, 1016, NULL, '319-390-2061', 1, 1, '2025-05-04 17:54:39', '2025-05-04 17:54:39'),
                                                                                                                                           (17, 17, 1017, NULL, '718-828-1071', 1, 1, '2025-05-21 12:58:19', '2025-05-21 12:58:19'),
                                                                                                                                           (18, 18, 1018, NULL, '702-434-1948', 1, 1, '2025-05-23 00:58:38', '2025-05-23 00:58:38'),
                                                                                                                                           (19, 19, 1019, NULL, '702-735-0082', 1, 1, '2025-05-24 03:44:58', '2025-05-24 03:44:58'),
                                                                                                                                           (20, 20, 1020, NULL, '702-435-9200', 1, 1, '2025-05-24 15:32:31', '2025-05-24 15:32:31'),
                                                                                                                                           (21, 21, 1021, NULL, '850-477-6544', 1, 1, '2025-05-25 04:18:50', '2025-05-25 04:18:50'),
                                                                                                                                           (22, 22, 1022, NULL, '850-476-6122', 1, 1, '2025-05-26 14:00:01', '2025-05-26 14:00:01'),
                                                                                                                                           (23, 23, 1023, NULL, '319-393-9595', 1, 1, '2025-05-30 20:34:42', '2025-05-30 20:34:42'),
                                                                                                                                           (24, 24, 1024, NULL, '319-378-6924', 1, 1, '2025-05-31 01:31:03', '2025-05-31 01:31:03'),
                                                                                                                                           (25, 25, 1025, NULL, '765-446-1946', 1, 1, '2025-05-31 01:32:04', '2025-05-31 01:32:04'),
                                                                                                                                           (26, 26, 1026, NULL, '765-447-6915', 1, 1, '2025-05-31 01:32:33', '2025-05-31 01:32:33'),
                                                                                                                                           (27, 27, 1027, NULL, '765-449-1790', 1, 1, '2025-05-31 01:33:03', '2025-05-31 01:33:03'),
                                                                                                                                           (28, 28, 1028, NULL, '765-446-0381', 1, 1, '2025-05-31 01:34:10', '2025-05-31 01:34:10'),
                                                                                                                                           (29, 29, 1029, NULL, '765-448-1900', 1, 1, '2025-05-31 01:34:42', '2025-05-31 01:34:42'),
                                                                                                                                           (30, 30, 1030, NULL, '765-447-7700', 1, 1, '2025-05-31 01:35:21', '2025-05-31 01:35:21'),
                                                                                                                                           (31, 31, 1031, NULL, '319-294-4802', 1, 1, '2025-06-02 23:02:19', '2025-06-02 23:02:19'),
                                                                                                                                           (32, 32, 1032, NULL, '319-396-3651', 1, 1, '2025-06-02 23:03:21', '2025-06-02 23:03:21'),
                                                                                                                                           (33, 33, 1033, NULL, '641-753-1230', 1, 1, '2025-06-02 23:50:03', '2025-06-02 23:50:03'),
                                                                                                                                           (34, 34, 1034, NULL, '515-440-0698', 1, 1, '2025-06-02 23:51:39', '2025-06-02 23:51:39'),
                                                                                                                                           (35, 35, 1035, NULL, '779-238-3618', 1, 1, '2025-06-03 00:46:18', '2025-06-03 00:46:18'),
                                                                                                                                           (36, 36, 1036, NULL, '541-782-8477', 1, 1, '2025-06-03 01:37:15', '2025-06-03 01:37:15'),
                                                                                                                                           (37, 37, 1037, NULL, '541-782-2084', 1, 1, '2025-06-03 01:39:00', '2025-06-03 01:39:00'),
                                                                                                                                           (38, 38, 1038, NULL, '319-366-7756', 1, 1, '2025-09-24 17:05:15', '2025-09-24 17:05:15'),
                                                                                                                                           (39, 39, 1039, NULL, '319-395-9212', 1, 1, '2025-10-07 16:39:41', '2025-10-07 16:39:41'),
                                                                                                                                           (40, 40, 1040, NULL, '319-294-6999', 1, 1, '2025-10-24 22:43:16', '2025-10-24 22:43:16'),
                                                                                                                                           (41, 41, 1041, NULL, '319-393-8942', 1, 1, '2025-10-24 22:53:53', '2025-10-24 22:53:53'),
                                                                                                                                           (42, 42, 1042, NULL, '319-393-3529', 1, 1, '2025-10-24 23:22:46', '2025-10-24 23:22:46');

-- =====================================================================
-- PATRIOT THANKS: INCENTIVES
-- =====================================================================

INSERT IGNORE INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active, submitted_by_user_id, created_at, updated_at) VALUES
                                                                                                                                                                                                 (1, 1, 'Free Meal for Veterans', 'Free meal on Veterans day.', NULL, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-14 02:20:52', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (2, 5, '10% Discount for Veterans', '10% off for eligible purchases. Must be registered and verified. Go to https://www.homedepot.com/c/military for more information and to sign up.', 10, '2024-01-01', '2026-12-31', 'Registration required', 1, 6, '2025-04-20 03:18:42', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (3, 6, 'Free Meal for Veterans', 'Free select meal on Veterans day or receive a coupon for a free select meal available anytime from Veterans day until May 31st of the following year.', NULL, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-22 14:59:20', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (4, 11, 'Free Meal for Veterans', 'Free High Fives', NULL, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-27 00:08:20', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (5, 4, '10% Discount for Veterans', '10% off everyday.', 10, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-29 00:11:58', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (6, 4, 'Free Meal for Veterans', 'Free Meal on Veterans Day!', NULL, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-29 00:14:57', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (7, 7, '10% Discount for Veterans', '10% off of parts and labor for Veterans', 10, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-29 00:24:15', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (8, 8, '10% Discount for Veterans', 'Discounts vary from store to store.  Most stores offer 10% when asked, others require you subscribe to a veteran service called wesalute.com.  Check store for details.', 10, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-04-29 00:31:33', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (9, 3, 'Special Offer', '$0.10 fuel saver reward for anyone that mentions this incentive. Only available at this location.', NULL, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-05-02 00:00:10', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (10, 3, '15% Discount for Veterans', 'This is a test incentive', 15, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-09-29 13:57:27', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (11, 39, '10% Discount for Veterans', '10% off when asked about this incentive. ID required.', 10, '2024-01-01', '2026-12-31', 'Military ID or valid identification', 1, 6, '2025-10-07 16:45:54', '2025-11-08 19:43:54'),
                                                                                                                                                                                                 (12, 43, '10% Discount for Veterans, Active Duty, and First Responders', '10% discount. Must be asked for at time of purchase.', 10, '2024-01-01', '2026-12-31', NULL, 1, 6, '2025-11-23 23:38:48', '2025-11-23 23:38:48');

-- =====================================================================
-- PATRIOT THANKS: BUSINESS-INCENTIVE-TYPES JUNCTION
-- =====================================================================

INSERT IGNORE INTO business_incentive_types (incentive_id, incentive_type_id) VALUES
                                                                                (1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1),
                                                                                (9, 5), (10, 1), (11, 1), (12, 1), (12, 2), (12, 3);

-- =====================================================================
-- PATRIOT USERS: ROLES (for the separate auth system)
-- =====================================================================

INSERT IGNORE INTO patriot_roles (name, description) VALUES
                                                       ('VETERAN', 'Veteran of the uniformed services'),
                                                       ('ACTIVE_DUTY', 'Active duty service member'),
                                                       ('FIRST_RESPONDER', 'Fire, police, or EMS personnel'),
                                                       ('MILITARY_SPOUSE', 'Spouse of a veteran or service member'),
                                                       ('BUSINESS_OWNER', 'Business owner offering incentives'),
                                                       ('SUPPORTER', 'Supporter of veterans and service members'),
                                                       ('PLATFORM_ADMIN', 'Patriot Thanks platform administrator');

SET FOREIGN_KEY_CHECKS = 1;
