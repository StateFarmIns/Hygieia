package com.capitalone.dashboard.gitlab;

import java.util.List;

import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.model.GitlabGitRepo;

/**
 * Created by benathmane on 23/06/16.
 */

/**
 * Client for fetching commit history from Gitlab
 */
public interface GitlabGitClient {

    /**
     * Fetch all of the commits.
     *
     * @param repo GitlabGitRepo
     * @return all commits in repo
     */

	List<Commit> getCommits(GitlabGitRepo repo, boolean firstRun);
	
	List<GitRequest> getIssues();
	
	List<GitRequest> getMergeRequests(GitlabGitRepo repo, boolean firstRun);
	
}
