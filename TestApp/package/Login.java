/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author derek
 */

@Named(value = "login")
@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private String username, password;
    private int emp_id;
    
    private DBConnect dbConnect = new DBConnect();
    
    private UIInput loginUI;

    public UIInput getLoginUI() {
        return loginUI;
    }

    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }
       
    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void validateUsr(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        // this functions validates username and login
        // does not return anything but will throw an exception if the user
        // uses an incorrect login
        // the exception will print an error message on the page definded by validator message
        String pass;
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);
                
        PreparedStatement validateAcc = con.prepareStatement(
            "SELECT login, password FROM customers WHERE login = ?");
        
        username = loginUI.getValue().toString();
        password = value.toString();
        
        validateAcc.setString(1, username);

        ResultSet rs = validateAcc.executeQuery();

        if(rs.next())
        {
            pass = rs.getString("password");
            if(!password.equals(pass)) {     // password validates with login
                FacesMessage errorMessage = new FacesMessage("Wrong login/password");
                throw new ValidatorException(errorMessage);
            }
        }
        else if(username.equals("") && password.equals("")) {
            
        }
        else {
            FacesMessage errorMessage = new FacesMessage("Wrong login/password");
            throw new ValidatorException(errorMessage);
        }
    }
    
    public void validateEmp(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        // this functions validates username and login
        // does not return anything but will throw an exception if the user
        // uses an incorrect login
        // the exception will print an error message on the page definded by validator message
        String pass;
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);
                
        PreparedStatement validateAcc = con.prepareStatement(
            "SELECT emp_id, password FROM employees WHERE emp_id = ?");
        
        emp_id = (Integer) loginUI.getValue();
        password = value.toString();
        
        validateAcc.setInt(1, emp_id);

        ResultSet rs = validateAcc.executeQuery();

        if(rs.next())
        {
            pass = rs.getString("password");
            if(!password.equals(pass)) {     // password validates with login
                FacesMessage errorMessage = new FacesMessage("Wrong id/password");
                throw new ValidatorException(errorMessage);
            }
        }
        else if(emp_id == 0 && password.equals("")) {
            
        }
        else {
            FacesMessage errorMessage = new FacesMessage("Wrong id/password");
            throw new ValidatorException(errorMessage);
        }
    }
    public String go() {
        // login success go to home page
        return "Login";
    }

}
