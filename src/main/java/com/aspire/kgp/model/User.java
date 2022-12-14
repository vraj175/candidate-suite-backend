package com.aspire.kgp.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class User extends SuperBase {

  @ManyToOne
  @JoinColumn(name = "role", referencedColumnName = "id", insertable = true, nullable = false,
      updatable = true)
  private Role role;

  @Column(name = "galaxyId", nullable = false)
  private String galaxyId;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  private Timestamp lastLogin;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "language", referencedColumnName = "id", insertable = true, nullable = false,
      updatable = true)
  private Language language;

  @Column(columnDefinition = "boolean default true")
  private boolean passwordReset;

  @Column(columnDefinition = "boolean default false")
  private boolean isDeleted;

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getGalaxyId() {
    return galaxyId;
  }

  public void setGalaxyId(String galaxyId) {
    this.galaxyId = galaxyId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Timestamp getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Timestamp lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public boolean isPasswordReset() {
    return passwordReset;
  }

  public void setPasswordReset(boolean passwordReset) {
    this.passwordReset = passwordReset;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
