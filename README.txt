About
This project contains a TestNG-based test suite designed to validate various database 
operations using JDBC. The tests verify database connectivity, data integrity, query execution, 
and error handling.

Tools used:

JDBC
testNG
AventSpart report

IDE:

Eclipse

Test Suite Features

   - Database Connectivity Tests: Verifies successful and unsuccessful connections to the database.
   - Metadata Retrieval: Retrieves and validates table column names and data types.
   - Data Integrity: Checks for non-null data, ascending order of IDs, unique usernames, and email patterns.
   - Data Manipulation: Tests insert and update operations, including validation of duplicates and error handling.

Test cases included.

Test 0: Test successful database connection.
Test 1: Test unsuccessful database connection.
Test 2: Retrieve column names.
Test 3: Retrieve column data types.
Test 4: Check for non-null data.
Test 5: Check ID order in ascending sequence.
Test 6: Check email format.
Test 7: Ensure unique usernames.
Test 8: Add new records to the database.
Test 9: Prevent duplicate usernames.
Test 10: Test incorrect table name error handling.
Test 11: Update existing records.