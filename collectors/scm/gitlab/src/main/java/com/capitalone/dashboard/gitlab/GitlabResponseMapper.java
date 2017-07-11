package com.capitalone.dashboard.gitlab;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.gitlab.model.GitlabCommit;
import com.capitalone.dashboard.gitlab.model.GitlabMergeRequest;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.CommitType;
import com.capitalone.dashboard.model.GitRequest;

@Component
public class GitlabResponseMapper {
    
    public List<Commit> mapCommits(List<GitlabCommit> gitlabCommits, String repoUrl, String branch) {
        List<Commit> commits = new ArrayList<>();
        for (GitlabCommit gitlabCommit : gitlabCommits) {
            commits.add(mapCommit(repoUrl, branch, gitlabCommit));     
        }
        
        return commits;
    }
    
    public List<GitRequest> mapMergeRequests(List<GitlabMergeRequest> mergeRequests, String repoUrl, String branch) {
        List<GitRequest> gitRequests = new ArrayList<>();
        for (GitlabMergeRequest mergeRequest : mergeRequests) {
            gitRequests.add(mapMergeRequest(mergeRequest, repoUrl, branch));
        }
        
        return gitRequests;
    }

    private GitRequest mapMergeRequest(GitlabMergeRequest mergeRequest, String repoUrl, String branch) {
        long createdTimestamp = OffsetDateTime.parse(mergeRequest.getCreatedAt()).toEpochSecond();
        
        GitRequest request = new GitRequest();
        request.setRequestType("pull");
        request.setNumber(mergeRequest.getIid());
        request.setUserId(mergeRequest.getAuthor().getUsername());
        request.setScmUrl(repoUrl);
        request.setScmBranch(branch);
        request.setTimestamp(createdTimestamp);
        request.setCreatedAt(createdTimestamp);
        request.setScmRevisionNumber(mergeRequest.getSha());
        request.setScmCommitLog(mergeRequest.getTitle());
        
        if("closed".equals(mergeRequest.getState())) {
            request.setState("closed");
            request.setClosedAt(OffsetDateTime.parse(mergeRequest.getUpdatedAt()).toEpochSecond());
        }
        if("merged".equals(mergeRequest.getState())) {
            request.setState("merged");
            request.setMergedAt(OffsetDateTime.parse(mergeRequest.getUpdatedAt()).toEpochSecond());
        }
        
        
        return request;
    }

    private Commit mapCommit(String repoUrl, String branch, GitlabCommit gitlabCommit) {
        long timestamp = new DateTime(gitlabCommit.getCreatedAt()).getMillis();
        CommitType commitType = CollectionUtils.isEmpty(gitlabCommit.getParentIds()) ? CommitType.New : CommitType.Merge;
        
        Commit commit = new Commit();
        commit.setTimestamp(System.currentTimeMillis());
        commit.setScmUrl(repoUrl);
        commit.setScmBranch(branch);
        commit.setScmRevisionNumber(gitlabCommit.getId());
        commit.setScmAuthor(gitlabCommit.getAuthorName());
        commit.setScmCommitLog(gitlabCommit.getMessage());
        commit.setScmCommitTimestamp(timestamp);
        commit.setNumberOfChanges(1);
        commit.setScmParentRevisionNumbers(gitlabCommit.getParentIds());
        commit.setType(commitType);
        return commit;
    }

    
}
