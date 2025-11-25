package com.vn.Project_Trainning_Week2.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
abstract class BaseEntity {
  public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Column(name = "CREATE_DAY_TIME")
  private String createDayTime;

  @Column(name = "UPDATE_DAY_TIME")
  private String updateDayTime;

  @PrePersist
  public void prePersist() {
    this.createDayTime = LocalDateTime.now().format(formatter);
    this.updateDayTime = this.createDayTime;
  }

  @PreUpdate
  public void preUpdate() {
    if (this.createDayTime == null) {
      this.createDayTime = LocalDateTime.now().format(formatter);
    }
    this.updateDayTime = LocalDateTime.now().format(formatter);
  }
}
