INSERT INTO patients (patient_name, dob, gender, wardNumber, Ward, patientId) VALUES 
('Hemang', '2005-05-15', 'M', 101, 'general Medicine', 1),
('Alice', '2006-03-20', 'F', 102, 'pediatrics', 2),
('Michal', '2004-07-10', 'M', 110, 'ENT', 3),
('Praveen', '2005-11-25', 'F', 103, 'optical', 4),
('Abhi', '2006-01-08', 'M', 99, 'dermatology', 5),
('Ankit', '2005-08-30', 'F', 101, 'general Medicine', 6),
('Priya', '2004-12-12', 'M', 108, 'heart', 7),
('Harry', '2006-06-05', 'F', 102, 'pediatrics', 8),
('Denis', '2005-09-18', 'M', 110, 'ENT', 9),
('Raju', '2004-04-03', 'M', 110, 'ENT', 10);

INSERT INTO doctors (doctor_name, dob, docId, salary, gender) VALUES 
('Farhan', '1980-05-15', 1001, 50000.00, 'M'),
('Laxmi', '1985-09-20', 1002, 48000.00, 'F'),
('Ripunjay', '1975-07-10', 1003, 55000.00, 'M'),
('Madhav', '1982-11-25', 1004, 52000.00, 'F'),
('Johnny', '1988-01-08', 1005, 49000.00, 'M'),
('Francis', '1979-06-20', 1006, 53000.00, 'F');


INSERT INTO medicine (mid, name) VALUES 
(91, 'Paracetamol'),
(92, 'Amoxicillin'),
(93, 'Ibuprofen'),
(94, 'Aspirin'),
(95, 'Omeprazole'),
(96, 'Lisinopril'),
(97, 'Crocin'),
(98, 'Herion'),
(99, 'Montelucast'),
(100, 'Disprin');

INSERT INTO medication (pid, docid, mid) VALUES 
(1, 1001, 91),  -- Hemang treated by Farhan with Paracetamol (mid: 1)
(2, 1002, 92),  -- Alice treated by Laxmi with Amoxicillin (mid: 2)
(3, 1003, 93),  -- Michal treated by Ripunjay with Ibuprofen (mid: 3)
(4, 1004, 94),  -- Praveen treated by Madhav with Aspirin (mid: 4)
(5, 1005, 95),  -- Abhi treated by Johnny with Omeprazole (mid: 5)
(6, 1006, 96);  -- Ankit treated by Francis with Lisinopril (mid: 6)

INSERT INTO room (roomNumber, patId) VALUES
(101, 1), -- Room 101 occupied by Hemang
(102, 2), -- Room 102 occupied by Alice
(103, 3), -- Room 103 occupied by Micahal
(104, 4), -- Room 104 vacant
(105, 5); -- Room 105 vacant
