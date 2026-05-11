ALTER TABLE `prode`.`Partidos` 
MODIFY COLUMN `equipoLocal` INT NULL,
MODIFY COLUMN `equipoVisitante` INT NULL;

DELETE FROM `prode`.`jugadas`;
ALTER TABLE `prode`.`jugadas` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Partidos`;
ALTER TABLE `prode`.`Partidos` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Equipos`;
ALTER TABLE `prode`.`Equipos` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Etapas`;
ALTER TABLE `prode`.`Etapas` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Estadios`;
ALTER TABLE `prode`.`Estadios` AUTO_INCREMENT = 1;

-- 3. ESTADIOS (IDs 1 al 16)
INSERT INTO `prode`.`Estadios` (estadio, ciudad, pais) VALUES
('Estadio Azteca', 'CDMX', 'México'), ('Estadio Akron', 'Guadalajara', 'México'), 
('Estadio BBVA', 'Monterrey', 'México'), ('BMO Field', 'Toronto', 'Canadá'),
('BC Place', 'Vancouver', 'Canadá'), ('Mercedes-Benz Stadium', 'Atlanta', 'USA'),
('Gillette Stadium', 'Boston', 'USA'), ('AT&T Stadium', 'Dallas', 'USA'),
('NRG Stadium', 'Houston', 'USA'), ('Arrowhead Stadium', 'Kansas City', 'USA'),
('SoFi Stadium', 'Los Angeles', 'USA'), ('Hard Rock Stadium', 'Miami', 'USA'),
('MetLife Stadium', 'East Rutherford', 'USA'), ('Lincoln Financial Field', 'Filadelfia', 'USA'),
('Levi\'s Stadium', 'Santa Clara', 'USA'), ('Lumen Field', 'Seattle', 'USA');

-- 4. ETAPAS (IDs 1 al 9)
INSERT INTO `prode`.`Etapas` (nombreEtapa) VALUES
('Fase de Grupos - Fecha 1'), ('Fase de Grupos - Fecha 2'), ('Fase de Grupos - Fecha 3'),
('Dieciseisavos de Final'), ('Octavos de Final'), ('Cuartos de Final'),
('Semifinal'), ('Tercer Puesto'), ('Final');

-- 5. EQUIPOS (Los 48 países, IDs 1 al 48)
INSERT INTO `prode`.`Equipos` (nombre, grupo) VALUES
('México', 'A'), ('Sudáfrica', 'A'), ('Corea del Sur', 'A'), ('República Checa', 'A'),
('Canadá', 'B'), ('Bosnia y Herz.', 'B'), ('Qatar', 'B'), ('Suiza', 'B'),
('Uruguay', 'C'), ('Marruecos', 'C'), ('Haití', 'C'), ('Escocia', 'C'),
('Estados Unidos', 'D'), ('Paraguay', 'D'), ('Australia', 'D'), ('Turquía', 'D'),
('Brasil', 'E'), ('Egipto', 'E'), ('Japón', 'E'), ('Polonia', 'E'),
('España', 'F'), ('Nigeria', 'F'), ('Irak', 'F'), ('Gales', 'F'),
('Francia', 'G'), ('Senegal', 'G'), ('Irán', 'G'), ('Rumanía', 'G'),
('Inglaterra', 'H'), ('Ecuador', 'H'), ('Uzbekistán', 'H'), ('Noruega', 'H'),
('Bélgica', 'I'), ('Costa de Marfil', 'I'), ('Panamá', 'I'), ('Ucrania', 'I'),
('Argentina', 'J'), ('Argelia', 'J'), ('Austria', 'J'), ('Jordania', 'J'),
('Portugal', 'K'), ('Camerún', 'K'), ('Costa Rica', 'K'), ('Hungría', 'K'),
('Países Bajos', 'L'), ('Colombia', 'L'), ('Nueva Zelanda', 'L'), ('Serbia', 'L');

INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
-- GRUPO A
(1, 2, 1, 1, '2026-06-11 16:00:00', 0), (3, 4, 1, 2, '2026-06-11 20:00:00', 0),
(1, 3, 1, 3, '2026-06-16 18:00:00', 0), (4, 2, 1, 1, '2026-06-16 21:00:00', 0),
(4, 1, 1, 2, '2026-06-24 17:00:00', 0), (2, 3, 1, 3, '2026-06-24 17:00:00', 0),

-- GRUPO B
(5, 6, 1, 4, '2026-06-12 13:00:00', 0), (7, 8, 1, 5, '2026-06-12 16:00:00', 0),
(5, 7, 1, 4, '2026-06-17 15:00:00', 0), (8, 6, 1, 5, '2026-06-17 19:00:00', 0),
(8, 5, 1, 4, '2026-06-24 20:00:00', 0), (6, 7, 1, 5, '2026-06-24 20:00:00', 0),

-- GRUPO C
(9, 10, 1, 6, '2026-06-13 12:00:00', 0), (11, 12, 1, 7, '2026-06-13 15:00:00', 0),
(9, 11, 1, 14, '2026-06-18 12:00:00', 0), (12, 10, 1, 13, '2026-06-18 15:00:00', 0),
(12, 9, 1, 13, '2026-06-25 14:00:00', 0), (10, 11, 1, 14, '2026-06-25 14:00:00', 0),

-- GRUPO D (Inaugura USA en LA)
(13, 14, 1, 11, '2026-06-12 19:00:00', 0), (15, 16, 1, 15, '2026-06-13 18:00:00', 0),
(13, 15, 1, 16, '2026-06-19 15:00:00', 0), (16, 14, 1, 15, '2026-06-19 19:00:00', 0),
(16, 13, 1, 11, '2026-06-25 18:00:00', 0), (14, 15, 1, 16, '2026-06-25 18:00:00', 0),

-- GRUPO E
(17, 18, 1, 8, '2026-06-14 13:00:00', 0), (19, 20, 1, 9, '2026-06-14 17:00:00', 0),
(17, 19, 1, 8, '2026-06-20 12:00:00', 0), (20, 18, 1, 9, '2026-06-20 16:00:00', 0),
(20, 17, 1, 10, '2026-06-26 13:00:00', 0), (18, 19, 1, 8, '2026-06-26 13:00:00', 0),

-- GRUPO F
(21, 22, 1, 12, '2026-06-14 20:00:00', 0), (23, 24, 1, 13, '2026-06-15 12:00:00', 0),
(21, 23, 1, 12, '2026-06-20 20:00:00', 0), (24, 22, 1, 14, '2026-06-21 12:00:00', 0),
(24, 21, 1, 12, '2026-06-26 17:00:00', 0), (22, 23, 1, 13, '2026-06-26 17:00:00', 0),

-- GRUPO G
(25, 26, 1, 1, '2026-06-15 15:00:00', 0), (27, 28, 1, 10, '2026-06-15 18:00:00', 0),
(25, 27, 1, 11, '2026-06-21 15:00:00', 0), (28, 26, 1, 16, '2026-06-21 18:00:00', 0),
(28, 25, 1, 15, '2026-06-26 20:00:00', 0), (26, 27, 1, 16, '2026-06-26 20:00:00', 0),

-- GRUPO H
(29, 30, 1, 6, '2026-06-16 12:00:00', 0), (31, 32, 1, 7, '2026-06-16 15:00:00', 0),
(29, 31, 1, 13, '2026-06-22 12:00:00', 0), (32, 30, 1, 14, '2026-06-22 15:00:00', 0),
(32, 29, 1, 6, '2026-06-27 13:00:00', 0), (30, 31, 1, 7, '2026-06-27 13:00:00', 0),

-- GRUPO I
(33, 34, 1, 4, '2026-06-17 12:00:00', 0), (35, 36, 1, 5, '2026-06-18 18:00:00', 0),
(33, 35, 1, 15, '2026-06-22 18:00:00', 0), (36, 34, 1, 11, '2026-06-22 21:00:00', 0),
(36, 33, 1, 4, '2026-06-27 16:00:00', 0), (34, 35, 1, 5, '2026-06-27 16:00:00', 0),

-- GRUPO J
(37, 38, 1, 8, '2026-06-19 12:00:00', 0), (39, 40, 1, 9, '2026-06-19 15:00:00', 0),
(37, 39, 1, 10, '2026-06-23 13:00:00', 0), (40, 38, 1, 1, '2026-06-23 16:00:00', 0),
(40, 37, 1, 9, '2026-06-27 19:00:00', 0), (38, 39, 1, 10, '2026-06-27 19:00:00', 0),

-- GRUPO K
(41, 42, 1, 2, '2026-06-20 15:00:00', 0), (43, 44, 1, 3, '2026-06-20 18:00:00', 0),
(41, 43, 1, 6, '2026-06-23 19:00:00', 0), (44, 42, 1, 7, '2026-06-23 22:00:00', 0),
(44, 41, 1, 2, '2026-06-27 21:00:00', 0), (42, 43, 1, 3, '2026-06-27 21:00:00', 0),

-- GRUPO L
(45, 46, 1, 13, '2026-06-21 19:00:00', 0), (47, 48, 1, 12, '2026-06-21 21:00:00', 0),
(45, 47, 1, 15, '2026-06-24 14:00:00', 0), (48, 46, 1, 16, '2026-06-24 14:00:00', 0),
(48, 45, 1, 14, '2026-06-27 23:00:00', 0), (46, 47, 1, 13, '2026-06-27 23:00:00', 0);

-- DIECISEISAVOS DE FINAL (32 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 2, 8, '2026-06-28 14:00:00', 0),
(NULL, NULL, 2, 13, '2026-06-28 18:00:00', 0),
-- ... (repetir hasta completar 32 partidos de esta fase)
(NULL, NULL, 2, 1, '2026-07-03 12:00:00', 0);

-- OCTAVOS DE FINAL (16 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 3, 11, '2026-07-04 15:00:00', 0),
(NULL, NULL, 3, 12, '2026-07-05 15:00:00', 0),
(NULL, NULL, 3, 10, '2026-07-06 18:00:00', 0);

-- CUARTOS DE FINAL (4 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 4, 12, '2026-07-09 13:00:00', 0), -- Miami
(NULL, NULL, 4, 10, '2026-07-10 18:00:00', 0), -- Kansas City
(NULL, NULL, 4, 11, '2026-07-11 13:00:00', 0), -- Los Ángeles
(NULL, NULL, 4, 7, '2026-07-11 18:00:00', 0);  -- Boston

-- SEMIFINALES (2 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 5, 8, '2026-07-14 19:00:00', 0),  -- Dallas
(NULL, NULL, 5, 6, '2026-07-15 19:00:00', 0);  -- Atlanta

-- TERCER PUESTO
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 6, 12, '2026-07-18 14:00:00', 0); -- Miami

-- GRAN FINAL
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 7, 13, '2026-07-19 14:00:00', 0); -- MetLife Stadium, NY/NJ