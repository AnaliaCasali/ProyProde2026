DROP DATABASE IF EXISTS `prode`;
CREATE SCHEMA IF NOT EXISTS `prode` DEFAULT CHARACTER SET utf8;
USE `prode`;

-- 1. USUARIOS
CREATE TABLE IF NOT EXISTS `prode`.`Usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE, -- Añadido UNIQUE para evitar correos duplicados
  `tipo` VARCHAR(100) NOT NULL, -- Para definir tipo de usuario
  `curso` VARCHAR(45) NOT NULL,
  `carrera` VARCHAR(45) NOT NULL, -- TSDE, LENGUA, etc.
  `nombreGrupo` VARCHAR(45) NULL,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB;

-- 2. ESTADIOS
CREATE TABLE IF NOT EXISTS `prode`.`Estadios` (
  `idEstadio` INT NOT NULL AUTO_INCREMENT,
  `estadio` VARCHAR(100) NULL,
  `ciudad` VARCHAR(45) NULL,
  `pais` VARCHAR(45) NULL,
  PRIMARY KEY (`idEstadio`))
ENGINE = InnoDB;

-- 3. ETAPAS / JORNADAS (Solo maneja el momento/fase del torneo)
CREATE TABLE IF NOT EXISTS `prode`.`Etapas` (
  `idEtapa` INT NOT NULL AUTO_INCREMENT,
  `nombreEtapa` VARCHAR(45) NOT NULL, -- Ej: 'Fecha 1 - Grupo A', 'Semifinal'
  PRIMARY KEY (`idEtapa`))
ENGINE = InnoDB;

-- 4. EQUIPOS
CREATE TABLE IF NOT EXISTS `prode`.`Equipos` (
  `idEquipo` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `icono` VARCHAR(245) NULL,
  `grupo` VARCHAR(10) NULL, -- Ej: 'Grupo A'
  PRIMARY KEY (`idEquipo`))
ENGINE = InnoDB;

-- 5. PARTIDOS (El estadio y la fecha exacta pertenecen aquí)
CREATE TABLE IF NOT EXISTS `prode`.`Partidos` (
  `idPartido` INT NOT NULL AUTO_INCREMENT,
  `equipoLocal` INT NOT NULL,
  `equipoVisitante` INT NOT NULL,
  `idEtapa` INT NOT NULL,
  `idEstadio` INT NULL,
  `fechaHora` DATETIME NOT NULL, -- Tipo de dato correcto para tiempo
  `golesLocal` INT NULL, -- NULL significa que no se ha jugado
  `golesVisitante` INT NULL,
  `finalizado` TINYINT(1) NOT NULL DEFAULT 0, -- 0 = No, 1 = Sí
  PRIMARY KEY (`idPartido`),
  CONSTRAINT `fk_partido_local` FOREIGN KEY (`equipoLocal`) REFERENCES `prode`.`Equipos` (`idEquipo`),
  CONSTRAINT `fk_partido_visitante` FOREIGN KEY (`equipoVisitante`) REFERENCES `prode`.`Equipos` (`idEquipo`),
  CONSTRAINT `fk_partido_etapa` FOREIGN KEY (`idEtapa`) REFERENCES `prode`.`Etapas` (`idEtapa`),
  CONSTRAINT `fk_partido_estadio` FOREIGN KEY (`idEstadio`) REFERENCES `prode`.`Estadios` (`idEstadio`))
ENGINE = InnoDB;

-- 6. JUGADAS (Predicciones de los usuarios)
CREATE TABLE IF NOT EXISTS `prode`.`Jugadas` (
  `idJugada` INT NOT NULL AUTO_INCREMENT, -- Agregado AUTO_INCREMENT
  `idUsuario` INT NOT NULL,
  `idPartido` INT NOT NULL,
  `golesLocal` TINYINT NOT NULL, -- Eliminado el campo 'resultado' por redundancia
  `golesVisitante` TINYINT NOT NULL,
  `puntaje` INT NOT NULL DEFAULT 0, -- Se calcula mediante script/trigger al finalizar el partido
  PRIMARY KEY (`idJugada`),
  CONSTRAINT `fk_jugada_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `prode`.`Usuarios` (`idUsuario`),
  CONSTRAINT `fk_jugada_partido` FOREIGN KEY (`idPartido`) REFERENCES `prode`.`Partidos` (`idPartido`),
  UNIQUE KEY `usuario_partido_unique` (`idUsuario`, `idPartido`)) -- Evita que un usuario juegue dos veces el mismo partido
ENGINE = InnoDB;