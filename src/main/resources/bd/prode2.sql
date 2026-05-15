DROP DATABASE IF EXISTS `prode`;
CREATE SCHEMA IF NOT EXISTS `prode` DEFAULT CHARACTER SET utf8;
USE `prode`;

-- 1. USUARIOS
CREATE TABLE IF NOT EXISTS `prode`.`Usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE, -- Añadido UNIQUE para evitar correos duplicados
  `tipo` VARCHAR(45) NOT NULL, -- Para definir tipo de usuario
  `curso` VARCHAR(45) NOT NULL,
  `carrera` VARCHAR(145) NOT NULL, -- TSDE, LENGUA, etc.
  `nombreGrupo` VARCHAR(45) NULL,
  `puntajeTotal` INT NOT NULL DEFAULT 0, -- Suma de puntaje total de todas sus jugadas
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
  `puntaje` INT NOT NULL DEFAULT 0, -- Se calcula mediante script/trigger al finalizar el partido (tr_calcular_puntajes)
  PRIMARY KEY (`idJugada`),
  CONSTRAINT `fk_jugada_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `prode`.`Usuarios` (`idUsuario`),
  CONSTRAINT `fk_jugada_partido` FOREIGN KEY (`idPartido`) REFERENCES `prode`.`Partidos` (`idPartido`),
  UNIQUE KEY `usuario_partido_unique` (`idUsuario`, `idPartido`)) -- Evita que un usuario juegue dos veces el mismo partido
ENGINE = InnoDB;

-- Insert usuario administrador
INSERT INTO prode.Usuarios (idUsuario, password, email, tipo, curso, carrera, nombreGrupo)
VALUES ('1', '$2a$12$wWnqWd.GVGeqyhfwpog3z.nikcB8gOf/5dM1Ads.OrkyzSJcZCyhS', 'tsds@administrador.edu', 'ADMINISTRADOR', 'Tercer Año', 'TECNICATURA_SUPERIOR_EN_DESARROLLO_DE_SOFTWARE', 'Administrador');
-- email: tsds@administrador.edu | contraseña: 123456

-- TRIGGERS
-- Asegurar que en el milisegundo en que pasas un partido a finalizado = 1, o al actualizar goles
-- Todos los usuarios que participaron reciben sus puntos automáticamente, sin que tener ejecutar consultas manuales desde backend.
DROP TRIGGER IF EXISTS tr_calcular_puntajes;
DELIMITER $$
CREATE TRIGGER tr_calcular_puntajes
    AFTER UPDATE ON Partidos
    FOR EACH ROW
BEGIN
    -- Se ejecuta SOLO si el partido está finalizado (cubre tanto la primera vez como correcciones)
    IF NEW.finalizado = 1 AND (
        OLD.finalizado = 0 OR
        OLD.golesLocal != NEW.golesLocal OR
        OLD.golesVisitante != NEW.golesVisitante
    ) THEN

    UPDATE Jugadas as j
    SET j.puntaje =
            CASE
                -- Acertó TODO (Resultado exacto)
                WHEN NEW.golesLocal = j.golesLocal AND NEW.golesVisitante = j.golesVisitante
                    THEN 90

                -- Acertó resultado y 1 de goles
                WHEN SIGN(NEW.golesLocal - NEW.golesVisitante) = SIGN(j.golesLocal - j.golesVisitante)
                    AND (NEW.golesLocal = j.golesLocal OR NEW.golesVisitante = j.golesVisitante)
                    THEN 70

                -- Acertó solo resultado general
                WHEN SIGN(NEW.golesLocal - NEW.golesVisitante) = SIGN(j.golesLocal - j.golesVisitante)
                    THEN 50

                -- No acertó el resultado
                ELSE
                    0
                END
    WHERE j.idPartido = NEW.idPartido;
END IF;

END $$
DELIMITER ;