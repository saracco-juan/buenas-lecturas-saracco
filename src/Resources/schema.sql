DROP TABLE IF EXISTS REVIEWS;
DROP TABLE IF EXISTS USER_BOOKS;
DROP TABLE IF EXISTS APP_USER;

CREATE TABLE APP_USER (
                          ID INT PRIMARY KEY AUTO_INCREMENT,
                          NAME VARCHAR(255) NOT NULL,
                          EMAIL VARCHAR(255) NOT NULL UNIQUE,
                          PASSWORD VARCHAR(255) NOT NULL
);

CREATE TABLE USER_BOOKS (
                            ID INT PRIMARY KEY AUTO_INCREMENT,
                            USER_ID INT NOT NULL,
                            BOOK_ISBN VARCHAR(50) NOT NULL,
                            LIST_TYPE VARCHAR(20) NOT NULL,
                            FOREIGN KEY (USER_ID) REFERENCES APP_USER(ID) ON DELETE CASCADE
);

CREATE TABLE REVIEWS (
                         ID INT PRIMARY KEY AUTO_INCREMENT,
                         USER_ID INT NOT NULL,
                         BOOK_ISBN VARCHAR(50) NOT NULL,
                         RATING INT NOT NULL CHECK (RATING >= 1 AND RATING <= 5),
                         COMMENT VARCHAR(1000),
                         CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (USER_ID) REFERENCES APP_USER(ID) ON DELETE CASCADE,
                         CONSTRAINT uk_user_review UNIQUE (USER_ID, BOOK_ISBN)
);