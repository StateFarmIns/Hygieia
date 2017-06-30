package com.capitalone.dashboard.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabIssue {

    private String targetBranch;
    private String sourceBranch;
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String state;
    private GitlabUser user;
    
}
