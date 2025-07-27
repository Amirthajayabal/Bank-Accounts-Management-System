CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

CREATE TABLE IF NOT EXISTS accounts (
    acc_no INT PRIMARY KEY,
    name VARCHAR(100),
    balance DOUBLE
);