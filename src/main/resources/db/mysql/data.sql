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
-- ATHLEAGUES DATA (Professor's original - unchanged except status_id
-- added to users inserts per User.java entity requirement)
-- =====================================================================

INSERT INTO roles (name, description) VALUES
                                        ('SCHOOL_ADMIN', 'Rec Center Admin: Can manage facilities, leagues, scores, and users.'),
                                        ('STUDENT', 'Student: Can join leagues, create teams, and view schedules.');

INSERT INTO permissions (name, description) VALUES
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

-- Student permissions (role_id=2)
INSERT INTO permission_role (permission_id, role_id) VALUES (1, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (2, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (3, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (4, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (5, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (6, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (7, 2);
INSERT INTO permission_role (permission_id, role_id) VALUES (8, 2);

-- School Admin permissions (role_id=1, all permissions)
INSERT INTO permission_role (permission_id, role_id) VALUES (1, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (2, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (3, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (4, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (5, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (6, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (7, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (8, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (9, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (10, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (11, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (12, 1);
INSERT INTO permission_role (permission_id, role_id) VALUES (13, 1);

-- Professor's users (status_id=6 means Supporter - a safe default)
INSERT INTO users (first_name, last_name, email, password_hash) VALUES
  ('Brett', 'School Admin', 'brett.baumgart@kirkwood.edu', 'hashed_password_for_brett');
INSERT INTO users (first_name, last_name, email, password_hash) VALUES
  ('Alex', 'Student', 'alex.student@student.kirkwood.edu', 'hashed_password_for_alex');


INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

INSERT INTO schools (name, domain) VALUES
                                                ('Kirkwood Community College', 'kirkwood.edu'),
                                                ('University of Iowa', 'uiowa.edu'),
                                                ('Iowa State University', 'iastate.edu'),
                                                ('University of Northern Iowa', 'uni.edu'),
                                                ('Coe College', 'coe.edu'),
                                                ('Mount Mercy University', 'mtmercy.edu'),
                                                ('Drake University', 'drake.edu'),
                                                ('Grinnell College', 'grinnell.edu'),
                                                ('Luther College', 'luther.edu'),
                                                ('Simpson College', 'simpson.edu'),
                                                ('Wartburg College', 'wartburg.edu'),
                                                ('Cornell College', 'cornellcollege.edu'),
                                                ('Loras College', 'loras.edu'),
                                                ('Clarke University', 'clarke.edu'),
                                                ('St. Ambrose University', 'sau.edu');

INSERT INTO locations (school_id, name, description, address) VALUES
  (1, 'Main Campus', 'The primary campus in Cedar Rapids', '6301 Kirkwood Blvd SW, Cedar Rapids, IA');
INSERT INTO locations (school_id, name, description, address) VALUES
  (2, 'Carver-Hawkeye Arena', 'Main sports arena', '1 Elliott Dr, Iowa City, IA');

INSERT INTO locations (school_id, parent_location_id, name, description) VALUES
                                                                                      (1, 1, 'Michael J Gould Rec Center', 'Student recreation facility'),
                                                                                      (1, 1, 'Johnson Hall', 'Athletics building and gymnasium');
INSERT INTO locations (school_id, parent_location_id, name, description) VALUES
                                                                                      (2, 2, 'Main Court', 'The primary basketball court'),
                                                                                      (2, 2, 'Weight Room', 'Athlete training facility');
INSERT INTO locations (school_id, parent_location_id, name, description) VALUES
                                                                                      (1, 3, 'Basketball Court 1', 'North court'),
                                                                                      (1, 3, 'Basketball Court 2', 'South court');

-- =====================================================================
-- PATRIOT THANKS: Lookup Data
-- =====================================================================

INSERT INTO statuses (id, name, description) VALUES (1, 'Veteran', 'A veteran of the uniformed services.');
INSERT INTO statuses (id, name, description) VALUES (2, 'Active Duty', 'An active duty member of the uniformed services.');
INSERT INTO statuses (id, name, description) VALUES (3, 'First Responder', 'An active Fire, Police, or Emergency personnel');
INSERT INTO statuses (id, name, description) VALUES (4, 'Spouse', 'A spouse of a veteran, military, or first responder.');
INSERT INTO statuses (id, name, description) VALUES (5, 'Business Owner', 'A business owner');
INSERT INTO statuses (id, name, description) VALUES (6, 'Supporter', 'A person that supports veterans, military, and first responders.');

INSERT INTO titles (id, name, description, display_order) VALUES (1, '', 'No Title Chosen', 7);
INSERT INTO titles (id, name, description, display_order) VALUES (2, 'Mr.', 'The title of Mister for either married or unmarried men.', 2);
INSERT INTO titles (id, name, description, display_order) VALUES (3, 'Mrs.', 'Mrs. is traditionally used for married women.', 3);
INSERT INTO titles (id, name, description, display_order) VALUES (4, 'Ms.', 'Ms. is a title that can be used for women regardless of their marital status, or when that status is unknown.', 4);
INSERT INTO titles (id, name, description, display_order) VALUES (5, 'Miss', 'Miss is a title traditionally used for unmarried women, particularly younger women or girls.', 5);
INSERT INTO titles (id, name, description, display_order) VALUES (6, 'Mx.', 'Mx is a gender-neutral title that is commonly used by non-binary people.', 6);
INSERT INTO titles (id, name, description, display_order) VALUES (7, 'Dr.', 'Dr. (Doctor) is an academic title used for individuals who have earned a doctoral degree.', 1);

INSERT INTO us_states (id, code, name) VALUES
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
-- PATRIOT THANKS: Business Types (25 categories)
-- =====================================================================

INSERT INTO business_types (id, name, description, display_order, is_active) VALUES
                                                                               (1, 'Automotive', 'Vehicles, parts, repairs, and transportation services.', 1, TRUE),
                                                                               (2, 'Beauty', 'Cosmetics, skincare, salon services, and personal care products.', 2, TRUE),
                                                                               (3, 'Bookstore', 'Books, magazines, educational materials, and reading accessories.', 3, TRUE),
                                                                               (4, 'Clothing', 'Apparel, footwear, accessories, and fashion items.', 4, TRUE),
                                                                               (5, 'Convenience Store', 'Quick-access groceries, snacks, beverages, and everyday essentials.', 5, TRUE),
                                                                               (6, 'Department Store', 'Multi-category retail offering clothing, home goods, and general merchandise.', 6, TRUE),
                                                                               (7, 'Electronics', 'Consumer electronics, appliances, and electronic accessories.', 7, TRUE),
                                                                               (8, 'Entertainment', 'Media, events, performances, and recreational activities.', 8, TRUE),
                                                                               (9, 'Furniture', 'Home and office furniture, fixtures, and decor items.', 9, TRUE),
                                                                               (10, 'Fuel Station', 'Gasoline, diesel, vehicle fluids, and convenience items.', 10, TRUE),
                                                                               (11, 'Gift Shop', 'Gifts, cards, novelties, and specialty presentation items.', 11, TRUE),
                                                                               (12, 'Grocery', 'Food, beverages, household supplies, and perishable goods.', 12, TRUE),
                                                                               (13, 'Hardware', 'Tools, building materials, and home improvement supplies.', 13, TRUE),
                                                                               (14, 'Health', 'Health services, medical supplies, and wellness products.', 14, TRUE),
                                                                               (15, 'Hotel/Motel', 'Lodging, accommodations, and hospitality services.', 15, TRUE),
                                                                               (16, 'Jewelry', 'Fine jewelry, watches, precious metals, and gemstones.', 16, TRUE),
                                                                               (17, 'Other', 'Miscellaneous businesses not fitting standard categories.', 17, TRUE),
                                                                               (18, 'Pharmacy', 'Prescription medications, over-the-counter drugs, and health consultations.', 18, TRUE),
                                                                               (19, 'Restaurant', 'Food preparation, dining services, and culinary experiences.', 19, TRUE),
                                                                               (20, 'Retail', 'General consumer goods sold directly to customers.', 20, TRUE),
                                                                               (21, 'Service', 'Professional services, repairs, and skilled labor.', 21, TRUE),
                                                                               (22, 'Specialty', 'Niche products and specialized goods for specific markets.', 22, TRUE),
                                                                               (23, 'Sporting Goods', 'Athletic equipment, sportswear, and recreational gear.', 23, TRUE),
                                                                               (24, 'Technology', 'Computer hardware, software, IT services, and digital solutions.', 24, TRUE),
                                                                               (25, 'Toys', 'Children''s toys, games, educational play items, and hobbies.', 25, TRUE);

-- =====================================================================
-- PATRIOT THANKS: Incentive Types
-- =====================================================================

INSERT INTO incentive_types (id, name, description, display_order, is_active) VALUES
                                                                                (1, 'Veteran', 'Veterans special pricing and benefits.', 1, TRUE),
                                                                                (2, 'Active Duty', 'Active duty military personnel discount.', 2, TRUE),
                                                                                (3, 'First Responder', 'Discounts for emergency services professionals.', 3, TRUE),
                                                                                (4, 'Spouse', 'Benefits extended to military/responder spouses.', 4, TRUE),
                                                                                (5, 'Other', 'Alternative special discounts or promotional offers.', 5, TRUE);

-- =====================================================================
-- PATRIOT THANKS: Businesses (43 - mirrors MySQL production data)
-- submitted_by_user_id references are omitted here because the
-- professor's users table has different IDs than the MySQL production
-- database. Set to NULL so FK constraints are satisfied.
-- =====================================================================

INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (1, 'Olive Garden', 'olive-garden', 19, 'Italian restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (2, 'Taco Bell', 'taco-bell', 19, 'Fast food Mexican restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (3, 'Hy-Vee Fast & Fresh', 'hy-vee-fast-and-fresh', 5, 'Convenience store and gas station', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (4, 'Red Lobster', 'red-lobster', 19, 'Seafood restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (5, 'The Home Depot', 'the-home-depot', 13, 'Home improvement retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (6, 'Texas Roadhouse', 'texas-roadhouse', 19, 'Steakhouse restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (7, 'Milex Complete Car Care', 'milex-complete-car-care', 1, 'Automotive service and repair', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (8, 'Wendy''s', 'wendys', 19, 'Fast food hamburger restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (9, 'El Viejo Mexican Restaurant', 'el-viejo-mexican-restaurant', 19, 'Authentic Mexican restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (10, 'China Inn Restaurant', 'china-inn-restaurant', 19, 'Chinese cuisine restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (11, 'Kirkwood Community College', 'kirkwood-community-college', 17, 'Educational institution', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (12, 'Taco Bell Marion', 'taco-bell-marion', 19, 'Fast food Mexican restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (13, 'Biaggi''s Ristorante Italiano', 'biaggis-ristorante-italiano', 19, 'Italian fine dining restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (14, 'Taco John''s', 'taco-johns', 19, 'Fast food Mexican restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (15, 'Perkins American Food Co.', 'perkins-american-food-co', 19, 'Family dining restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (16, 'Culver''s', 'culvers', 19, 'Fast casual restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (17, 'The Home Depot Bronx', 'the-home-depot-bronx', 13, 'Home improvement retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (18, 'The Home Depot Las Vegas', 'the-home-depot-las-vegas', 13, 'Home improvement retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (19, 'Olive Garden Las Vegas', 'olive-garden-las-vegas', 19, 'Italian restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (20, 'The Home Depot Henderson', 'the-home-depot-henderson', 13, 'Home improvement retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (21, 'Olive Garden Pensacola', 'olive-garden-pensacola', 19, 'Italian restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (22, 'Taylor''s Breakfast and Lunch', 'taylors-breakfast-and-lunch', 19, 'Breakfast and lunch restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (23, 'Applebee''s Grill + Bar', 'applebees-grill-and-bar', 19, 'Casual dining restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (24, 'Red Robin Gourmet Burgers', 'red-robin-gourmet-burgers', 19, 'Burger restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (25, 'The Home Depot Lafayette', 'the-home-depot-lafayette', 13, 'Home improvement retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (26, 'Golden Corral Buffet & Grill', 'golden-corral-buffet-and-grill', 19, 'Buffet restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (27, 'Outback Steakhouse', 'outback-steakhouse', 19, 'Steakhouse restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (28, 'Red Lobster Lafayette', 'red-lobster-lafayette', 19, 'Seafood restaurant chain', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (29, 'Lowe''s Home Improvement', 'lowes-home-improvement', 13, 'Home improvement retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (30, 'Sonic Drive-In', 'sonic-drive-in', 19, 'Fast food drive-in restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (31, 'Shag Cedar Rapids', 'shag-cedar-rapids', 22, 'Specialty carpet store', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (32, 'Shag Wiley Blvd', 'shag-wiley-blvd', 22, 'Specialty carpet store', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (33, 'Shag Marshalltown', 'shag-marshalltown', 22, 'Specialty carpet store', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (34, 'Shag West Des Moines', 'shag-west-des-moines', 22, 'Specialty carpet store', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (35, 'Raising Cane''s Chicken Fingers', 'raising-canes-chicken-fingers', 19, 'Fast food chicken restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (36, 'Holt Adventure Co.', 'holt-adventure-co', 22, 'Outdoor adventure specialty store', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (37, 'Dairy Queen Grill & Chill', 'dairy-queen-grill-and-chill', 19, 'Fast food ice cream restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (38, 'Hy-Vee Grocery Store', 'hy-vee-grocery-store', 12, 'Full service grocery store', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (39, 'OfficeMax', 'officemax', 22, 'Office supplies retailer', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (40, 'Taste Of India', 'taste-of-india', 19, 'Indian cuisine restaurant', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (41, 'Marcus Cedar Rapids Cinema', 'marcus-cedar-rapids-cinema', 8, 'Movie theater', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (42, 'The Grand White Rabbit Tea Co', 'the-grand-white-rabbit-tea-co', 19, 'Tea house and cafe', TRUE, TRUE);
INSERT INTO businesses (id, name, slug, business_type_id, description, is_verified, is_active) VALUES (43, 'Pak Mail', 'pak-mail', 22, 'Mail and Postal Services', TRUE, TRUE);

-- =====================================================================
-- PATRIOT THANKS: Addresses
-- =====================================================================

INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1001, '367 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1002, '750 Marion Blvd', NULL, 'Marion', 15, '52302');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1003, '3935 Blairs Ferry Rd', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1004, '163 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1005, '4501 1st Ave SE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1006, '2425 Edgewood Rd SW', NULL, 'Cedar Rapids', 15, '52404');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1007, '2010 Sylvia Ave NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1008, '190 Collins Rd NE', 'By Hobby Lobby', 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1009, '90 Twixt Town Rd', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1010, '1401 1st Ave SE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1011, '6301 Kirkwood Blvd SW', NULL, 'Cedar Rapids', 15, '52404');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1012, '3045 Williams Blvd SW', 'Next to SSA', 'Cedar Rapids', 15, '52404');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1013, '320 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1014, '223 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1015, '315 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1016, '2405 Edgewood Rd SW', NULL, 'Cedar Rapids', 15, '52404');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1017, '2560 Bruckner Blvd', NULL, 'New York', 32, '10465');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1018, '6025 S Pecos Rd', NULL, 'Las Vegas', 28, '89120');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1019, '1545 E Flamingo Rd', NULL, 'Las Vegas', 28, '89119');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1020, '1030 W Sunset Rd', NULL, 'Henderson', 28, '89014');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1021, '5037 Bayou Blvd', NULL, 'Pensacola', 9, '32503');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1022, '7175 N Davis Hwy', NULL, 'Pensacola', 9, '32504');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1023, '303 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1024, '4625 1st Ave SE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1025, '311 Sagamore Pkwy N', NULL, 'Lafayette', 14, '47904');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1026, '79 Shenandoah Dr', NULL, 'Lafayette', 14, '47905');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1027, '3660 State Road 26', NULL, 'Lafayette', 14, '47905');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1028, '120 S Creasy Ln', NULL, 'Lafayette', 14, '47905');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1029, '100 N Creasy Ln', NULL, 'Lafayette', 14, '47905');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1030, '150 S Creasy Ln', NULL, 'Lafayette', 14, '47905');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1031, '4444 1st Ave NE Suite #66', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1032, '3005 Wiley Blvd SW #115', NULL, 'Cedar Rapids', 15, '52404');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1033, '2501 S Center St Ste. B', NULL, 'Marshalltown', 15, '50158');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1034, '2600 University Ave #200', NULL, 'West Des Moines', 15, '50266');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1035, '230 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1036, '48333 E 1st St', NULL, 'Oakridge', 37, '97463');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1037, '47720 OR-58', NULL, 'Oakridge', 37, '97463');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1038, '3235 Oakland Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1039, '327 Collins Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1040, '1060 Old Marion Rd NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1041, '5340 Council St NE', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1042, '4444 1st Ave NE Kiosk 3519', NULL, 'Cedar Rapids', 15, '52402');
INSERT INTO addresses (id, street_address, address_line_2, city, state_id, zip_code) VALUES (1043, '5249 N Park Place NE', NULL, 'Cedar Rapids', 15, '52402');

-- =====================================================================
-- PATRIOT THANKS: Business Locations
-- =====================================================================

INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (1, 1, 1001, NULL, '319-378-6401', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (2, 2, 1002, NULL, '319-377-8747', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (3, 3, 1003, NULL, '319-378-0684', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (4, 4, 1004, NULL, '319-395-0450', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (5, 5, 1005, NULL, '319-294-0480', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (6, 6, 1006, NULL, '319-396-3300', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (7, 7, 1007, NULL, '319-382-0090', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (8, 8, 1008, 'By Hobby Lobby', '319-373-4188', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (9, 9, 1009, NULL, '319-200-4447', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (10, 10, 1010, NULL, '319-247-1888', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (11, 11, 1011, NULL, '319-999-9999', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (12, 12, 1012, 'Next to SSA', '319-366-0607', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (13, 13, 1013, NULL, '319-393-6593', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (14, 14, 1014, NULL, '319-450-0532', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (15, 15, 1015, NULL, '319-382-2122', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (16, 16, 1016, NULL, '319-390-2061', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (17, 17, 1017, NULL, '718-828-1071', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (18, 18, 1018, NULL, '702-434-1948', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (19, 19, 1019, NULL, '702-735-0082', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (20, 20, 1020, NULL, '702-435-9200', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (21, 21, 1021, NULL, '850-477-6544', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (22, 22, 1022, NULL, '850-476-6122', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (23, 23, 1023, NULL, '319-393-9595', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (24, 24, 1024, NULL, '319-378-6924', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (25, 25, 1025, NULL, '765-446-1946', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (26, 26, 1026, NULL, '765-447-6915', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (27, 27, 1027, NULL, '765-449-1790', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (28, 28, 1028, NULL, '765-446-0381', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (29, 29, 1029, NULL, '765-448-1900', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (30, 30, 1030, NULL, '765-447-7700', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (31, 31, 1031, NULL, '319-294-4802', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (32, 32, 1032, NULL, '319-396-3651', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (33, 33, 1033, NULL, '641-753-1230', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (34, 34, 1034, NULL, '515-440-0698', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (35, 35, 1035, NULL, '779-238-3618', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (36, 36, 1036, NULL, '541-782-8477', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (37, 37, 1037, NULL, '541-782-2084', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (38, 38, 1038, NULL, '319-366-7756', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (39, 39, 1039, NULL, '319-395-9212', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (40, 40, 1040, NULL, '319-294-6999', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (41, 41, 1041, NULL, '319-393-8942', TRUE, TRUE);
INSERT INTO business_locations (id, business_id, address_id, location_name, phone, is_primary, is_active) VALUES (42, 42, 1042, NULL, '319-393-3529', TRUE, TRUE);

-- =====================================================================
-- PATRIOT THANKS: Incentives
-- =====================================================================

INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (1, 1, 'Free Meal for Veterans', 'Free meal on Veterans day.', NULL, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (2, 5, '10% Discount for Veterans', '10% off for eligible purchases. Must be registered and verified.', 10, '2024-01-01', '2026-12-31', 'Registration required', TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (3, 6, 'Free Meal for Veterans', 'Free select meal on Veterans day or receive a coupon for a free select meal.', NULL, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (4, 11, 'Free Meal for Veterans', 'Free High Fives', NULL, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (5, 4, '10% Discount for Veterans', '10% off everyday.', 10, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (6, 4, 'Free Meal for Veterans', 'Free Meal on Veterans Day!', NULL, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (7, 7, '10% Discount for Veterans', '10% off of parts and labor for Veterans', 10, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (8, 8, '10% Discount for Veterans', 'Discounts vary from store to store.', 10, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (9, 3, 'Special Offer', '$0.10 fuel saver reward for anyone that mentions this incentive.', NULL, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (10, 3, '15% Discount for Veterans', 'This is a test incentive', 15, '2024-01-01', '2026-12-31', NULL, TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (11, 39, '10% Discount for Veterans', '10% off when asked about this incentive. ID required.', 10, '2024-01-01', '2026-12-31', 'Military ID or valid identification', TRUE);
INSERT INTO incentives (id, business_id, title, description, discount_percentage, start_date, end_date, verification_required, is_active) VALUES (12, 43, '10% Discount for Veterans, Active Duty, and First Responders', '10% discount. Must be asked for at time of purchase.', 10, '2024-01-01', '2026-12-31', NULL, TRUE);

INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (1, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (2, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (3, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (4, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (5, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (6, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (7, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (8, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (9, 5);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (10, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (11, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (12, 1);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (12, 2);
INSERT INTO business_incentive_types (incentive_id, incentive_type_id) VALUES (12, 3);

-- =====================================================================
-- PATRIOT THANKS: Patriot Auth Roles
-- (Matches the roles from the signup page service types)
-- =====================================================================

INSERT INTO patriot_roles (name, description) VALUES ('VETERAN', 'Veteran of the uniformed services');
INSERT INTO patriot_roles (name, description) VALUES ('ACTIVE_DUTY', 'Active duty service member');
INSERT INTO patriot_roles (name, description) VALUES ('FIRST_RESPONDER', 'Fire, police, or EMS personnel');
INSERT INTO patriot_roles (name, description) VALUES ('MILITARY_SPOUSE', 'Spouse of a veteran or service member');
INSERT INTO patriot_roles (name, description) VALUES ('BUSINESS_OWNER', 'Business owner offering incentives');
INSERT INTO patriot_roles (name, description) VALUES ('SUPPORTER', 'Supporter of veterans and service members');
INSERT INTO patriot_roles (name, description) VALUES ('PLATFORM_ADMIN', 'Patriot Thanks platform administrator');

-- =====================================================================
-- PATRIOT THANKS: Sample Patriot Users (password: Password1)
-- BCrypt hash for "Password1": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- status_id values: 1=Veteran, 2=Active Duty, 3=First Responder,
--                   4=Spouse, 5=Business Owner, 6=Supporter
-- =====================================================================

INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (1, 'Edward', 'McKeown', 'edward@doctormckeown.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 1, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (2, 'Admin', 'User', 'admin@patriotthanks.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 6, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (3, 'John', 'Veteran', 'john.veteran@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 1, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (4, 'Jane', 'ActiveDuty', 'jane.active@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 2, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (5, 'Mike', 'Firefighter', 'mike.fire@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 3, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (6, 'Sarah', 'Business', 'sarah.biz@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 5, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (7, 'Tom', 'Supporter', 'tom.support@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 6, TRUE);
INSERT INTO patriot_users (id, first_name, last_name, email, password_hash, phone, status_id, email_verified) VALUES
  (8, 'Guest', 'User', 'guest@patriotthanks.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL, 6, FALSE);

-- patriot_user_roles: role IDs match patriot_roles insert order
-- 1=VETERAN, 2=ACTIVE_DUTY, 3=FIRST_RESPONDER, 4=MILITARY_SPOUSE,
-- 5=BUSINESS_OWNER, 6=SUPPORTER, 7=PLATFORM_ADMIN
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (1, 7);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (2, 7);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (3, 1);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (4, 2);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (5, 3);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (6, 5);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (7, 6);
INSERT INTO patriot_user_roles (patriot_user_id, patriot_role_id) VALUES (8, 6);


SET FOREIGN_KEY_CHECKS = 1;
