package com.thinkgem.jeesite.modules.UserDataRole.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 15:36
 */
@Embeddable
public class DataRoleLimitsid implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer dataId;
  private String roledataId;

  public void setDataId(Integer dataId) {
    this.dataId = dataId;
  }

  public void setRoledataId(String roledataId) {
    this.roledataId = roledataId;
  }

  public Integer getDataId() {
    return dataId;
  }

  public String getRoledataId() {
    return roledataId;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof DataRoleLimitsid)) {
      return false;
    }

    DataRoleLimitsid key = (DataRoleLimitsid) obj;

    if (!dataId.equals(key.getDataId())) {
      return false;
    }

    if (!roledataId.equals(key.getRoledataId())) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {

    int result = 0;

    result = dataId == null ? 0 : dataId.hashCode();
    result = 29 * (roledataId == null ? 0 : roledataId.hashCode()) + result;
    return result;
  }
}
