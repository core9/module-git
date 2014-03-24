package io.core9.plugin.git;

import io.core9.core.plugin.Core9Plugin;

public interface GitRepositoryManager extends Core9Plugin {
	
	/**
	 * Register a repository on a path
	 * @param folder
	 * @return the GitRepository object
	 */
	GitRepository registerRepository(String foldername);

	/**
	 * Initialize a repository
	 * @param repo
	 */
	void init(GitRepository repo);

	/**
	 * Update a repository
	 * @param repo
	 */
	void pull(GitRepository repo);

	/**
	 * Push the repository to the remote repo
	 * @param repo
	 */
	void push(GitRepository repo);

}
