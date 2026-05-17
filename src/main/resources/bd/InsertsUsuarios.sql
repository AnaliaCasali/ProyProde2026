-- SCRIPT PARA POBLAR LA TABLA DE USUARIOS CON DATOS DE PRUEBA
-- Nota: La contraseña para todos estos usuarios es '123456'
-- (El hash corresponde a BCrypt)

-- 1. Insertamos un Administrador 
INSERT INTO `prode`.`Usuarios` (`password`, `email`, `tipo`, `curso`, `carrera`, `nombreGrupo`) 
VALUES 
('$2a$12$N9u13U/aP/0L.v5H7U..ueD2I6z9L8aP5Xz/3A0wO9F7V1mX/P.iG', 'admin@isp63.edu.ar', 'ADMINISTRADOR', 'Personal', 'TECNICATURA_SUPERIOR_EN_DESARROLLO_DE_SOFTWARE', 'Administración');

-- 2. Insertamos varios Usuarios normales
INSERT INTO `prode`.`Usuarios` (`password`, `email`, `tipo`, `curso`, `carrera`, `nombreGrupo`) 
VALUES 
('$2a$12$N9u13U/aP/0L.v5H7U..ueD2I6z9L8aP5Xz/3A0wO9F7V1mX/P.iG', 'juan.perez@isp63.edu.ar', 'USUARIO', '3ro', 'TECNICATURA_SUPERIOR_EN_DESARROLLO_DE_SOFTWARE', 'Los Pibes del Backend'),
('$2a$12$N9u13U/aP/0L.v5H7U..ueD2I6z9L8aP5Xz/3A0wO9F7V1mX/P.iG', 'maria.gomez@isp63.edu.ar', 'USUARIO', '2do', 'PROFESORADO_EN_LENGUA_Y_LITERATURA', 'Las Letras'),
('$2a$12$N9u13U/aP/0L.v5H7U..ueD2I6z9L8aP5Xz/3A0wO9F7V1mX/P.iG', 'carlos.ruiz@isp63.edu.ar', 'USUARIO', '1ro', 'PROFESORADO_DE_EDUCACION_PRIMARIA', 'Primaria Power'),
('$2a$12$N9u13U/aP/0L.v5H7U..ueD2I6z9L8aP5Xz/3A0wO9F7V1mX/P.iG', 'lucia.fernandez@isp63.edu.ar', 'USUARIO', '3ro', 'TECNICATURA_EN_ADMINISTRACION_RURAL', 'El Campo Prode'),
('$2a$12$N9u13U/aP/0L.v5H7U..ueD2I6z9L8aP5Xz/3A0wO9F7V1mX/P.iG', 'pedro.martinez@isp63.edu.ar', 'USUARIO', '1ro', 'TECNICATURA_SUPERIOR_EN_DESARROLLO_DE_SOFTWARE', 'Cachimbos IT');

-- --------------------------------------------------------
-- SCRIPT EN CASO DE PROBAR JUGADAS Y SIMULAR EL RANKING 
-- Asumimos que los IDs de usuario generados van del 2 al 6 (el 1 es admin)
-- Asumimos que el idPartido = 1 (México vs Sudáfrica) y el idPartido = 2 (Corea vs Chequia) ya existen.
-- --------------------------------------------------------

-- Jugadas para Juan Pérez (El líder del ranking - 150 pts totales)
INSERT INTO `prode`.`jugadas` (idUsuario, idPartido, golesLocal, golesVisitante, puntaje) VALUES 
(2, 1, 2, 1, 100), -- Acertó resultado exacto
(2, 2, 1, 0, 50);  -- Acertó ganador pero no resultado exacto

-- Jugadas para María Gómez (Segunda en el ranking - 100 pts totales)
INSERT INTO `prode`.`jugadas` (idUsuario, idPartido, golesLocal, golesVisitante, puntaje) VALUES 
(3, 1, 2, 1, 100), -- Acertó resultado exacto
(3, 2, 0, 0, 0);   -- No acertó nada

-- Jugadas para Carlos Ruiz (Tercero en el ranking - 50 pts totales)
INSERT INTO `prode`.`jugadas` (idUsuario, idPartido, golesLocal, golesVisitante, puntaje) VALUES 
(4, 1, 1, 0, 50),  -- Acertó ganador pero no exacto
(4, 2, 2, 2, 0);   -- No acertó nada

-- Jugadas para Lucía Fernández (Cuarta, fuera del top 3 )
INSERT INTO `prode`.`jugadas` (idUsuario, idPartido, golesLocal, golesVisitante, puntaje) VALUES 
(5, 1, 0, 3, 0);   -- Le pifió por completo
