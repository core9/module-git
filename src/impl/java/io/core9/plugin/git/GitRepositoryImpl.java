package io.core9.plugin.git;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitRepositoryImpl implements GitRepository {
	private static final Logger log = Logger.getLogger(GitRepositoryImpl.class);
	private static final String pathPrefix = "data/git/";
	
	private String localPath;
	private String origin;
	private String username;
	private String password;
	private boolean initialized = false;
	
	private Git git;
	private CredentialsProvider cp;
	
	@Override
	public String getLocalPath() {
		return pathPrefix + localPath;
	}
	
	@Override
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	@Override
	public String getOrigin() {
		return origin;
	}

	@Override
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Initialize the repository, clones or pulls a remote
	 * @return
	 * @throws GitAPIException
	 */
	public GitRepositoryImpl init() throws GitAPIException {
		log.info("Initializing git repository: " + getLocalPath());
		if(getUsername() != null) {
			cp = new UsernamePasswordCredentialsProvider(getUsername(), getPassword());
		} else {
			cp = null;
		}
		
		if(new File(getLocalPath() + "/.git").exists()) {
			git = Git.init().setDirectory(new File(getLocalPath())).call();
			git.reset().setMode(ResetType.HARD).call();
			try {
				pull();
			} catch (GitAPIException | JGitInternalException e) {
				log.error("Couldn't execute git pull: " + e.getMessage());
			}
		} else {
			log.info("Cloning from: " + getOrigin());
			CloneCommand clone = Git.cloneRepository();
			if(cp != null) {
				clone.setCredentialsProvider(cp);
			}
			git = clone
				.setDirectory(new File(getLocalPath()))
				.setURI(origin)
				.call();
		}
		initialized = true;
		return this;
	}
	
	/**
	 * Update the existing repository
	 * @return
	 * @throws GitAPIException 
	 */
	public GitRepositoryImpl pull() throws GitAPIException, JGitInternalException {
		log.info("Pulling from: " + getOrigin());
		PullCommand pull = git.pull();
		if(cp != null) {
			pull.setCredentialsProvider(cp);
		}
		pull.call();
		return this;
	}

	@Override
	public boolean exists() {
		return initialized;
	}

	public GitRepositoryImpl push() throws GitAPIException {
		log.info("Commit on repo: " + this.getLocalPath());
		
		if(new File(getLocalPath() + "/.git").exists()) {
			git = Git.init().setDirectory(new File(getLocalPath())).call();
		}
		
		git.add().addFilepattern(".").call();
		git.commit().setMessage("Updated features").call();
		PushCommand push = git.push();
		if(cp != null) {
			push.setCredentialsProvider(cp);
		}else{
			if(getUsername() != null) {
				cp = new UsernamePasswordCredentialsProvider(getUsername(), getPassword());
				push.setCredentialsProvider(cp);
			} else {
				cp = null;
			}
		}
		log.info("Pushing to remote: " + this.getOrigin());
		push.call();
		return this;
	}
	
}
