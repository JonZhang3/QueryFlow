package com.queryflow.entity;

import com.queryflow.annotation.Column;
import com.queryflow.annotation.Table;
import com.queryflow.common.FillType;

@Table
public class Entity {

    private String id;

    @Column(value = "is_exists", typeHandler = BooleanTypeHandler.class, updateGroups = {UpdateGroupExists.class})
    private boolean exists;

    @Column(fillDatePattern = "yyyyMMdd", fillType = FillType.INSERT)
    private String createTime;

    @Column(fillType = FillType.INSERT_UPDATE)
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static class UpdateGroupExists {
    }

}
