INSERT INTO BRANCH (city) VALUES ('Prague');
INSERT INTO BRANCH (city) VALUES ('Brno');
INSERT INTO BRANCH (city) VALUES ('Ostrava');
INSERT INTO BRANCH (city) VALUES ('Beroun');
INSERT INTO BRANCH (city) VALUES ('Olomouc');

INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (1, 'Bara', 'Kol', 3, 'Prague', true);
INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (2, 'Jan', 'Novak', 1, 'Brno', false);
INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (3, 'Klara', 'Vlk', 0, 'Prague', false);
INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (4, 'Hana', 'Nejezchlebova', 10, 'Olomouc', true);
INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (5, 'Josef', 'Picha', 1, 'Ostrava', false);
INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (6, 'Michal', 'Schreiber', 8, 'Brno', true);
INSERT INTO EMPLOYEE (employee_id, first_name, last_name, employment_duration_in_years, belongs_to_city, is_manager)
VALUES (7, 'Tereza', 'Vesela', 9, 'Ostrava', true);

INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (1, 'PREMIUM', 'Prague');
INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (2, 'STANDARD', 'Brno');
INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (3, 'BASIC', 'Olomouc');
INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (4, 'BASIC', 'Prague');
INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (5, 'BASIC', 'Prague');
INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (6, 'STANDARD', 'Brno');
INSERT INTO SHARED_DESK (desk_id, equipment, is_located_at_city) VALUES (7, 'BASIC', 'Beroun');

INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-01', 1, 1);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-10', 1, 1);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-08', 2, 5);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-03', 3, 4);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-13', 3, 4);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-13', 4, 3);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-15', 6, 6);
INSERT INTO BOOKING (booking_time, fk_desk, fk_employee) VALUES ('2022-02-16', 5, 3);
