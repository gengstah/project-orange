INSERT INTO WORK_EXPERIENCE(WORK_EXPERIENCE_ID, NAME) VALUES(1, 'Skin White');
INSERT INTO WORK_EXPERIENCE(WORK_EXPERIENCE_ID, NAME) VALUES(2, 'Johnny Walker');
INSERT INTO WORK_EXPERIENCE(WORK_EXPERIENCE_ID, NAME) VALUES(3, 'Jack Daniels');
INSERT INTO WORK_EXPERIENCE(WORK_EXPERIENCE_ID, NAME) VALUES(4, 'Tanduay');
INSERT INTO WORK_EXPERIENCE(WORK_EXPERIENCE_ID, NAME) VALUES(5, 'Coors Light');
INSERT INTO HIBERNATE_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_HI_VALUE) VALUES('WORK_EXPERIENCE', 5);

INSERT INTO TALENT(TALENT_ID, FIRSTNAME, LASTNAME, GENDER, BIRTHDATE, FEET, INCHES, WEIGHT, CONTACT, CITY, EXPECTED_SALARY, VITAL1, VITAL2, VITAL3, TALENT_STATUS, ADMIN_NOTE) VALUES(1, 'Mahha', 'Saleh', 'F', DATE '1983-08-13', 5, 3, 70.235, '09274116475', 'Manila', 2543.215, 44, 44, 44, 'FOR_APPROVAL', 'Your account is still for approval. Please allow us to process your registration within 24-48 hours. You can still update your profile whenever you wish.');
INSERT INTO HIBERNATE_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_HI_VALUE) VALUES('TALENT', 1);

INSERT INTO AGENCY(AGENCY_ID, FIRSTNAME, LASTNAME, CONTACT, AGENCY_NAME, AGENCY_ADDRESS, AGENCY_STATUS, ADMIN_NOTE) VALUES(1, 'Angelo Patrick', 'Angni', '0922-222-2222', 'Sabalakatoy Enterprise', '841 Norma St., B. Balic, Sampaloc, Manila, 1008', 'FOR_APPROVAL', 'Your account is still for approval. Please allow us to process your registration within 24-48 hours. You can still update your profile whenever you wish.');
INSERT INTO HIBERNATE_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_HI_VALUE) VALUES('AGENCY', 1);

INSERT INTO APP_USER(USER_ID, EMAIL, PASSWORD, USER_ROLE, USER_STATUS, TALENT_ID, DATE_CREATED) VALUES(1, 'mhaj_saleh@yahoo.com', '$2a$11$XSB0vvR1nqiXpDZ9BjwdrO97kKkB2zoocGYq1b0n.Y0aC0TDxcJvG', 'ROLE_USER', 'ACTIVE', 1, now());
INSERT INTO APP_USER(USER_ID, EMAIL, PASSWORD, USER_ROLE, USER_STATUS, DATE_CREATED) VALUES(2, 'gerardpdelasarmas@gmail.com', '$2a$11$XSB0vvR1nqiXpDZ9BjwdrO97kKkB2zoocGYq1b0n.Y0aC0TDxcJvG', 'ROLE_ADMIN', 'ACTIVE', now());
INSERT INTO APP_USER(USER_ID, EMAIL, PASSWORD, USER_ROLE, USER_STATUS, AGENCY_ID, DATE_CREATED) VALUES(3, 'angelopatrickangni1021@gmail.com', '$2a$11$XSB0vvR1nqiXpDZ9BjwdrO97kKkB2zoocGYq1b0n.Y0aC0TDxcJvG', 'ROLE_AGENCY', 'ACTIVE', 1, now());
INSERT INTO HIBERNATE_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_HI_VALUE) VALUES('APP_USER', 3);

INSERT INTO TALENT_WORK_EXPERIENCE(TALENT_ID, WORK_EXPERIENCE_ID) VALUES(1, 1);
INSERT INTO TALENT_WORK_EXPERIENCE(TALENT_ID, WORK_EXPERIENCE_ID) VALUES(1, 2);
INSERT INTO TALENT_WORK_EXPERIENCE(TALENT_ID, WORK_EXPERIENCE_ID) VALUES(1, 3);
INSERT INTO TALENT_WORK_EXPERIENCE(TALENT_ID, WORK_EXPERIENCE_ID) VALUES(1, 4);
INSERT INTO TALENT_WORK_EXPERIENCE(TALENT_ID, WORK_EXPERIENCE_ID) VALUES(1, 5);

INSERT INTO TALENT_IMAGE(TALENT_IMAGE_ID, FILE_LOCATION, THUMBNAIL_FILE_LOCATION, TALENT_ID) VALUES(1, '/img/talents/mahha-1.jpeg', '/img/talents/thumbnails/mahha-1.jpeg', 1);
INSERT INTO TALENT_IMAGE(TALENT_IMAGE_ID, FILE_LOCATION, THUMBNAIL_FILE_LOCATION, TALENT_ID) VALUES(2, '/img/talents/mahha-2.jpeg', '/img/talents/thumbnails/mahha-2.jpeg', 1);
INSERT INTO HIBERNATE_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_HI_VALUE) VALUES('TALENT_IMAGE', 2);