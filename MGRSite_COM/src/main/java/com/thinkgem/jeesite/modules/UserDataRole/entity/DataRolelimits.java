package com.thinkgem.jeesite.modules.UserDataRole.entity;

/**
 * 数据角色权限
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 13:33
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SUP_DATAROLELIMITS")
public class DataRolelimits implements Serializable {
  private static final long serialVersionUID = 1L;
  // 产品id
  private String dataId;
  // 角色id
  private String roledataId;

  public void setDataId(String dataId) {
    this.dataId = dataId;
  }

  public void setRoledataId(String roledataId) {
    this.roledataId = roledataId;
  }

  @Id
  @Column(name = "dataId")
  public String getDataId() {
    return dataId;
  }

  @Id
  @Column(name = "roledataId")
  public String getRoledataId() {
    return roledataId;
  }
}
