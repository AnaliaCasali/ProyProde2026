-- ============================================================================
-- DATABASE INITIALIZATION SCRIPT FOR TESTING
-- Combina prode2.sql (schema) e InsertsProde.sql (data)
-- ============================================================================

DROP DATABASE IF EXISTS `prode`;
CREATE SCHEMA IF NOT EXISTS `prode` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `prode`;

-- ============================================================================
-- 1. USUARIOS
-- ============================================================================
CREATE TABLE IF NOT EXISTS `prode`.`Usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `tipo` VARCHAR(100) NOT NULL,
  `curso` VARCHAR(45) NOT NULL,
  `carrera` VARCHAR(45) NOT NULL,
  `nombreGrupo` VARCHAR(45) NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE = InnoDB;

-- ============================================================================
-- 2. ESTADIOS
-- ============================================================================
CREATE TABLE IF NOT EXISTS `prode`.`Estadios` (
  `idEstadio` INT NOT NULL AUTO_INCREMENT,
  `estadio` VARCHAR(100) NULL,
  `ciudad` VARCHAR(45) NULL,
  `pais` VARCHAR(45) NULL,
  PRIMARY KEY (`idEstadio`)
) ENGINE = InnoDB;

-- ============================================================================
-- 3. ETAPAS / JORNADAS
-- ============================================================================
CREATE TABLE IF NOT EXISTS `prode`.`Etapas` (
  `idEtapa` INT NOT NULL AUTO_INCREMENT,
  `nombreEtapa` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEtapa`)
) ENGINE = InnoDB;

-- ============================================================================
-- 4. EQUIPOS
-- ============================================================================
CREATE TABLE IF NOT EXISTS `prode`.`Equipos` (
  `idEquipo` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `icono` VARCHAR(245) NULL,
  `grupo` VARCHAR(10) NULL,
  PRIMARY KEY (`idEquipo`)
) ENGINE = InnoDB;

-- ============================================================================
-- 5. PARTIDOS
-- ============================================================================
CREATE TABLE IF NOT EXISTS `prode`.`Partidos` (
  `idPartido` INT NOT NULL AUTO_INCREMENT,
  `equipoLocal` INT NULL,
  `equipoVisitante` INT NULL,
  `idEtapa` INT NOT NULL,
  `idEstadio` INT NULL,
  `fechaHora` DATETIME NOT NULL,
  `golesLocal` INT NULL,
  `golesVisitante` INT NULL,
  `finalizado` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`idPartido`),
  CONSTRAINT `fk_partido_local` FOREIGN KEY (`equipoLocal`) REFERENCES `prode`.`Equipos` (`idEquipo`),
  CONSTRAINT `fk_partido_visitante` FOREIGN KEY (`equipoVisitante`) REFERENCES `prode`.`Equipos` (`idEquipo`),
  CONSTRAINT `fk_partido_etapa` FOREIGN KEY (`idEtapa`) REFERENCES `prode`.`Etapas` (`idEtapa`),
  CONSTRAINT `fk_partido_estadio` FOREIGN KEY (`idEstadio`) REFERENCES `prode`.`Estadios` (`idEstadio`)
) ENGINE = InnoDB;

-- ============================================================================
-- 6. JUGADAS (Predicciones de los usuarios)
-- ============================================================================
CREATE TABLE IF NOT EXISTS `prode`.`Jugadas` (
  `idJugada` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `idPartido` INT NOT NULL,
  `golesLocal` TINYINT NOT NULL,
  `golesVisitante` TINYINT NOT NULL,
  `puntaje` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idJugada`),
  CONSTRAINT `fk_jugada_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `prode`.`Usuarios` (`idUsuario`),
  CONSTRAINT `fk_jugada_partido` FOREIGN KEY (`idPartido`) REFERENCES `prode`.`Partidos` (`idPartido`),
  UNIQUE KEY `usuario_partido_unique` (`idUsuario`, `idPartido`)
) ENGINE = InnoDB;

-- ============================================================================
-- INSERTS - LIMPIEZA PREVIA
-- ============================================================================
DELETE FROM `prode`.`Jugadas`;
ALTER TABLE `prode`.`Jugadas` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Partidos`;
ALTER TABLE `prode`.`Partidos` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Equipos`;
ALTER TABLE `prode`.`Equipos` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Etapas`;
ALTER TABLE `prode`.`Etapas` AUTO_INCREMENT = 1;

DELETE FROM `prode`.`Estadios`;
ALTER TABLE `prode`.`Estadios` AUTO_INCREMENT = 1;

-- ============================================================================
-- INSERTS - ESTADIOS (IDs 1 al 16)
-- ============================================================================
INSERT INTO `prode`.`Estadios` (estadio, ciudad, pais) VALUES
('Estadio Azteca', 'CDMX', 'México'),
('Estadio Akron', 'Guadalajara', 'México'),
('Estadio BBVA', 'Monterrey', 'México'),
('BMO Field', 'Toronto', 'Canadá'),
('BC Place', 'Vancouver', 'Canadá'),
('Mercedes-Benz Stadium', 'Atlanta', 'USA'),
('Gillette Stadium', 'Boston', 'USA'),
('AT&T Stadium', 'Dallas', 'USA'),
('NRG Stadium', 'Houston', 'USA'),
('Arrowhead Stadium', 'Kansas City', 'USA'),
('SoFi Stadium', 'Los Angeles', 'USA'),
('Hard Rock Stadium', 'Miami', 'USA'),
('MetLife Stadium', 'East Rutherford', 'USA'),
('Lincoln Financial Field', 'Filadelfia', 'USA'),
('Levi\'s Stadium', 'Santa Clara', 'USA'),
('Lumen Field', 'Seattle', 'USA');

-- ============================================================================
-- INSERTS - ETAPAS (IDs 1 al 9)
-- ============================================================================
INSERT INTO `prode`.`Etapas` (nombreEtapa) VALUES
('Dieciseisavos de Final'),
('Octavos de Final'),
('Cuartos de Final'),
('Semifinal'),
('Tercer Puesto'),
('Final'),
('Fase de Grupos - Fecha 1'),
('Fase de Grupos - Fecha 2'),
('Fase de Grupos - Fecha 3');

-- ============================================================================
-- INSERTS - EQUIPOS (IDs 1 al 48)
-- ============================================================================
INSERT INTO `prode`.`Equipos` (nombre, grupo, icono) VALUES
-- Grupo A
('México', 'Grupo A', 'https://flagcdn.com/w80/mx.png'),
('Sudáfrica', 'Grupo A', 'https://flagcdn.com/w80/za.png'),
('Corea del Sur', 'Grupo A', 'https://flagcdn.com/w80/kr.png'),
('República Checa', 'Grupo A', 'https://flagcdn.com/w80/cz.png'),
-- Grupo B
('Canadá', 'Grupo B', 'https://flagcdn.com/w80/ca.png'),
('Bosnia y Herz.', 'Grupo B', 'https://flagcdn.com/w80/ba.png'),
('Qatar', 'Grupo B', 'https://flagcdn.com/w80/qa.png'),
('Suiza', 'Grupo B', 'https://flagcdn.com/w80/ch.png'),
-- Grupo C
('Uruguay', 'Grupo C', 'https://flagcdn.com/w80/uy.png'),
('Marruecos', 'Grupo C', 'https://flagcdn.com/w80/ma.png'),
('Haití', 'Grupo C', 'https://flagcdn.com/w80/ht.png'),
('Escocia', 'Grupo C', 'https://flagcdn.com/w80/gb-sct.png'),
-- Grupo D
('Estados Unidos', 'Grupo D', 'https://flagcdn.com/w80/us.png'),
('Paraguay', 'Grupo D', 'https://flagcdn.com/w80/py.png'),
('Australia', 'Grupo D', 'https://flagcdn.com/w80/au.png'),
('Turquía', 'Grupo D', 'https://flagcdn.com/w80/tr.png'),
-- Grupo E
('Brasil', 'Grupo E', 'https://flagcdn.com/w80/br.png'),
('Egipto', 'Grupo E', 'https://flagcdn.com/w80/eg.png'),
('Japón', 'Grupo E', 'https://flagcdn.com/w80/jp.png'),
('Polonia', 'Grupo E', 'https://flagcdn.com/w80/pl.png'),
-- Grupo F
('España', 'Grupo F', 'https://flagcdn.com/w80/es.png'),
('Nigeria', 'Grupo F', 'https://flagcdn.com/w80/ng.png'),
('Irak', 'Grupo F', 'https://flagcdn.com/w80/iq.png'),
('Gales', 'Grupo F', 'https://flagcdn.com/w80/gb-wls.png'),
-- Grupo G
('Francia', 'Grupo G', 'https://flagcdn.com/w80/fr.png'),
('Senegal', 'Grupo G', 'https://flagcdn.com/w80/sn.png'),
('Irán', 'Grupo G', 'https://flagcdn.com/w80/ir.png'),
('Rumanía', 'Grupo G', 'https://flagcdn.com/w80/ro.png'),
-- Grupo H
('Inglaterra', 'Grupo H', 'https://flagcdn.com/w80/gb.png'),
('Ecuador', 'Grupo H', 'https://flagcdn.com/w80/ec.png'),
('Uzbekistán', 'Grupo H', 'https://flagcdn.com/w80/uz.png'),
('Noruega', 'Grupo H', 'https://flagcdn.com/w80/no.png'),
-- Grupo I
('Bélgica', 'Grupo I', 'https://flagcdn.com/w80/be.png'),
('Costa de Marfil', 'Grupo I', 'https://flagcdn.com/w80/ci.png'),
('Panamá', 'Grupo I', 'https://flagcdn.com/w80/pa.png'),
('Ucrania', 'Grupo I', 'https://flagcdn.com/w80/ua.png'),
-- Grupo J
('Argentina', 'Grupo J', 'https://flagcdn.com/w80/ar.png'),
('Argelia', 'Grupo J', 'https://flagcdn.com/w80/dz.png'),
('Austria', 'Grupo J', 'https://flagcdn.com/w80/at.png'),
('Jordania', 'Grupo J', 'https://flagcdn.com/w80/jo.png'),
-- Grupo K
('Portugal', 'Grupo K', 'https://flagcdn.com/w80/pt.png'),
('Camerún', 'Grupo K', 'https://flagcdn.com/w80/cm.png'),
('Costa Rica', 'Grupo K', 'https://flagcdn.com/w80/cr.png'),
('Hungría', 'Grupo K', 'https://flagcdn.com/w80/hu.png'),
-- Grupo L
('Países Bajos', 'Grupo L', 'https://flagcdn.com/w80/nl.png'),
('Colombia', 'Grupo L', 'https://flagcdn.com/w80/co.png'),
('Nueva Zelanda', 'Grupo L', 'https://flagcdn.com/w80/nz.png'),
('Serbia', 'Grupo L', 'https://flagcdn.com/w80/rs.png');

-- ============================================================================
-- INSERTS - PARTIDOS - FASE DE GRUPOS (72 partidos)
-- ============================================================================
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
-- GRUPO A (6 partidos)
(1, 2, 7, 1, '2026-06-11 16:00:00', 0), (3, 4, 7, 2, '2026-06-11 20:00:00', 0),
(1, 3, 7, 3, '2026-06-16 18:00:00', 0), (4, 2, 7, 1, '2026-06-16 21:00:00', 0),
(4, 1, 7, 2, '2026-06-24 17:00:00', 0), (2, 3, 7, 3, '2026-06-24 17:00:00', 0),

-- GRUPO B (6 partidos)
(5, 6, 7, 4, '2026-06-12 13:00:00', 0), (7, 8, 7, 5, '2026-06-12 16:00:00', 0),
(5, 7, 7, 4, '2026-06-17 15:00:00', 0), (8, 6, 7, 5, '2026-06-17 19:00:00', 0),
(8, 5, 7, 4, '2026-06-24 20:00:00', 0), (6, 7, 7, 5, '2026-06-24 20:00:00', 0),

-- GRUPO C (6 partidos)
(9, 10, 7, 6, '2026-06-13 12:00:00', 0), (11, 12, 7, 7, '2026-06-13 15:00:00', 0),
(9, 11, 7, 14, '2026-06-18 12:00:00', 0), (12, 10, 7, 13, '2026-06-18 15:00:00', 0),
(12, 9, 7, 13, '2026-06-25 14:00:00', 0), (10, 11, 7, 14, '2026-06-25 14:00:00', 0),

-- GRUPO D (6 partidos)
(13, 14, 7, 11, '2026-06-12 19:00:00', 0), (15, 16, 7, 15, '2026-06-13 18:00:00', 0),
(13, 15, 7, 16, '2026-06-19 15:00:00', 0), (16, 14, 7, 15, '2026-06-19 19:00:00', 0),
(16, 13, 7, 11, '2026-06-25 18:00:00', 0), (14, 15, 7, 16, '2026-06-25 18:00:00', 0),

-- GRUPO E (6 partidos)
(17, 18, 7, 8, '2026-06-14 13:00:00', 0), (19, 20, 7, 9, '2026-06-14 17:00:00', 0),
(17, 19, 7, 8, '2026-06-20 12:00:00', 0), (20, 18, 7, 9, '2026-06-20 16:00:00', 0),
(20, 17, 7, 10, '2026-06-26 13:00:00', 0), (18, 19, 7, 8, '2026-06-26 13:00:00', 0),

-- GRUPO F (6 partidos)
(21, 22, 7, 12, '2026-06-14 20:00:00', 0), (23, 24, 7, 13, '2026-06-15 12:00:00', 0),
(21, 23, 7, 12, '2026-06-20 20:00:00', 0), (24, 22, 7, 14, '2026-06-21 12:00:00', 0),
(24, 21, 7, 12, '2026-06-26 17:00:00', 0), (22, 23, 7, 13, '2026-06-26 17:00:00', 0),

-- GRUPO G (6 partidos)
(25, 26, 7, 1, '2026-06-15 15:00:00', 0), (27, 28, 7, 10, '2026-06-15 18:00:00', 0),
(25, 27, 7, 11, '2026-06-21 15:00:00', 0), (28, 26, 7, 16, '2026-06-21 18:00:00', 0),
(28, 25, 7, 15, '2026-06-26 20:00:00', 0), (26, 27, 7, 16, '2026-06-26 20:00:00', 0),

-- GRUPO H (6 partidos)
(29, 30, 7, 6, '2026-06-16 12:00:00', 0), (31, 32, 7, 7, '2026-06-16 15:00:00', 0),
(29, 31, 7, 13, '2026-06-22 12:00:00', 0), (32, 30, 7, 14, '2026-06-22 15:00:00', 0),
(32, 29, 7, 6, '2026-06-27 13:00:00', 0), (30, 31, 7, 7, '2026-06-27 13:00:00', 0),

-- GRUPO I (6 partidos)
(33, 34, 7, 4, '2026-06-17 12:00:00', 0), (35, 36, 7, 5, '2026-06-18 18:00:00', 0),
(33, 35, 7, 15, '2026-06-22 18:00:00', 0), (36, 34, 7, 11, '2026-06-22 21:00:00', 0),
(36, 33, 7, 4, '2026-06-27 16:00:00', 0), (34, 35, 7, 5, '2026-06-27 16:00:00', 0),

-- GRUPO J (6 partidos)
(37, 38, 7, 8, '2026-06-19 12:00:00', 0), (39, 40, 7, 9, '2026-06-19 15:00:00', 0),
(37, 39, 7, 10, '2026-06-23 13:00:00', 0), (40, 38, 7, 1, '2026-06-23 16:00:00', 0),
(40, 37, 7, 9, '2026-06-27 19:00:00', 0), (38, 39, 7, 10, '2026-06-27 19:00:00', 0),

-- GRUPO K (6 partidos)
(41, 42, 7, 2, '2026-06-20 15:00:00', 0), (43, 44, 7, 3, '2026-06-20 18:00:00', 0),
(41, 43, 7, 6, '2026-06-23 19:00:00', 0), (44, 42, 7, 7, '2026-06-23 22:00:00', 0),
(44, 41, 7, 2, '2026-06-27 21:00:00', 0), (42, 43, 7, 3, '2026-06-27 21:00:00', 0),

-- GRUPO L (6 partidos)
(45, 46, 7, 13, '2026-06-21 19:00:00', 0), (47, 48, 7, 12, '2026-06-21 21:00:00', 0),
(45, 47, 7, 15, '2026-06-24 14:00:00', 0), (48, 46, 7, 16, '2026-06-24 14:00:00', 0),
(48, 45, 7, 14, '2026-06-27 23:00:00', 0), (46, 47, 7, 13, '2026-06-27 23:00:00', 0);

-- ============================================================================
-- INSERTS - PARTIDOS - FASES ELIMINATORIAS (Placeholders para IDs futuras)
-- ============================================================================

-- DIECISEISAVOS DE FINAL (16 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 1, 8, '2026-06-28 14:00:00', 0),
(NULL, NULL, 1, 13, '2026-06-28 18:00:00', 0),
(NULL, NULL, 1, 1, '2026-06-29 14:00:00', 0),
(NULL, NULL, 1, 11, '2026-06-29 18:00:00', 0),
(NULL, NULL, 1, 6, '2026-06-29 22:00:00', 0),
(NULL, NULL, 1, 12, '2026-06-30 14:00:00', 0),
(NULL, NULL, 1, 7, '2026-06-30 18:00:00', 0),
(NULL, NULL, 1, 15, '2026-07-01 14:00:00', 0),
(NULL, NULL, 1, 8, '2026-07-01 18:00:00', 0),
(NULL, NULL, 1, 9, '2026-07-01 22:00:00', 0),
(NULL, NULL, 1, 10, '2026-07-02 14:00:00', 0),
(NULL, NULL, 1, 14, '2026-07-02 18:00:00', 0),
(NULL, NULL, 1, 4, '2026-07-02 22:00:00', 0),
(NULL, NULL, 1, 5, '2026-07-03 14:00:00', 0),
(NULL, NULL, 1, 2, '2026-07-03 18:00:00', 0),
(NULL, NULL, 1, 1, '2026-07-03 22:00:00', 0);

-- OCTAVOS DE FINAL (8 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 2, 11, '2026-07-04 15:00:00', 0),
(NULL, NULL, 2, 12, '2026-07-04 19:00:00', 0),
(NULL, NULL, 2, 10, '2026-07-05 15:00:00', 0),
(NULL, NULL, 2, 8, '2026-07-05 19:00:00', 0),
(NULL, NULL, 2, 6, '2026-07-06 15:00:00', 0),
(NULL, NULL, 2, 13, '2026-07-06 19:00:00', 0),
(NULL, NULL, 2, 7, '2026-07-07 15:00:00', 0),
(NULL, NULL, 2, 9, '2026-07-07 19:00:00', 0);

-- CUARTOS DE FINAL (4 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 3, 12, '2026-07-09 13:00:00', 0),
(NULL, NULL, 3, 10, '2026-07-10 18:00:00', 0),
(NULL, NULL, 3, 11, '2026-07-11 13:00:00', 0),
(NULL, NULL, 3, 7, '2026-07-11 18:00:00', 0);

-- SEMIFINALES (2 partidos)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 4, 8, '2026-07-14 19:00:00', 0),
(NULL, NULL, 4, 6, '2026-07-15 19:00:00', 0);

-- TERCER PUESTO (1 partido)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 5, 12, '2026-07-18 14:00:00', 0);

-- GRAN FINAL (1 partido)
INSERT INTO `prode`.`Partidos` (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, finalizado) VALUES
(NULL, NULL, 6, 13, '2026-07-19 14:00:00', 0);
