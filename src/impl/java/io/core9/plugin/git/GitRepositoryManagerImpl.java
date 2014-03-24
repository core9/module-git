package io.core9.plugin.git;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.eclipse.jgit.api.errors.GitAPIException;

@PluginImplementation
public class GitRepositoryManagerImpl implements GitRepositoryManager {
	
	private Map<String,GitRepositoryImpl> repositories = new HashMap<String,GitRepositoryImpl>();

	@Override
	public GitRepositoryImpl registerRepository(String identifier) {
		if(!repositories.containsKey(identifier)) {
			repositories.put(identifier, new GitRepositoryImpl());
		}
		return repositories.get(identifier);
	}

	@Override
	public void init(GitRepository repo) {
		try {
			((GitRepositoryImpl) repo).init();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pull(GitRepository repo) {
		try {
			((GitRepositoryImpl) repo).pull();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void push(GitRepository repo) {
		try {
			((GitRepositoryImpl) repo).push();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
	}
}
