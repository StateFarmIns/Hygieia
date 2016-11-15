package com.capitalone.dashboard.collector;

import java.util.List;

import com.capitalone.dashboard.model.GitlabIssue;
import com.capitalone.dashboard.model.GitlabProject;
import com.capitalone.dashboard.model.GitlabTeam;

public interface FeatureDataClient {
	
	void updateTeams(List<GitlabTeam> teams);

	void updateProjects(List<GitlabProject> projects);

	void updateIssuesInProgress(List<GitlabIssue> issues);
	
}
