package com.nmpiesat.idata.user.entity;

/**
 * 数据角色权限
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 13:33
 */
import java.io.Serializable;

public class DataRolelimits implements Serializable {
  private static final long serialVersionUID = 1L;
  // 产品id或资料代号
  private String dataId;
  // 角色id
  private String roledataId;

  public void setDataId(String dataId) {
    this.dataId = dataId;
  }

  public void setRoledataId(String roledataId) {
    this.roledataId = roledataId;
  }

  public String getDataId() {
    return dataId;
  }

  public String getRoledataId() {
    return roledataId;
  }
}
