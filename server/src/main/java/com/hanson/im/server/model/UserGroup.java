package com.hanson.im.server.model;

import java.util.Date;

public class UserGroup {
    private Long id;

    private String groupId;

    private String groupName;

    private Date createTime;

    private Date updateTime;

    private String groupUserIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGroupUserIds() {
        return groupUserIds;
    }

    public void setGroupUserIds(String groupUserIds) {
        this.groupUserIds = groupUserIds == null ? null : groupUserIds.trim();
    }
}