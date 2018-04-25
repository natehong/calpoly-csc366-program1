/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Patrick
 */

@Named(value = "item")
@ManagedBean
@SessionScoped

public class Item implements Serializable {
    private int chargeCode;
    private String description;
    private int cost;
    private boolean itemCreated = false;
    private DBConnect dbConnect = new DBConnect();
    
    
    public boolean isItemCreated() {
        return itemCreated;
    }

    public void setItemCreated(boolean itemCreated) {
        this.itemCreated = itemCreated;
    }

    public int getChargeCode(){
        return chargeCode;
    }
    
    public void setChargeCode(int chargeCode){
        this.chargeCode = chargeCode;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCost(){
        return cost;
    }
    
    public void setCost(int cost){
        this.cost = cost;
    }
    
    public void createItem() throws SQLException {
        int ID;
        Connection con = dbConnect.getConnection();

        if (con == null) {
         throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);
        
        Statement statement = con.createStatement();
        
        PreparedStatement getID = con.prepareStatement(
            "SELECT charge_code FROM additional_charges ORDER BY charge_code DESC");
        
        ResultSet rsID = getID.executeQuery();

        ID = (rsID.next()) ? rsID.getInt("charge_code") + 1 : 1;
        
        PreparedStatement createEmp = con.prepareStatement(
            "INSERT INTO additional_charges VALUES(?,?,?)");
        
        createEmp.setInt(1, ID);
        createEmp.setString(2, description);
        createEmp.setInt(3, cost);
        
        createEmp.executeUpdate();
        statement.close();
        con.commit();
        con.close();
        
        description = "";
        cost = 0;
        itemCreated = true;
    }
}