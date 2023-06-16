/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.main.java.com.allset.gestaovenda.modelo.dao;

import src.main.java.com.allset.gestaovenda.modelo.entidades.PERFIL;
import src.main.java.com.allset.gestaovenda.modelo.entidades.Usuario;
import src.main.java.com.allset.gestaovenda.modelo.exception.NegocioException;
import src.main.java.com.allset.gestaovenda.view.modelo.LoginDto;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class AutenticacaoDao {
    
    private final UsuarioDao usuarioDao;

    public AutenticacaoDao() {
        this.usuarioDao = new UsuarioDao();
    }
    
    public boolean temPermissao(Usuario usuario) {
        try {
            permissao(usuario);
            return true;
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Usuario sem permissao", 0);
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }
    
    private void permissao(Usuario usuario) {
        if(!PERFIL.ADMIN.equals(usuario.getPerfil())) {
            throw new NegocioException("Sem permissao para realizar essa açao.");
        }
    }

    public Usuario login(LoginDto login) {
        Usuario usuario = usuarioDao.buscarUsuarioPeloUsername(login.getUsername());
        
        if(usuario == null || !usuario.isEstado()) 
            return null;
       
        if(usuario.isEstado() && validaSenha(usuario.getSenha(), login.getSenha())) {
            usuarioDao.actualizarUltimoLogin(usuario);
            return usuario;
        }
        return null;
    }
    
    private boolean validaSenha(String usuarioSenha, String loginSenha) {
        return usuarioSenha.equals(loginSenha);
    }
    
}
