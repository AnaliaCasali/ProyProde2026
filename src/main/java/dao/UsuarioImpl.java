package dao;

import entities.Usuario;
import enums.TipoUsuario;
import interfaces.AdmConexion;
import interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl implements DAO<Usuario, Integer>, AdmConexion {
    private Connection conn = null;

    // CONSULTAS SQL
    // insert general
    private static final String SQL_INSERT =
            "INSERT INTO usuarios (email, password, curso, tipo, carrera, nombreGrupo)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    // update general
    private static final String SQL_UPDATE =
            "UPDATE usuarios SET " +
                    "email = ? , password ? , curso = ? , carrera = ? , nombreGrupo)" +
                    "WHERE idUsuario = ?";

    private static final String SQL_UPDATEPASSWORD="UPDATE usuarios SET " +
                    "password = ? " +
                    "WHERE idUsuario = ?";

    private static final String SQL_DELETE =
            "DELETE * FROM usuarios WHERE idUsuario = ?";

    private static final String SQL_GETALL =
            "SELECT * FROM usuarios ORDER BY idUsuario";

    private static final String SQL_GETBYID =
            "SELECT * FROM usuarios WHERE idUsuario = ?";

    // consulta para obtener usuario por email
    private static final String SQL_GETBYEMAIL =
            "SELECT * FROM usuarios WHERE email = ?";


    // métodos DAO
    @Override
    public List<Usuario> getAll() {
        conn = obtenerConexion();

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<Usuario> listaUsuarios = new ArrayList<>();

        try {
            pst = conn.prepareStatement(SQL_GETALL);
            rs = pst.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));
                usuario.setCurso(rs.getString("curso"));
                usuario.setCarrera(rs.getString("carrera"));
                usuario.setNombreGrupo(rs.getString("nombreGrupo"));
                listaUsuarios.add(usuario);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Hubo un error al obtener la lista de usuarios");
            throw new RuntimeException(e);
        }
        return listaUsuarios;
    }

    @Override
    public void insert(Usuario objeto) {
        Usuario usuario = objeto;
        conn = obtenerConexion();

        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, usuario.getEmail());
            pst.setString(2, usuario.getPassword());
            pst.setString(3, usuario.getTipo().toString());
            pst.setString(4, usuario.getCurso());
            pst.setString(5, usuario.getCarrera());
            pst.setString(6, usuario.getNombreGrupo());

            int resultado = pst.executeUpdate();
            if (resultado == 1) {
                System.out.println("El usuario ha sido registrado");
            } else {
                System.out.println("No se pudo registrar el usuario");
            }

            pst.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Hubo un error al registrar al usuario");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Usuario objeto) {
        Usuario usuario = new Usuario();

        if (this.existsById(usuario.getIdUsuario())) {
            conn = obtenerConexion();
            PreparedStatement pst = null;

            try {
                pst = conn.prepareStatement(SQL_UPDATE);

                pst.setString(1, usuario.getEmail());
                pst.setString(2, usuario.getPassword());
                pst.setString(3, usuario.getTipo().toString());
                pst.setString(4, usuario.getCurso());
                pst.setString(5, usuario.getCarrera());
                pst.setString(6, usuario.getNombreGrupo());

                int resultado = pst.executeUpdate();
                if (resultado == 1) {
                    System.out.println("El usuario se ha actualizado");
                } else {
                    System.out.println("No se pudo actualizar el usuario");
                }

                pst.close();
                conn.close();

            } catch (SQLException e) {
                System.out.println("Hubo un error al actualizar el usuario");
                throw new RuntimeException(e);
            }
        }
    }


    public void updatePassword(Integer idUsuario, String nuevaPassword) {

        conn = obtenerConexion();

        PreparedStatement pst = null;

        try {

            pst = conn.prepareStatement(SQL_UPDATEPASSWORD);

            pst.setString(1, nuevaPassword);
            pst.setInt(2, idUsuario);

            int resultado = pst.executeUpdate();

            if(resultado == 1) {
                System.out.println("Contraseña actualizada");
            } else {
                System.out.println("No se pudo actualizar la contraseña");
            }

            pst.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error al actualizar contraseña");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        conn = obtenerConexion();

        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement(SQL_DELETE);

            pst.setInt(1, id);

            int resultado = pst.executeUpdate();
            if (resultado == 1) {
                System.out.println("Usuario eliminado");
            } else {
                System.out.println("No se pudo eliminar el usuario");
            }

        } catch (SQLException e) {
            System.out.println("Hubo un error al intentar eliminar un usuario");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario getById(Integer id) {
        conn = obtenerConexion();

        PreparedStatement pst = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            pst = conn.prepareStatement(SQL_GETBYID);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));
                usuario.setCurso(rs.getString("curso"));
                usuario.setCarrera(rs.getString("carrera"));
                usuario.setNombreGrupo(rs.getString("nombreGrupo"));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    @Override
    public boolean existsById(Integer id) {
        conn = obtenerConexion();

        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            pst = conn.prepareStatement(SQL_GETBYID);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                existe = true;
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return existe;
    }

    public Usuario getByEmail(String id) {
        conn = obtenerConexion();

        PreparedStatement psBuscar = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            psBuscar = conn.prepareStatement(SQL_GETBYEMAIL);
            psBuscar.setString(1, id);
            rs = psBuscar.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));
                usuario.setCurso(rs.getString("curso"));
                usuario.setCarrera(rs.getString("carrera"));
                usuario.setNombreGrupo(rs.getString("nombreGrupo"));
            }

            rs.close();
            psBuscar.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean existsByEmail(String email) {
        conn = obtenerConexion();

        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            pst = conn.prepareStatement(SQL_GETBYEMAIL);
            pst.setString(1, email);
            rs = pst.executeQuery();

            if (rs.next()) {
                existe = true;
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return existe;
    }
}
