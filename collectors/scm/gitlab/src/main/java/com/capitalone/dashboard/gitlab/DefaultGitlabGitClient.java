package com.capitalone.dashboard.gitlab;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.gitlab.model.GitlabCommit;
import com.capitalone.dashboard.gitlab.model.GitlabMergeRequest;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.model.GitlabGitRepo;
import com.capitalone.dashboard.util.Supplier;

/**
 * Created by benathmane on 23/06/16.
 */

@Component
public class DefaultGitlabGitClient implements  GitlabGitClient {

    //Gitlab max results per page. Reduces amount of network calls.
    private static final int RESULTS_PER_PAGE = 100;
    private static final String PAGINATION_HEADER = "X-Next-Page";
    
    private final RestOperations restOperations;
    private final GitlabUrlUtility gitlabUrlUtility;
    private final GitlabResponseMapper responseMapper;
    
    @Autowired
    public DefaultGitlabGitClient(GitlabUrlUtility gitlabUrlUtility,
                                       Supplier<RestOperations> restOperationsSupplier,
                                       GitlabResponseMapper responseMapper) {
        this.gitlabUrlUtility = gitlabUrlUtility;
        this.restOperations = restOperationsSupplier.get();
        this.responseMapper = responseMapper;
    }

    @Override
	public List<Commit> getCommits(GitlabGitRepo repo, boolean firstRun) {
        List<Commit> commits = new ArrayList<>();

		URI uri = gitlabUrlUtility.buildCommitsUrl(repo, firstRun, RESULTS_PER_PAGE);
		List<GitlabCommit> gitlabCommits = makePaginatedGitlabRequest(uri, GitlabCommit[].class);
		commits.addAll(responseMapper.mapCommits(gitlabCommits, repo.getRepoUrl(), repo.getBranch()));
		
        return commits;
    }

    @Override
    public List<GitRequest> getIssues() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GitRequest> getMergeRequests(GitlabGitRepo repo, boolean firstRun) {
        List<GitRequest> mergeRequests = new ArrayList<>();
        
        URI uri = gitlabUrlUtility.buildMergeRequestUrl(repo, firstRun, RESULTS_PER_PAGE);
        List<GitlabMergeRequest> gitlabMergeRequests =  makePaginatedGitlabRequest(uri, GitlabMergeRequest[].class);
        mergeRequests.addAll(responseMapper.mapMergeRequests(gitlabMergeRequests, repo.getRepoUrl(), repo.getBranch()));
        
        return mergeRequests;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> List<T> makePaginatedGitlabRequest(URI uri, Class gitlabResponseType) {
        URI restUri = uri;
        HttpEntity<String> headersEntity = gitlabUrlUtility.buildAuthenticationHeader();
        
        List<T> body =  new ArrayList<>();
        boolean hasNextPage = true;
        while (hasNextPage) {
            ResponseEntity<T[]> response = restOperations.exchange(restUri, HttpMethod.GET, headersEntity, gitlabResponseType);
            CollectionUtils.addAll(body, response.getBody());
            
            if(hasNextPage = hasNextPage(response.getBody().length)) {
                restUri = gitlabUrlUtility.updatePage(restUri, response.getHeaders().get(PAGINATION_HEADER).get(0));
            }
        }
        
        return body;
    }
    
    private boolean hasNextPage(int numberOfResults) {
        return numberOfResults >= RESULTS_PER_PAGE;
    }
    
    
}
